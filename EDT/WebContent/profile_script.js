var btnAddEdt = document.querySelector(".button-add-edt");
var edtScroller = document.querySelector("edt-scroller");

btnAddEdt.addEventListener("click", function() {
    //TODO
});




function copyContent(code) {
    navigator.clipboard.writeText(code)
      .then(() => {
        // Show notification
        var notif = document.querySelector(".notif");
        notif.innerHTML = " Code copié ";
        notif.classList.add("notif-show");

        setTimeout(() => {
            notif.classList.remove("notif-show");
            notif.innerHTML = "";
        }, 1000); 
      })
      .catch(err => {
        // Handle any errors
        console.error("Impossible de copier le contenu: ", err);
      });
  }
  
  
  
  

