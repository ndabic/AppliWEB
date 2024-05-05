function validateForm() {
    var mail = document.forms["signin"]["mail"].value;
    var password = document.forms["signin"]["mdp"].value;

    if (mail == "" || password == "") {
        var errorMessage = document.querySelector(".error-message");
        errorMessage.innerHTML = "Veuillez renseigner tous les champs";

        return false;
    }
    

    return true;
}

