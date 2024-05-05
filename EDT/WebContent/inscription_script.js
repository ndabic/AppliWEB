function validateForm() {
    var prenom = document.forms["signup"]["prenom"].value;
    var nom = document.forms["signup"]["nom"].value;
    var mail = document.forms["signup"]["mail"].value;
    var password = document.forms["signup"]["mdp"].value;
    var passwordConf = document.forms["signup"]["mdpConf"].value;
    var errorMessage = document.querySelector(".error-message");
    errorMessage.innerHTML = "";
    if (prenom == "" || nom == "" || mail == "" || password == "" || passwordConf == "") {
        
        errorMessage.innerHTML = "Veuillez renseigner tous les champs";

        return false;
    }
    if(password != passwordConf) {
        errorMessage.innerHTML = "Les mots de passe doivent correspondre";

        return false;
    }

    return true;
}


