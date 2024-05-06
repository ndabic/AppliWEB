var btnAddEdt = document.querySelector(".button-add-edt");
var edtScroller = document.querySelector("edt-scroller");

btnAddEdt.addEventListener("click", function() {
    //TODO
});


document.addEventListener("DOMContentLoaded", function() {
  var linkInputs = document.querySelectorAll(".link-edt");
  linkInputs.forEach(input => {
      input.addEventListener("blur", function() {
          if (!input.value.trim().startsWith(" ")) {
              input.value = " " + input.value.trim();
          }
      });
  });
});


function copyContent(code) {
    navigator.clipboard.writeText(code)
      .then(() => {
        var notif = document.querySelector(".notif");
        notif.innerHTML = "Copié ";
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


document.addEventListener("DOMContentLoaded", function() {
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
  
  

