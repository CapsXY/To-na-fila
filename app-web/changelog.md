# Changelog


Todas as mudanças desse sistema serão documentadas nesse arquivo. Aqui também serão publicadas as futuras funcionalidades e eventuais exclusões ou adições. 

![](public/image/tonafila_red.svg)

# **[Released]**

## **[1.0.0]** - 11-06-2019
### Adicionado:
- Implantação da funcionalidade Cadastrar Empresa
- Implantação da funcionalidade Editar dados empresa

## **[1.1.0]** - 12-06-2019
### Adicionado
- Implantação da vizualização da logo ao consultar empresa em 'Editar Empresa'

### Correções:
- Correção de bugs nos checkboxes de forma de pagamento 
- Correção de erros de digitação e formatação de Layouts 

## **[1.1.1]** - 12-06-2019
### Correções:
- Correção de erros de digitação e formatação de Layouts 
- **[FIXED]** Bug que deleta a referencia da logo ao atualizar os dados da empresa no Firebase Database
- **[FIXED]** Bug de renderizaçào da tela ao selecionar uma empresa recem criada para popular seus dados no banco. 

## **[1.1.2]** - 16-06-2019
### Correções: 
- Inclusão de máscara no campo Contato utilizando o plugin Jquery Mask.
- Alteração do padrão do separador da string 'pagamentos' de ','(vírgula) para '-'(traço)
- Correção do dimensionamento da logo do estabelecimento para o padrão 370px x 400px 

## **[1.1.3]** - 17-06-2019
### Correções: 
- Alteração do padrão do separador da string 'pagamentos' de '-'(traço) para ' '(espaço)

## **[1.2.0]** - 17-06-2019
### Correções: 
- **[FIXED]** Ao clicar no botão cadastrar na tela de edição sem dado algum e sem empresa selecionada, eram salvos dados vazios sem nó de empresa no banco de dados 
- Adicionadas linhas de comentário no código explicando todo o funcionamento da aplicação. 

# To Do: Próximas Releases

- Alterar imagem da empresa através de input file gravando o arquivo no Firebase Storage e guardando sua referencia no Firebase Database