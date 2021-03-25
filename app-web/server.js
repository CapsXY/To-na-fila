const express = require('express') //Resposavel pelo servidor HTTP
const app = express(); // Instância da Biblioteca Express
const handlebars = require('express-handlebars') // Bliblioteca JavaScript para gerar HTML dinamicamente 
const path = require("path")// Biblioteca Nativa para coletar o caminho do diretório raiz
const session = require("express-session") //Biblioteca para gerenciar sessão de usuários
const flash = require("connect-flash") // Biblioteca para gerenciar variáveis locais
const cors = require("cors"); // Biblioteca que permite a integração de funcionalidades que estão em diferentes domínios. (Necessário para deploy no Heroku)

// Sessão
app.use(session({
    secret:"eb8f2767383cc2a8805f232ab824e47e",
    resave: true,
    saveUninitialized: true
}))
app.use(flash())

// Middleware - Globals 
app.use((req,res,next)=>{
    res.locals.success_msg = req.flash("success_msg");
    res.locals.error_msg = req.flash("error_msg");
    res.locals.empresas = req.flash("empresas");
    res.locals.dadoEmpresa =req.flash("dadoEmpresa");
    
    // Variáveis de forma de pagamento
    res.locals.pagamento = req.flash("pagamento");
   
    next();
})

//Express Body Parser Config 
app.use(express.json())// Para decodificar as REQs em JSON
app.use(express.urlencoded({ extended: true})) // Para permitir o envio e arquivos

//Routes
app.use(require('./routes'))

//Template Engine - configura o handlebars
app.engine('handlebars', handlebars({defaultLayout:'main'}))
app.set('view engine', 'handlebars')

//Public - caminho para acesso ao diretório público que contém os arquivos estáticos da página 
app.use(express.static(path.join(__dirname,"public")))

//Define a porta onde será executada a aplicação
app.listen(process.env.PORT || 3000, function(){
    console.log("-- Servidor iniciado em http://localhost:3000");
})

