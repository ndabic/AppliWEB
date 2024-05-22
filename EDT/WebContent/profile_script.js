var btnAddEdt = document.querySelector(".button-add-edt");
var edtScroller = document.querySelector("edt-scroller");

document.addEventListener("DOMContentLoaded", function() {
	updateUserInfoAjax();
	
	var linkInputs = document.querySelectorAll(".link-edt");
	  linkInputs.forEach(input => {
	      input.addEventListener("blur", function() {
	          if (!input.value.trim().startsWith(" ")) {
	              input.value = " " + input.value.trim();
	          }
	      });
	  });
	  
  var linkInputs = document.querySelectorAll(".pswrd-input");
  linkInputs.forEach(input => {
      input.style.color = "var(--color-4)";
      input.addEventListener("blur", function() {
          if (!input.value.startsWith(" ")) {
              input.value = " " + input.value;
          }
          if (input.value === " ") {
              input.value = " Nouveau mot de passe";
              input.style.color = "var(--color-4)";
          }
      });

      input.addEventListener("focus", function(){
          if (input.value === " Nouveau mot de passe") {
              input.value = " ";
              input.style.color = "var(--color-5)";
          }
      });
  });
  
  getEDTsAjax();
});

btnAddEdt.addEventListener("click", function() {
    //TODO
});




function copyContent(code) {
    navigator.clipboard.writeText(code)
      .then(() => {
        var notif = document.querySelector(".notif");
        notif.innerHTML = "Copié";
        notif.classList.add("notif-show");

        setTimeout(() => {
            notif.classList.remove("notif-show");
            notif.innerHTML = "";
        }, 1000); 
      })
      .catch(err => {
        console.error("Impossible de copier le contenu: ", err);
      });
  }

function getEdtsAjax(){	
    var formData = new FormData(); // Serialize form data

    formData.append("op", "getEdts");
    
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "Serv", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // Set content type
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Request succeeded
            	var res = xhr.responseText;
            	if (res.startsWith("success")) {
            		var ress = res.split(":");
            		var ress2 = ress[1].split(",");
            	    document.getElementById("user-firstname").value = ress2[0];
            	    document.getElementById("user-name").value = ress2[1];
            	    document.getElementById("user-email").value = ress2[2];
            	} else {
            		console.error("pas d'utilisateur:", res);
            	}

            } else {
                // Request failed
                console.error("Request failed with status:", xhr.status);
            }
        }
    };
    xhr.send(new URLSearchParams(formData)); // Send form data
	
}

function validateForm(popupInfo) {
    switch (popupInfo) {

        case 'createEdt':
                var name = document.forms["createEdt"]["nom-edt"].value;

                if (name == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champ obligatoire";

                    return false;
                }
            break;

        case 'linkEdt':
                var code = document.forms["linkEdt"]["code-link"].value;
                var num = document.forms["linkEdt"]["num-link"].value;

                if (code == "" || num == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;
    }
    
    return true;
}

function submitFormAjax(formID) {
	if (validateForm(formID)){
		var form = document.getElementById(formID);
	
	    var formData = new FormData(form); // Serialize form data
	
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "Serv", true);
	    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // Set content type
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState === XMLHttpRequest.DONE) {
	            if (xhr.status === 200) {
	                // Request succeeded
	                document.querySelector("h1").innerHTML = xhr.responseText;
	                popUpContainer.classList.toggle("show-flex");
	                popUp.innerHTML = "";
	                contentAdded();
	            } else {
	                // Request failed
	                console.error("Request failed with status:", xhr.status);
	            }
	        }
	    };
	    xhr.send(new URLSearchParams(formData)); // Send form data
	}
    
}
  

function updateUserInfoAjax(){	
    var formData = new FormData(); // Serialize form data

    formData.append("op", "getUserInfos");
    
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "Serv", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // Set content type
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Request succeeded
            	var res = xhr.responseText;
            	if (res.startsWith("success")) {
            		var ress = res.split(":");
            		var ress2 = ress[1].split(",");
            	    document.getElementById("user-firstname").value = ress2[0];
            	    document.getElementById("user-name").value = ress2[1];
            	    document.getElementById("user-email").value = ress2[2];
            	} else {
            		console.error("pas d'utilisateur:", res);
            	}

            } else {
                // Request failed
                console.error("Request failed with status:", xhr.status);
            }
        }
    };
    xhr.send(new URLSearchParams(formData)); // Send form data
	
}



var popUpContainer = document.querySelector(".popup-container");
var popUp = document.querySelector(".popup");

function toggleShow(self) {
    var popupInfo = self.innerHTML;
    var togglePopUp = false;
	switch (popupInfo) {
			case '+':
				popupInfo = "Créer EDT"
	            popUp.innerHTML = `
	                    <div class="header-line">
	                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
	                        <button onclick="toggleShow(this)" class="button-exit">X</button>
	                    </div>
	                    <form id="createEdt" name="createEdt" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
	                        <table class="popup-table">
	                            <tr>
	                                <td><label for="nom-edt">Nom:</label></td>
	                                <td><input type="text" id="nom-edt" name="nom-edt"><br></td>
	                            </tr>
	                        </table>
	                        <div class="center-line">
	                            <button type="button" class="submit-button" onclick="submitFormAjax('createEdt')">Créer</button>
	                        </div>
	                        <input type="hidden" name="op" value="createEdt">
	                    </form>
	                    <div class="error-message"></div>
	                `;
	
	            
	            togglePopUp = true;
	            break;
	            
			case '-//-':
				popupInfo = "Rejoindre un EDT"
	            popUp.innerHTML = `
	                    <div class="header-line">
	                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
	                        <button onclick="toggleShow(this)" class="button-exit">X</button>
	                    </div>
	                    <form id="linkEdt" name="linkEdt" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
	                        <table class="popup-table">
	                            <tr>
	                                <td><label for="code-link">Code EDT:</label></td>
	                                <td><input type="text" id="code-link" name="code-link"><br></td>
	                            </tr>
	                            <tr>
	                                <td><label for="num-link">Numéro étudiant:</label></td>
	                                <td><input type="text" id="num-link" name="num-link"><br></td>
	                            </tr>
	                        </table>
	                        <div class="center-line">
	                            <button type="button" class="submit-button" onclick="submitFormAjax('linkEdt')">Lier</button>
	                        </div>
	                        <input type="hidden" name="op" value="linkEdt">
	                    </form>
	                    <div class="error-message"></div>
	                `;
	
	            
	            togglePopUp = true;
	            break;
	       
			case 'X':
	            popUpContainer.classList.toggle("show-flex");
	            popUp.innerHTML = "";
	            break;

		}
	    if (togglePopUp){
	          
		    popUpContainer.classList.toggle("show-flex");
	    }
}

