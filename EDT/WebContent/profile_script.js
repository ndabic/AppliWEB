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

