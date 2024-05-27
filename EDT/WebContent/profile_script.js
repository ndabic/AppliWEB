var btnAddEdt = document.querySelector(".button-add-edt");
var edtScroller = document.querySelector("edt-scroller");

var nbEDT = 0;

function getCookie(name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function deleteCookie(name, path = "/", domain = "") {
    if (getCookie(name)) {
        document.cookie = name + "=" +
            "; expires=Thu, 01 Jan 1970 00:00:00 UTC" +
            "; path=" + path +
            (domain ? "; domain=" + domain : "") +
            ";";
    }
}

function deleteCookieAndRefresh(name, path = "/", domain = "localhost") {
    deleteCookie(name, path, domain);
    window.location.href = "schedule.html"; // Refresh the page
}

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
  
  getEdtsAjax();
});

btnAddEdt.addEventListener("click", function() {
    //TODO
});


function modifyInfos() {
    var notif = document.querySelector(".notif");
    notif.innerHTML = "Modifié";
    notif.classList.add("notif-show");

    setTimeout(() => {
        notif.classList.remove("notif-show");
        notif.innerHTML = "";
    }, 1000); 
  }

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
            		var edts = ress[1].split(";");
            		var edtScroller = document.querySelector(".edt-scroller");
            		for(let i = 0; i < edts.length-1; i++){
            			var edt = edts[i].split(",");
            			if (edt[0] == "0"){ // edt admin
            				edtScroller.innerHTML += `
		            					<div class="edt edt-admin ${nbEDT}" onclick="adminEdtForm(this)">
				                            <div class="edt-name">${edt[1]}</div>
				                            <div class="edt-interface ">
				                            	<div class="label-code">Admin:</div>
				                                <div class="code-edt padded-container code-admin ${nbEDT}" onclick="copyContent('${edt[2]}')">${edt[2]}</div>
				                                <div class="label-code">Professeur:</div>
				                                <div class="code-edt padded-container code-prof ${nbEDT}" onclick="copyContent('${edt[3]}')">${edt[3]}</div>
				                                <div class="label-code">Etudiant:</div>
				                                <div class="code-edt padded-container code-etu ${nbEDT}" onclick="copyContent('${edt[4]}')">${edt[4]}</div>
				                                <button class="remove-edt ${nbEDT}" title="Détruire l'edt">X</button>
				                            </div>
				                        </div>
				                        `
            				
            			}else{
            				edtScroller.innerHTML += `
		            					<div class="edt edt-linked ${nbEDT}">
						                    <div class="edt-name">${edt[1]}</div>
						                    <div class="edt-interface">
						                        <div class="code-edt padded-container code-admin ${nbEDT}" onclick="copyContent('${edt[2]}')">${edt[2]}</div>
						                        <label for="link-number" class="label-edt label-num padded-container" onclick="copyContent('${edt[3]}')">Numéro:</label>
						                        <input id="link-number" class="link-edt padded-container" value="${edt[3]}"></input>
						                        <button class="save-number ${nbEDT}" title="Sauvegarder le numéro courant">S</button>
						                        <button class="remove-edt ${nbEDT}" title="Détruire l'edt">X</button>
						                    </div>
						                </div>
						                `
            			}
            			nbEDT += 1;
            		}
            	} else {
            		console.error(res);
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

function createLinkEDTAjax(formID) {
	if (validateForm(formID)){
		var form = document.getElementById(formID);
	
	    var formData = new FormData(form); // Serialize form data
	
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "Serv", true);
	    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // Set content type
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState === XMLHttpRequest.DONE) {
	            if (xhr.status === 200) {
	            	var res = xhr.responseText;
	            	if (res.startsWith("success")) {
	            		var ress = res.split(":");
	            		var edtScroller = document.querySelector(".edt-scroller");
	            		var edt = ress[1].split(",");
            			if (edt[0] == "0"){ // edt admin
            				edtScroller.innerHTML += `
		            					<div class="edt edt-admin ${nbEDT}" onclick="adminEdtForm(this)">
				                            <div class="edt-name">${edt[1]}</div>
				                            <div class="edt-interface ">
				                            	<div class="label-code">Admin:</div>
				                                <div class="code-edt padded-container code-admin ${nbEDT}" onclick="copyContent('${edt[2]}')">${edt[2]}</div>
				                                <div class="label-code">Professeur:</div>
				                                <div class="code-edt padded-container code-prof ${nbEDT}" onclick="copyContent('${edt[3]}')">${edt[3]}</div>
				                                <div class="label-code">Etudiant:</div>
				                                <div class="code-edt padded-container code-etu ${nbEDT}" onclick="copyContent('${edt[4]}')">${edt[4]}</div>
				                                <button class="remove-edt ${nbEDT}" title="Détruire l'edt">X</button>
				                            </div>
				                        </div>
				                        `
            				
            			}else{
            				edtScroller.innerHTML += `
		            					<div class="edt edt-linked ${nbEDT}">
						                    <div class="edt-name">${edt[1]}</div>
						                    <div class="edt-interface">
						                        <div class="code-edt padded-container code-admin ${nbEDT}" onclick="copyContent('${edt[2]}')">${edt[2]}</div>
						                        <label for="link-number" class="label-edt label-num padded-container" onclick="copyContent('${edt[3]}')">Numéro:</label>
						                        <input id="link-number" class="link-edt padded-container" value="${edt[3]}"></input>
						                        <button class="save-number ${nbEDT}" title="Sauvegarder le numéro courant">S</button>
						                        <button class="remove-edt ${nbEDT}" title="Détruire l'edt">X</button>
						                    </div>
						                </div>
						                `
            			}
            			nbEDT += 1;
	            	} else {
	            		console.error(res);
	            	}
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

function modifyInfoAjax(modifPswd){	
    var formData = new FormData(); // Serialize form data

    var correct = false;
    
    if (modifPswd){
	    formData.append("op", "save-user-pswrd");
    	var pswd = document.getElementById("user-new-pswd").value;
	    var pswdConf = document.getElementById("user-new-pswd-conf").value;
	    
	    formData.append("user-new-pswd", pswd);
	    formData.append("user-new-pswd-conf", pswdConf);
	    
	    if (!(pswd == " Nouveau mot de passe" || pswd == "" || pswdConf == " Nouveau mot de passe" || pswdConf == "" )){
	    	correct = true;
	    }
    }else{
	    formData.append("op", "save-user-infos");
	    var firstname = document.getElementById("user-firstname").value;
	    var name = document.getElementById("user-name").value;
	    var email = document.getElementById("user-email").value;
	    
	    formData.append("user-firstname", firstname);
	    formData.append("user-name", name);
	    formData.append("user-email", email);
	    
	    if (!(firstname == "" || name == "" || email == "" )){
	    	correct = true;
	    }
    }
    if(correct){
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "Serv", true);
	    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // Set content type
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState === XMLHttpRequest.DONE) {
	            if (xhr.status === 200) {
	                // Request succeeded
	            	var res = xhr.responseText;
	            	if (res.startsWith("success")) {
	            		if (modifPswd){
	            			document.getElementById("user-new-pswd").value = " Nouveau mot de passe";
	            		    document.getElementById("user-new-pswd-conf").value = " Nouveau mot de passe";
	            		}
	            		modifyInfos();
	            	    
	            	} else {
	            		console.error(res);
	            	}
	
	            } else {
	                // Request failed
	                console.error("Request failed with status:", xhr.status);
	            }
	        }
	    };
	    xhr.send(new URLSearchParams(formData)); // Send form data
    }
}

function adminEdtForm(self) {
	
	
	var codeA = self.querySelector(".code-admin").innerHTML;
	var codeP = self.querySelector(".code-prof").innerHTML;
	var codeE = self.querySelector(".code-etu").innerHTML;
    // Create a new form element
    var form = document.createElement('form');
    form.method = 'POST'; // or 'GET'
    form.action = 'Serv'; // replace with your endpoint

    // Create and append input elements to the form
    var nameInput = document.createElement('input');
    nameInput.type = 'hidden';
    nameInput.name = 'edt';
    nameInput.value = ""+codeA+","+codeP+","+codeE; // replace with dynamic value if needed
    form.appendChild(nameInput);
    
    var nameInput = document.createElement('input');
    nameInput.type = 'hidden';
    nameInput.name = 'op';
    nameInput.value = "adminEDT"; // replace with dynamic value if needed
    form.appendChild(nameInput);

    // Append the form to the document body
    document.body.appendChild(form);

    // Submit the form
    form.submit();
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
	                            <button type="button" class="submit-button" onclick="createLinkEDTAjax('createEdt')">Créer</button>
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
	                            <button type="button" class="submit-button" onclick="createLinkEDTAjax('linkEdt')">Lier</button>
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

