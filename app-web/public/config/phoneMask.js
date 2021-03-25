$(document).ready(function(){
  $("#contato").mask("(00) 0000-00009")
  $("#contato").blur(function(event){
    if ($(this).val().length == 15){
      $("#contato").mask("(00) 00000-0009")
    }else{
      $("#contato").mask("(00) 0000-00009")
    }
  })
})