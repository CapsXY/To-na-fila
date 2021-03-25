const express = require('express');
const routes = express.Router();
const firebase = require ("./public/config/firebaseProd");
const authenticate = firebase.auth(); 
const database  = firebase.database();

//Rota que carrega a homepage
routes.get('/', (req, res)=>{res.render('dashboard')});

// -- Criação de conta empresa
// Rota que carrega a tela de "Criação de conta empresa" 
routes.get('/criarUsuario', (req,res)=>{res.render('conta')});

//Rota que trata o envio do formulario para Criação de conta empresa
routes.post('/criarUsuario', (req,res)=>{
    // -- Validação de Dados do formulário
    // Variáveis que vão guardar as mensagens de erro
    var erro = [];
    // Verificação simples para evitar que um dos dados seja inexistente, indefinido ou nulo. 
    if(!req.body.nome || typeof req.body.nome == undefined || req.body.nome == null){erro.push({msg:"Nome inválido"})}
    if(!req.body.email || typeof req.body.email == undefined || req.body.email == null){erro.push({msg:"Email inválido"})}
    if(!req.body.password || typeof req.body.emaipasswordl == undefined || req.body.password == null){erro.push({msg:"Senha inválida"})}
    else if(req.body.password !== req.body.confirmpassword){erro.push({msg:"As senhas não conferem!"})}

    // -- Validação de integridadedos dados e script de criação da conta 
    if(erro.length > 0){
        //Se houver erros, rederiza as mensagens de erro na tela do usuário
        res.render('conta',{error: erro})
    } else {
        // Se não houver erros, coleta-se as informações de email e senha a partir do corpo da requisição POST
        const email     = req.body.email;
        const password  = req.body.password;
        
        // A partir da intancia do Firebase Auth chama-se o método de criar novo usuário com email e senha. 
        authenticate.createUserWithEmailAndPassword(email, password)
        // Caso o metodo retorne sucesso o método THEN é chamado. 
        .then((result)=>{
            //Variáveis utilizadas para popular o Firebase Database
            const uidUser = result.user.uid;
            const data = {
                "nome": req.body.nome,
                "idUsuario": uidUser,
                "tipo": "empresa"
            };

            //Definimos o caminho onde devemos salvar os dados a partir do método ref() do Firebase Database 
            const dataCliente = database.ref(`usuarios/${uidUser}`);
            
            // O método SET efetiva a gravação dos dados do usuário criado no Firebase Auth dentro do Database. 
            dataCliente.set(data)
            .then((success)=>{
                
                // Define um valor para variavel global que armazena as mensagens de sucesso.
                req.flash("success_msg", "Usuário criado com sucesso!")
                
                // Redireciona para a página anterior
                res.redirect('..')
            })
            .catch((err)=>{
                console.log(err);
                // Define um valor para variavel global que armazena as mensagens de erro.
                erro.push({msg:"Erro ao gravar dados do usuário no banco de dados."});
                //Rederiza as mensagens de erro na tela do usuário
                res.render('conta', {error: erro});
                
            })

            
        })
        .catch((err)=>{
            // Define um valor para variavel global que armazena as mensagens de erro.
            erro.push({msg:"Falha ao criar usuário! Por favor, tente novamente."});
            //Rederiza as mensagens de erro na tela do usuário
            res.render('conta', {error: erro}); 
            
        })
    }
})
   
// -- Edição / Cadastro de informações da conta empresa
// Carrega a tela de "Editar Empresa"
routes.get('/empresa', (req, res)=>{
    const query = database.ref("usuarios")
    var data = [];
    // Consulta as contas tipo empresa para listar-las em  "Selecione uma empresa"
    query.once("value")
    .then((snap)=>{
        // Percorre os dados retornados para preencher o select automaticamente com os nomes de estabalecimentos
        snap.forEach(function(child) {
          if(child.val().tipo == "empresa"){
            data.push(child.val())
          }
        }) 
        // Rederiza a tela passando informações de Titulo da Página Informações das Empresas e Estado do botão de "Cadastrar"
        res.render('empresa', {
            title:"Empresa",
            empresas: data,
            btn: {ativo:"disabled"}
        }); 
       
    });
});

