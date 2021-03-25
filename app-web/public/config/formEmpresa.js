//Define uma variavel pro texto que será criado
var inputText = "";

//Define um elemento input invisivel que será responável por armazenar valor das checkboxes.
var inputHidden = document.getElementById("inputHidden");

function paymentMethod (){
    //Verifica o status das checkboxes
    var dinheiro = document.getElementById("dinheiro").checked;
    var credito = document.getElementById("credito").checked;
    var debito = document.getElementById("debito").checked;
    var voucher= document.getElementById("voucher").checked;

    //Constroi a string 
    if(dinheiro == true){ inputText += "Dinheiro"}
    if(credito == true){ inputText += " Crédito "}
    if(debito == true){ inputText += " Débito "}
    if(voucher == true){ inputText += " Voucher"} 

    //Define o novo valor do input oculto
    inputHidden.value = inputText;
    
    // console.log(inputHidden.value) // for debug
}