//Rota que trata o envio do formulario após "Selecionar uma Empresa"
routes.post('/empresa', (req, res)=>{
    if(req.body.empresa != "Escolha..."){
        const query = database.ref("empresas/" + req.body.empresa)
        const drop = database.ref("usuarios") 
        var data = [];
        //Repopula o dropdow de seleção de empresa
        drop.once("value")
        .then((snap)=>{
            snap.forEach(function(child) {
                if(child.val().tipo == "empresa"){
                    data.push(child.val())
                  }
            }) 
        })

        //Renderiza a pagina preenchendo conforme os dados da empresa selecionada. 
        query.once("value")
        .then((snap)=>{
            //Variavel de referencia a cada empresa retornada pelo firebase
            const item = snap.val();

            if (item != null){
                //Default das configurações de pagamento
                const info = {
                    "dinheiro": {id:"dinheiro", texto:"Dinheiro", status: ""},
                    "credito":  {id:"credito", texto:"Crédito", status: ""},
                    "debito":   {id:"debito", texto:"Débito", status: ""},
                    "voucher":  {id:"voucher", texto:"Voucher", status: ""}
                };
            
                //Coleta as informações de pagamento do firebase e distribui em um objeto
                const formasPagamento =  item.pagamento.split(' ');

                //Se a string do objeto for igual a do checkbox o mesmo é marcado automaticamente
                formasPagamento.forEach(function(obj){
                    const value = obj.trim();
                    if (value == "Dinheiro") {info.dinheiro.status = "checked"}
                    if (value == "Crédito") {info.credito.status = "checked"}
                    if (value == "Débito") {info.debito.status = "checked"}
                    if (value == "Voucher") {info.voucher.status = "checked"}
                    
                })
                // Caso a empresa não possa imagem definida exibe-se a default
                if(item.urlImagemLogo == undefined){
                    item.urlImagemLogo = "https://firebasestorage.googleapis.com/v0/b/tonafilavf-2019.appspot.com/o/imagens%2Fempresas%2Flogos%2FunknownPlace.jpg?alt=media&token=ec07055d-ed18-4549-9bef-2fa1adb34a58"
                }

                 //Rederiza os resultados na tela
                res.render('empresa', {
                    title:item.nome,  
                    dadoEmpresa: item,
                    empresas:data,
                    pagamento: info,
                    btn: {ativo:""}
                }) 
            } else {
                // Caso não exista registro da empresa no banco carregam-se os dados básicos com informações do nó de usuários 
                const reference = database.ref("usuarios/" + req.body.empresa)
                reference.once("value")
                .then((snap)=>{
                    var labels = snap.val();
                    const item = { categoria: "",
                    contato: "" ,
                    descricao:"" ,
                    horario:"",
                    idUsuario: req.body.empresa ,
                    informacao:"" ,
                    nome: labels.nome,
                    pagamento: "",
                    preco: "",
                    urlImagemLogo:"https://firebasestorage.googleapis.com/v0/b/tonafilavf-2019.appspot.com/o/imagens%2Fempresas%2Flogos%2FunknownPlace.jpg?alt=media&token=ec07055d-ed18-4549-9bef-2fa1adb34a58" };
                    res.render('empresa',{
                        title:item.nome,
                        dadoEmpresa: item,
                        empresas: data,
                        pagamento:"",
                        btn: {ativo:""}
                    })
                })
               
            }
            
           

        })
        .catch((err)=>console.log(err))
    } else {
        // Define um valor para variavel global que armazena as mensagens de erro.
        req.flash("error_msg", "Por favor, selecione uma empresa.")
         // Redireciona para a página anterior
        res.redirect('../empresa')
    }
})

//Rota que trata o envio do formulario para Edição de conta empresa
routes.post("/empresa/update", (req, res)=>{
    const query = database.ref("empresas/" + req.body.idUsuario)
   // Grava no banco de dados do Firebase as informaçoes atualizadas da empresa. 
    query.update(req.body)
    .then(()=>{
        // Define um valor para variavel global que armazena as mensagens de sucesso.
        req.flash("success_msg", "Dados da empresa " + req.body.nome + " atualizados com sucesso!")
        // Redireciona para a página anterior
        res.redirect('..')
    })
    .catch((err)=>{
        // Define um valor para variavel global que armazena as mensagens de erro.
        req.flash("error_msg", " Erro ao atualizar dados da empresa!")
        // Redireciona para a página anterior
        res.redirect('..')
    })
})



module.exports = routes;