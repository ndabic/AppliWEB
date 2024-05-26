const delay = ms => new Promise(res => setTimeout(res, ms));
let waitingTime = 30;
const days = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

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
	checkCookieAjax();
    

    const scheduleTable = document.getElementById("schedule");

    // Create the table header
    let tableHTML = "<table class='schedule-table'>";
    tableHTML += "<tr class='weekDays'>";
    days.forEach(day => {
        tableHTML += `<th class="${day}">${day}</th>`;
    });
    tableHTML += "</tr>";

    for (let index1 = 8; index1 < 20; index1+=2) {
        tableHTML += "<tr>";
        for (let index2 = 0; index2 < 5; index2++) {
            tableHTML += "<td class='slot "+index2+"_"+index1+"'></td>";
            
        }
        tableHTML += "</tr>";
    }
    

    tableHTML += "</table>";

    scheduleTable.innerHTML = tableHTML;

    async function getters() {
        try {
            await checkCookieAjax();
            console.log("checkCookieAjax OK");
            await getAllMondaysYearAjax();
            console.log("getAllMondaysYearAjax OK");
            await getWeekAjax();
            console.log("getWeekAjax OK");
            await getCoursGroupesAjax();
            console.log("getCoursGroupesAjax OK");
        } catch (error) {
            console.error("An error occurred:", error);
        }
    }
    getters();
    
    printHours();
    
    showTime();
    


});

/**
 * Attention fonction un peu brut force pour afficher les horaires sur l'EDT
 */
function printHours() {
    
    for (let index1 = 8; index1 < 20; index1+=2) {
        let cours_slot = document.getElementsByClassName("slot 0_"+index1);
        for (let index = 0; index < cours_slot.length; index++) {
            const element = cours_slot[index];
            element.innerHTML = "<div class='scheduleHours "+index1+"'>"+index1+":00 -</div>"+
                                "<div class='scheduleHours "+(index1+1)+"'>"+(index1+1)+":00 -</div>";
            if (index1 >= 18){
                element.innerHTML += "<div class='scheduleHours "+20+"'>"+20+":00 -</div>";
            }
        }
    }
    let first_hours = document.getElementsByClassName("scheduleHours ");
    for (let index = 0; index < first_hours.length; index++) {
        const element = first_hours[index];
        if (index%2==0){
            element.style.top = "-5%";
        }else{
            element.style.top = "45%";
        }
        element.style.left = "-40px";
    }

    
    let last_hour = document.getElementsByClassName("scheduleHours "+20);
    for (let index = 0; index < last_hour.length; index++) {
        const element = last_hour[index];
        element.style.top = "95%";
    }
}

const btn1 = document.querySelector(".button-color-1");
const btn2 = document.querySelector(".button-color-2");

btn1.onmouseenter = event => btn2.classList.add("sipped");
btn1.onmouseleave = event => btn2.classList.remove("sipped");

const showTime = async () => {
    
    await delay(500);
    //place_cours("rgba(255, 45, 100, 0.5)", "WEB", "TP", "A002", ["M1", "M2"], ["Daniel", "HAGIMONT"], [[8, 0],[15,45]], 2);
    /*let cours = document.getElementsByClassName("case_cours");
    for (let index = 0; index < cours.length; index++) {
        await delay(waitingTime);
        const element = cours[index];
        element.classList.add("print-out");
        //waitingTime += 20;
    }*/
    
};

function get_slot(heureDebut){
    switch (heureDebut) {
        case 8: case 9:
            return 8;
        case 10: case 11:
            return 10;
        case 12: case 13:
            return 12;
        case 14: case 15:
            return 14;
        case 16: case 17:
            return 16;
        case 18: case 19: case 20:
            return 18;
        default:
            break;
    }
}

/**
 * 
 * @param {*string} matiere 
 * @param {*string} type 
 * @param {*string} salle 
 * @param {*string[]} groupes 
 * @param {*string[]} prof 
 * @param {*int[][]} horaire 
 * @param {*int} jour 
 */
function place_cours(couleur, matiere, type, salles, groupes, profs, horaire, jour) {
    
    const showTimePlace = async () => {
        let heureDebut = horaire[0][0];
        console.log(heureDebut);
        let slot_debut = get_slot(heureDebut);
        let slot_fin = get_slot(horaire[1][0]);
        console.log(slot_debut);

        let cours_slot = document.getElementsByClassName("slot "+jour+"_"+slot_debut);
        
        for (let index = 0; index < cours_slot.length; index++) {

            let coursHTML = "";
            const element = cours_slot[index];
            coursHTML += "<div class='case_cours "+horaire[0][0]+"_"+horaire[0][1]+"_"+horaire[1][0]+"_"+horaire[1][1]+"'></div>";
            element.innerHTML += coursHTML;

            const case_cours = document.getElementsByClassName("case_cours "+horaire[0][0]+"_"+horaire[0][1]+"_"+horaire[1][0]+"_"+horaire[1][1]);

            for (let i = 0; i < case_cours.length; i++) {
                const singleCase = case_cours[i];

                // purement décoratif
                singleCase.style.top = "0%";
                singleCase.style.left = "0";
                singleCase.style.height = "0%";
                singleCase.style.backgroundColor = "rgba(255, 255, 255, 0.)";
                await delay(waitingTime);
                //
                
                percentage = ((horaire[0][0])%slot_debut)*50 + (horaire[0][1])*(50/60);
                percentage_duration = (((horaire[1][0])*60) + horaire[1][1] - ((horaire[0][0])*60) - horaire[0][1])*(50/60);
                console.log(percentage);
                console.log(percentage_duration);


                let groupesHTML = "<p><span class='info_cours'>Groupe";
                if (groupes.length > 1)
                    groupesHTML += "s: ";
                else
                    groupesHTML += ": ";
                for (let index2 = 0; index2 < groupes.length; index2++) {
                    const groupe = groupes[index2];
                    groupesHTML += groupe;
                    if (index2 + 1 < groupes.length)
                        groupesHTML += ", ";

                }
                groupesHTML += "</span></p>";

                let sallesHTML = "<p><span class='info_cours'>Salle";
                if (salles.length > 1)
                	sallesHTML += "s: ";
                else
                	sallesHTML += ": ";
                for (let index2 = 0; index2 < salles.length; index2++) {
                    const salle = salles[index2];
                    sallesHTML += salle;
                    if (index2 + 1 < salles.length)
                    	sallesHTML += ", ";

                }
                sallesHTML += "</span></p>";
                
                let profsHTML = "<p><span class='info_cours'>";
                for (let index2 = 0; index2 < profs.length; index2++) {
                    const prof = profs[index2];
                    profsHTML += prof;
                    if (index2 + 1 < profs.length)
                    	profsHTML += ", ";

                }
                profsHTML += "</span></p>";

                singleCase.innerHTML = "<p><span class='nom_cours'>"+type+" - "+matiere+"</span></p>"+
                						sallesHTML+
                						profsHTML+
                                        groupesHTML;



                singleCase.style.backgroundColor = couleur;
                singleCase.style.backgroundRepeat = "no-repeat";
                singleCase.style.top = percentage+"%";
                singleCase.style.left = "0";
                singleCase.style.height = "calc("+percentage_duration+"% + "+(slot_fin-slot_debut)+"px";
                singleCase.style.transition = "top 600ms, height 1200ms, background-color 300ms ease-in-out";
                


                
                
                //await delay(waitingTime);
                
                
                
                                        
            }
        }
    }
    showTimePlace();
    
}

function contentAdded() {
    var notif = document.querySelector(".notif");
    notif.innerHTML = "Ajouté";
    notif.classList.remove("hide");

    setTimeout(() => {
        notif.classList.add("hide");
        notif.innerHTML = "";
    }, 1000);
 }

function submitForm(form) {
    form.submit();
}

function getWeekAjax() {
    return new Promise((resolve, reject) => {
        var formData = new FormData();

        formData.append("op", "getWeek");
        var lun = document.querySelector(".monday-button.clicked").innerHTML;
        lun = lun.replace(/\//g, ",");
        formData.append("lundi", lun);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "Serv", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var res = xhr.responseText;
                    if (res.startsWith("success")) {
                        var ress = res.split(":");
                        var ress2 = ress[1].split(";");
                        for (let i = 0; i < 5; i++) {
                            var day = days[i] + " " + ress2[i];
                            document.querySelector("." + days[i]).innerHTML = day;
                        }
                        
                    } /*else {
                        reject(new Error(res));
                    }*/
                    resolve();
                } else {
                    reject(new Error("Request failed with status: " + xhr.status));
                }
            }
        };
        xhr.send(new URLSearchParams(formData));
    });
}

function getAllMondaysYearAjax() {
    return new Promise((resolve, reject) => {
        var formData = new FormData();

        formData.append("op", "getAllMondaysYear");

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "Serv", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var res = xhr.responseText;
                    if (res.startsWith("success")) {
                        var ress = res.split(":");
                        var lundiThisWeek = ress[1];
                        var ress2 = ress[2].split(";");
                        var mondays = document.querySelector(".mondays-container");
                        var buttonToScrollTo = null;
                        for (let i = 0; i < ress2.length-1; i++) {
                            var day = ress2[i];
                            if (day == lundiThisWeek){
                            	mondays.innerHTML += `<button class="monday-button clicked" onclick="toggleClickedOneAlways(this)">${day}</button>`;
                            	buttonToScrollTo = document.querySelector(".monday-button.clicked");
                            }else{
                            	mondays.innerHTML += `<button class="monday-button" onclick="toggleClickedOneAlways(this)">${day}</button>`;
                            }
                            
                            
                        }
                        
                    } /*else {
                        reject(new Error(res));
                    }*/
                    resolve();
                } else {
                    reject(new Error("Request failed with status: " + xhr.status));
                }
            }
        };
        xhr.send(new URLSearchParams(formData));
    });
}


function checkCookieAjax() {
    return new Promise((resolve, reject) => {
        var formData = new FormData();

        formData.append("op", "getUserInfos");

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "Serv", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var res = xhr.responseText;
                    if (res.startsWith("success")) {
                        var ress = res.split(":");
                        var ress2 = ress[1].split(",");
                        var profile = document.querySelector(".user-profile");
                        profile.innerHTML = ress2[0] + " " + ress2[1];
                        profile.classList.remove("hide");
                        
                        var logOut = document.querySelector(".button-logout").classList.remove("hide");

                        document.querySelector(".user-container").classList.add("connected");

                        document.querySelectorAll(".sign-button").forEach(function (button) {
                            button.classList.add("hide");
                        });
                    }else {
                		console.error("cookies différents:", res);
                	    document.querySelector(".user-profile").classList.add("hide");
                	    document.querySelector(".button-logout").classList.add("hide");
                	    
                	    document.querySelector(".user-container").classList.remove("connected");

                	    document.querySelectorAll(".sign-button").forEach(function(button) {
                	        button.classList.remove("hide");
                	    });
                	}
                    resolve();
                } else {
                    reject(new Error("Request failed with status: " + xhr.status));
                }
            }
        };
        xhr.send(new URLSearchParams(formData));
    });
}

function getCoursGroupesAjax() {
    return new Promise((resolve, reject) => {
    	var schedule = document.querySelector(".schedule-table");
    	var cours = schedule.querySelectorAll(".case_cours");
    	cours.forEach(function(element) {
            element.remove();
        });
    	
    	var form = document.getElementById("getCoursGroupes");
        var formData = new FormData(form);

        formData.append("op", "getCoursGroupes");
        var lun = document.querySelector(".monday-button.clicked").innerHTML;
        lun = lun.replace(/\//g, ",");
        formData.append("lundi", lun);
        var edtCodes = document.getElementById("edtCodes").innerHTML;
        formData.append("edt", edtCodes);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "Serv", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var res = xhr.responseText;
                    if (res.startsWith("success")) {
                    	
                    	var ress = res.split(":");
                        var coursS = ress[1].split(";");
                        
                        for (let i = 0; i < coursS.length-1; i++) {
                        	var cours = coursS[i].split(",");
                        	
                        	var couleur = "rgba("+cours[0]+","+cours[1]+","+cours[2]+",0.5)";
                            var matiere = cours[3];
                            var type = cours[4];
                            	var sallesSTR = cours[5].split("#");
                            var salles = [];
                            for(let j = 0; j < sallesSTR.length-1; j++){
                            	salles.push(sallesSTR[j]);
                            }
                            
                            	var groupesSTR = cours[6].split("#");
                            var groupes = [];
                            for(let j = 0; j < groupesSTR.length-1; j++){
                            	groupes.push(groupesSTR[j]);
                            }
                            
                            	var profsSTR = cours[7].split("#");
                            var profs = [];
                            for(let j = 0; j < profsSTR.length-1; j++){
                            	profs.push(profsSTR[j]);
                            }
                            var horaire = [[parseInt(cours[8]), parseInt(cours[9])], [parseInt(cours[10]), parseInt(cours[11])]];
                            var jour = parseInt(cours[12]) - 1;
                            
                            place_cours(couleur, matiere, type, salles, groupes, profs, horaire, jour);
                        }
                        
                    } /*else {
                        reject(new Error(res));
                    }*/
                    resolve();
                } else {
                    reject(new Error("Request failed with status: " + xhr.status));
                }
            }
        };
        xhr.send(new URLSearchParams(formData));
    });
}

async function updateSchedule() {
    try {
        await getWeekAjax();
        console.log("getWeekAjax OK");
        await getCoursGroupesAjax();
        console.log("getCoursGroupesAjax OK");
    } catch (error) {
        console.error("An error occurred:", error);
    }
}





function submitFormAjax(formID) {
	if (validateForm(formID)){
		var form = document.getElementById(formID);
	
	    var formData = new FormData(form); // Serialize form data
	
	    var lun = document.querySelector(".monday-button.clicked").innerHTML;
        lun = lun.replace(/\//g, ",");
        formData.append("lundi", lun);
        var edtCodes = document.getElementById("edtCodes").innerHTML;
        formData.append("edt", edtCodes);
	    
	    var xhr = new XMLHttpRequest();
	    xhr.open("POST", "Serv", true);
	    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"); // Set content type
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState === XMLHttpRequest.DONE) {
	            if (xhr.status === 200) {
	            	var res = xhr.responseText;
	            	if (res.startsWith("success")) {
	            		if (formID == "addCours"){
		                    async function updateCours() {
		            		    try {
		            		        await getCoursGroupesAjax();
		            		        console.log("getCoursGroupesAjax OK");
		            		    } catch (error) {
		            		        console.error("An error occurred:", error);
		            		    }
		            		}
		            		
		            		updateCours();
	            		}
	            		
	            		
	            		contentAdded();
	            		
	            		popUpContainer.classList.toggle("show-flex");
	            		popUp.innerHTML = "";
	            		
	            		
	            		
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

function buildDropDownAjax(containerID, descAToAdd, GETInfos) {
    return new Promise((resolve, reject) => {
        var formData = new FormData();
    	formData.append("op", GETInfos);
        // Append key-value pairs to the FormData object
        formData.append("edt", document.getElementById("edtCodes").innerHTML);

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "Serv", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // Request succeeded
                	var res = xhr.responseText;
                	if (res.startsWith("success")){
                		if(res != ("success:")){
	                		var ress = res.split(":");
		                	var elems = ress[1].split(",");
		                    var container = document.getElementById(containerID);
		                    for (var i = 0; i < elems.length; i++) {
		                    	if (!container.innerHTML.includes(elems[i] + "</a>")) {
		                    	    container.innerHTML += descAToAdd + elems[i] + "</a>";
		                    	}
		    					
		    				}
                		}
                		
                	}else{
                		console.error(res);
                	}
                	resolve();
                	
                } else {
                    // Request failed
                	reject(new Error("Request failed with status: " + xhr.status));
                }
            }
        };
        xhr.send(new URLSearchParams(formData)); // Send the FormData object
    });
}


function toggleDropdown(dropdownElement) {
    var dropdown = document.getElementById(dropdownElement);
    dropdown.classList.toggle("show");
}

async function listGroups() {
    try {
      //groupes
        await buildDropDownAjax("dropdown-div6", `<a onclick="toggleClicked(this, 'groups-schedule-view')" class="choice-element">`, "getGroupesEDT");
        console.log("getGroupesEDT OK");
    } catch (error) {
        console.error("An error occurred:", error);
    }
}



function toggleDropdownSendForm(dropdownElement) {
	listGroups();
	
    var dropdown = document.getElementById(dropdownElement);
    if (!dropdown.classList.contains("show")){
        dropdown.classList.add("show");
    }else{
        dropdown.classList.remove('show');
        var form = document.getElementById("getScheduleGroupForm");
        getCoursGroupesAjax();
    }
    
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.drop-button')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
    if (!event.target.matches('.choice-button')) {
        var dropdowns = document.getElementsByClassName("choice-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            // Check if the dropdown is open and the click is not on the button or within the dropdown
            if (openDropdown.classList.contains('show') && 
                !openDropdown.contains(event.target) && 
                !event.target.matches('.choice-button')) {
                openDropdown.classList.remove('show');
            }
        }
    }

    if (!event.target.matches('.view-button')) {
        var dropdowns = document.getElementsByClassName("view-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            // Check if the dropdown is open and the click is not on the button or within the dropdown
            if (openDropdown.classList.contains('show') && 
                !openDropdown.contains(event.target) && 
                !event.target.matches('.view-button')) {
                openDropdown.classList.remove('show');
                var form = document.getElementById("getScheduleGroupForm");
                getCoursGroupesAjax();
            }
        }
    }
    
}


function validateForm(popupInfo) {
    switch (popupInfo) {

        case 'addCours':
                //var csvFile = document.forms["addCours"]["file-cours"].value;
                var type = document.forms["addCours"]["type-cours"].value;
                var matiere = document.forms["addCours"]["matiere-cours"].value;
                var salles = document.forms["addCours"]["salles-cours"].value;
                var profs = document.forms["addCours"]["profs-cours"].value;
                var groupes = document.forms["addCours"]["groupes-cours"].value;

                if (type == "" || matiere == "" || salles == "" || profs == "" || groupes == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;

        case 'addEtudiant':
        	//var csvFile = document.forms["addEtudiant"]["file-link"].value;
                var number = document.forms["addEtudiant"]["num-link"].value;

                if (number == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;

        case 'addProfesseur':
        	//var csvFile = document.forms["addProfesseur"]["file-link"].value;
                var number = document.forms["addProfesseur"]["num-link"].value;
                var name = document.forms["addProfesseur"]["nom-link"].value;
                var firstname = document.forms["addProfesseur"]["prenom-link"].value;

                if (number == "" || name == "" || firstname == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;

        case 'addGroupe':
        	//var csvFile = document.forms["addGroupe"]["file-groupe"].value;
                var name = document.forms["addGroupe"]["nom-groupe"].value;

                if (name == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;

        case 'addSalle':
        	//var csvFile = document.forms["addSalle"]["file-salle"].value;
                var name = document.forms["addSalle"]["nom-salle"].value;

                if (name == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;

        

        case 'addMatiere':
        	//var csvFile = document.forms["addMatiere"]["file-matiere"].value;
                var name = document.forms["addMatiere"]["nom-matiere"].value;

                if (name == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;

        case 'addType':
        	//var csvFile = document.forms["addType"]["file-type"].value;
                var name = document.forms["addType"]["nom-type"].value;

                if (name == "") {
                    var errorMessage = document.querySelector(".error-message");
                    errorMessage.innerHTML = "Champs obligatoires";

                    return false;
                }
            break;
    
    }
    
    
    return true;
}


function removeFirstOccurrence(mainString, subString) {
    var index = mainString.indexOf(subString);
    if (index !== -1) {
        return mainString.slice(0, index) + mainString.slice(index + subString.length);
    } else {
        return mainString;
    }
}

function toggleClicked(self, inputToModifyName) {
    var inputList = document.getElementById(inputToModifyName)
    if (self.classList.contains("clicked")){
        self.classList.remove("clicked");
        var result = removeFirstOccurrence(inputList.value, ","+self.innerHTML);
        if (result != inputList.value){
            inputList.value = result;
        }else{
            inputList.value = removeFirstOccurrence(inputList.value, self.innerHTML);
        }
        
        if (inputList.value.startsWith(",")){
            inputList.value = inputList.value.slice(1);
        }
        
    }else{
        self.classList.add("clicked");
        if (inputList.value != ""){
            inputList.value += "," + self.innerHTML;
        }else{
            inputList.value += self.innerHTML;
        }
        
    }
    
}

function toggleClickedOne(self, inputToModifyName, groupId) {
    var input = document.getElementById(inputToModifyName)
    self.classList
    
    var choiceElements = document.querySelectorAll(".single-choice"+groupId);
    choiceElements.forEach(elem => {
        if (elem.classList.contains("clicked")){
            elem.classList.remove("clicked");
            input.value = "";
        }
    });
    if (self.classList.contains("clicked")){
        self.classList.remove("clicked");
        input.value = "";
    }else{
        self.classList.add("clicked");
        input.value = self.innerHTML;
    }
    
}

function toggleClickedOneAlways(self) {
	var clickedOne = document.querySelector(".monday-button.clicked");
	if (self != clickedOne){
		clickedOne.classList.remove("clicked");
		self.classList.add("clicked");
		updateSchedule();
	}
    
}

function increaseNumber(idInput, min, max) {
    var input = document.getElementById(idInput);
    var currentValue = parseInt(input.value);
    input.value = ((currentValue-min) + 1)%(max-min+1) + min;
}
  
function decreaseNumber(idInput, min, max) {
    var input = document.getElementById(idInput);
    var currentValue = parseInt(input.value);
    if (currentValue > min) {
        input.value = currentValue - 1;
    }else {
        input.value = max;
    }
}



var popUpContainer = document.querySelector(".popup-container");
var popUp = document.querySelector(".popup");

function toggleShow(self) {
    var popupInfo = self.innerHTML;
    var togglePopUp = false;
	switch (popupInfo) {
        case 'Ajouter Cours':
            popupInfo = "Cours";
            var currentDate = new Date();
            var day = currentDate.getDate();
            var month = currentDate.getMonth() + 1;
            var year = currentDate.getFullYear();
            popUp.innerHTML = `
                    <div class="header-line">
                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
                        <button onclick="toggleShow(this)" class="button-exit">X</button>
                    </div>
                    <form id="addCours" name="addCours" method="post" action="Serv">
                        <table class="popup-table">
                        
                            <tr>
                                <td><label for="num-link">Horaire:</label></td>
                                <td>
                                <div class="div-line">
                                    <div class="div-column">
                                    <button type="button" onclick="decreaseNumber('heure-cours-debut', 8, 20)" class="time-button">-</button>
                                    <input type="text" id="heure-cours-debut" name="heure-cours-debut" class="time-input" value="8">
                                    <button type="button" onclick="increaseNumber('heure-cours-debut', 8, 20)" class="time-button">+</button>
                                    </div>
                                    <label>:</label>
                                    <div class="div-column">
                                    <button type="button" onclick="decreaseNumber('minute-cours-debut', 0, 59)" class="time-button">-</button>
                                    <input type="text" id="minute-cours-debut" name="minute-cours-debut" class="time-input" value="0">
                                    <button type="button" onclick="increaseNumber('minute-cours-debut', 0, 59)" class="time-button">+</button>
                                    </div>
                                    <label>-</label>
                                    <div class="div-column">
                                    <button type="button" onclick="decreaseNumber('heure-cours-fin', 8, 20)" class="time-button">-</button>
                                    <input type="text" id="heure-cours-fin" name="heure-cours-fin" class="time-input" value="9">
                                    <button type="button" onclick="increaseNumber('heure-cours-fin', 8, 20)" class="time-button">+</button>
                                    </div>
                                    <label>:</label>
                                    <div class="div-column">
                                    <button type="button" onclick="decreaseNumber('minute-cours-fin', 0, 59)" class="time-button">-</button>
                                    <input type="text" id="minute-cours-fin" name="minute-cours-fin" class="time-input" value="0">
                                    <button type="button" onclick="increaseNumber('minute-cours-fin', 0, 59)" class="time-button">+</button>
                                    </div>
                                </div>
                                </td>
                            </tr>

                            <tr>
                            <td><label for="num-link">Date:</label></td>
                            <td>
                            <div class="div-line">
                                <div class="div-column">
                                <button type="button" onclick="decreaseNumber('jour-cours', 1, 31)" class="time-button">-</button>
                                <input type="text" id="jour-cours" name="jour-cours" class="time-input" value="${day}">
                                <button type="button" onclick="increaseNumber('jour-cours', 1, 31)" class="time-button">+</button>
                                </div>
                                <label>/</label>
                                <div class="div-column">
                                <button type="button" onclick="decreaseNumber('mois-cours', 1, 12)" class="time-button">-</button>
                                <input type="text" id="mois-cours" name="mois-cours" class="time-input" value="${month}">
                                <button type="button" onclick="increaseNumber('mois-cours', 1, 12)" class="time-button">+</button>
                                </div>
                                <label>/</label>
                                <div class="div-column">
                                <button type="button" onclick="decreaseNumber('annee-cours', 1, 9999)" class="time-button">-</button>
                                <input type="text" id="annee-cours" name="annee-cours" class="time-input year-input" value="${year}">
                                <button type="button" onclick="increaseNumber('annee-cours', 1, 9999)" class="time-button">+</button>
                                </div>
                            </div>
                            </td>
                            </tr>

                            <tr>
                                <td><div class="dropdown">
                                    <button type="button" onclick="toggleDropdown('dropdown-div1')" class="add-button choice-button">Type</button>
                                    <div id="dropdown-div1" class="choice-content">`+
                                    /*<a onclick="toggleClickedOne(this, 'type-cours', 0)" class="choice-element single-choice0">Cours</a>
                                    <a onclick="toggleClickedOne(this, 'type-cours', 0)" class="choice-element single-choice0">TD</a>
                                    <a onclick="toggleClickedOne(this, 'type-cours', 0)" class="choice-element single-choice0">TP</a>
                                    <a onclick="toggleClickedOne(this, 'type-cours', 0)" class="choice-element single-choice0">Cours-TD</a>
                                    <a onclick="toggleClickedOne(this, 'type-cours', 0)" class="choice-element single-choice0">Cours-TP</a>*/
                                
                                    `</div>
                                </div></td>
                                <td><input type="text" id="type-cours" name="type-cours"><br></td>
                            </tr>

                            <tr>
                                <td><div class="dropdown">
                                    <button type="button" onclick="toggleDropdown('dropdown-div2')" class="add-button choice-button">Matière</button>
                                    <div id="dropdown-div2" class="choice-content">`+
                                    /*<a onclick="toggleClickedOne(this, 'matiere-cours', 1)" class="choice-element single-choice1">Rendu</a>
                                    <a onclick="toggleClickedOne(this, 'matiere-cours', 1)" class="choice-element single-choice1">TAV</a>
                                    <a onclick="toggleClickedOne(this, 'matiere-cours', 1)" class="choice-element single-choice1">Web</a>
                                    <a onclick="toggleClickedOne(this, 'matiere-cours', 1)" class="choice-element single-choice1">Programmation Mobile</a>
                                    <a onclick="toggleClickedOne(this, 'matiere-cours', 1)" class="choice-element single-choice1">Base de données</a>*/
                                
                                    `</div>
                                </div></td>
                                <td><input type="text" id="matiere-cours" name="matiere-cours"><br></td>
                            </tr>

                            <tr>
                                <td><div class="dropdown">
                                    <button type="button" onclick="toggleDropdown('dropdown-div3')" class="add-button choice-button">Salles</button>
                                    <div id="dropdown-div3" class="choice-content">`+
                                    /*<a onclick="toggleClicked(this, 'salles-cours')" class="choice-element">B00</a>
                                    <a onclick="toggleClicked(this, 'salles-cours')" class="choice-element">A202</a>
                                    <a onclick="toggleClicked(this, 'salles-cours')" class="choice-element">C301</a>
                                    <a onclick="toggleClicked(this, 'salles-cours')" class="choice-element">A101</a>
                                    <a onclick="toggleClicked(this, 'salles-cours')" class="choice-element">B306</a>*/
                                  
                                    `</div>
                                </div></td>
                                <td><input type="text" id="salles-cours" name="salles-cours"><br></td>
                            </tr>

                            <tr>
                                <td><div class="dropdown">
                                    <button type="button" onclick="toggleDropdown('dropdown-div4')" class="add-button choice-button">Professeurs</button>
                                    <div id="dropdown-div4" class="choice-content">`+
                                    /*<a onclick="toggleClicked(this, 'profs-cours')" class="choice-element">Daniel HAGIMONT</a>
                                    <a onclick="toggleClicked(this, 'profs-cours')" class="choice-element">Pierre ALIBERT</a>
                                    <a onclick="toggleClicked(this, 'profs-cours')" class="choice-element">Nikola DABIC</a>
                                    <a onclick="toggleClicked(this, 'profs-cours')" class="choice-element">Arthur CLODION</a>
                                    <a onclick="toggleClicked(this, 'profs-cours')" class="choice-element">Géraldine Morin</a>*/
                                  
                                    `</div>
                                </div></td>
                                <td><input type="text" id="profs-cours" name="profs-cours"><br></td>
                            </tr>

                            <tr>
                                <td><div class="dropdown">
                                    <button type="button" onclick="toggleDropdown('dropdown-div5')" class="add-button choice-button">Groupes</button>
                                    <div id="dropdown-div5" class="choice-content">`+
                                    /*<a onclick="toggleClicked(this, 'groupes-cours')" class="choice-element">SN</a>
                                    <a onclick="toggleClicked(this, 'groupes-cours')" class="choice-element">3EA</a>
                                    <a onclick="toggleClicked(this, 'groupes-cours')" class="choice-element">MFEE</a>
                                    <a onclick="toggleClicked(this, 'groupes-cours')" class="choice-element">M1</a>
                                    <a onclick="toggleClicked(this, 'groupes-cours')" class="choice-element">M2</a>*/
                                  
                                    `</div>
                                </div></td>
                                <td><input type="text" id="groupes-cours" name="groupes-cours"><br></td>
                            </tr>

                            <tr>
                                <td><label for="nom-link">Infos:</label></td>
                                <td><input type="text" id="infosupp-cours" class="optional-input" name="infosupp-link" value="Facultatif"><br></td>
                            </tr>`+
                            
                            /*<tr>
                                <td><label>OU</label></td>
                            </tr>
                            <tr>
                                <td><label for="file-cours0">Liste des cours:</label></td>
                                <td><label for="file-cours" class="button-csv" title="ligne = type, matière, (salle1[, salle2,...]), (prof1[,prof2,...]), (groupe1[,groupe2,...])">Fichier CSV</label></td>
                                <td><input type="file" id="file-cours" name="file-cours" accept=".csv" class="input-csv hide"><br></td>
                            </tr>*/
                        `</table>
                        <div class="center-line">
                            <button type="button" class="submit-button" onclick="submitFormAjax('addCours')">Ajouter</button>
                        </div>
                        <input type="hidden" name="op" value="addCours">
                    </form>
                    <div class="error-message"></div>
                `;
            
            
            async function createLists() {
                try {
                	//types
                    await buildDropDownAjax("dropdown-div1", `<a onclick="toggleClickedOne(this, 'type-cours', 0)" class="choice-element single-choice0">`, "getTypesEDT");
                    console.log("getTypesEDT OK");
                    //matieres
                    await buildDropDownAjax("dropdown-div2", `<a onclick="toggleClickedOne(this, 'matiere-cours', 1)" class="choice-element single-choice1">`, "getMatieresEDT");
                    console.log("getMatieresEDT OK");
                    //salles
                    await buildDropDownAjax("dropdown-div3", `<a onclick="toggleClicked(this, 'salles-cours')" class="choice-element">`, "getSallesEDT");
                    console.log("getSallesEDT OK");
                    //profs
                    await buildDropDownAjax("dropdown-div4", `<a onclick="toggleClicked(this, 'profs-cours')" class="choice-element">`, "getProfsEDT");
                    console.log("getProfsEDT OK");
                  //groupes
                    await buildDropDownAjax("dropdown-div5", `<a onclick="toggleClicked(this, 'groupes-cours')" class="choice-element">`, "getGroupesEDT");
                    console.log("getGroupesEDT OK");
                } catch (error) {
                    console.error("An error occurred:", error);
                }
            }
            
            createLists();
            togglePopUp = true;
            //submitFormAjax('addCours');
                
            var optionalInputs = document.querySelectorAll(".optional-input");
            optionalInputs.forEach(input => {
                input.style.color = "var(--color-4)";
                input.style.backgroundColor = "var(--color-3)";
                input.addEventListener("blur", function() {
                    if (input.value === "") {
                        input.value = "Facultatif";
                        input.style.color = "var(--color-4)";
                        input.style.backgroundColor = "var(--color-3)";
                    }
                });
            
                input.addEventListener("focus", function(){
                    if (input.value === "Facultatif") {
                        input.value = "";
                        input.style.color = "var(--color-1)";
                        input.style.backgroundColor = "var(--color-4)";
                    }
                });
            });
            var heuresDebut = document.getElementById("heure-cours-debut");
            heuresDebut.addEventListener("blur", function() {
                var res = heuresDebut.value;
                if (isNaN(parseInt(res)) || 8 > res || res > 20) {
                    heuresDebut.value = 8;
                }
            });
            var minutesDebut = document.getElementById("minute-cours-debut");
            minutesDebut.addEventListener("blur", function() {
                var res = minutesDebut.value;
                if (isNaN(parseInt(res)) || 0 > res || res > 59) {
                    minutesDebut.value = 0;
                }
            });
            var heuresFin = document.getElementById("heure-cours-fin");
            heuresFin.addEventListener("blur", function() {
                var res = heuresFin.value;
                if (isNaN(parseInt(res)) || 8 > res || res > 20) {
                    heuresFin.value = 9;
                }
            });
            var minutesFin = document.getElementById("minute-cours-fin");
            minutesFin.addEventListener("blur", function() {
                var res = minutesFin.value;
                if (isNaN(parseInt(res)) || 0 > res || res > 59) {
                    minutesFin.value = 0;
                }
            });
            var jours = document.getElementById("jour-cours");
            jours.addEventListener("blur", function() {
                var res = jours.value;
                if (isNaN(parseInt(res)) || 1 > res || res > 31) {
                    jours.value = day;
                }
            });
            var mois = document.getElementById("mois-cours");
            mois.addEventListener("blur", function() {
                var res = mois.value;
                if (isNaN(parseInt(res)) || 1 > res || res > 12) {
                    mois.value = month;
                }
            });
            var annees = document.getElementById("annee-cours");
            annees.addEventListener("blur", function() {
                var res = annees.value;
                if (isNaN(parseInt(res)) || 1 > res || res > 9999) {
                    annees.value = year;
                }
            });
            break;

            
        case 'Étudiant':
            popUp.innerHTML = `
                    <div class="header-line">
                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
                        <button onclick="toggleShow(this)" class="button-exit">X</button>
                    </div>
                    <form id="addEtudiant" name="addEtudiant" method="post" action="Serv">
                        <table class="popup-table">
                            <tr>
                                <td><label for="num-link">Numéro:</label></td>
                                <td><input type="text" id="num-link" name="num-link"><br></td>
                            </tr>
                            <tr>
                                <td><label for="prenom-link">Prénom:</label></td>
                                <td><input type="text" id="prenom-link" class="optional-input" name="prenom-link" value="Facultatif"><br></td>
                            </tr>
                            <tr>
                                <td><label for="nom-link">Nom:</label></td>
                                <td><input type="text" id="nom-link" class="optional-input" name="nom-link" value="Facultatif"><br></td>
                            </tr>`+
                            
                            /*<tr>
                                <td><label for="file-link0">Liste des groupes:</label></td>
                                <td><label for="file-link" class="button-csv" title="ligne = numéro[, prénom, nom]">Fichier CSV</label></td>
                                <td><input type="file" id="file-link" name="file-link" accept=".csv" class="input-csv hide"><br></td>
                            </tr>*/
                            `<tr>
                                <td><div class="dropdown">
                                    <button type="button" onclick="toggleDropdown('dropdown-div5')" class="add-button choice-button">Groupes</button>
                                    <div id="dropdown-div5" class="choice-content">`+
                                    
                                    /*<a onclick="toggleClicked(this, 'groupes-link')" class="choice-element">SN</a>
                                    <a onclick="toggleClicked(this, 'groupes-link')" class="choice-element">3EA</a>
                                    <a onclick="toggleClicked(this, 'groupes-link')" class="choice-element">MFEE</a>
                                    <a onclick="toggleClicked(this, 'groupes-link')" class="choice-element">M1</a>
                                    <a onclick="toggleClicked(this, 'groupes-link')" class="choice-element">M2</a>*/
                                  
                                    `</div>
                                </div></td>
                                <td><input type="text" id="groupes-link" name="groupes-link"><br></td>
                            </tr>
                        
                        </table>
                        <div class="center-line">
                            <button type="button" class="submit-button" onclick="submitFormAjax('addEtudiant')">Ajouter</button>
                        </div>
                        <input type="hidden" name="op" value="addEtudiant">
                    </form>
                    <div class="error-message"></div>
                `;

            
            async function createList() {
                try {
                  //groupes
                    await buildDropDownAjax("dropdown-div5", `<a onclick="toggleClicked(this, 'groupes-link')" class="choice-element">`, "getGroupesEDT");
                    console.log("getGroupesEDT OK");
                } catch (error) {
                    console.error("An error occurred:", error);
                }
            }
            
            createList();
            
            togglePopUp = true;
            var optionalInputs = document.querySelectorAll(".optional-input");
            optionalInputs.forEach(input => {
                input.style.color = "var(--color-4)";
                input.style.backgroundColor = "var(--color-3)";
                input.addEventListener("blur", function() {
                    if (input.value === "") {
                        input.value = "Facultatif";
                        input.style.color = "var(--color-4)";
                        input.style.backgroundColor = "var(--color-3)";
                    }
                });
            
                input.addEventListener("focus", function(){
                    if (input.value === "Facultatif") {
                        input.value = "";
                        input.style.color = "var(--color-1)";
                        input.style.backgroundColor = "var(--color-4)";
                    }
                });
            });
            break;

            case 'Professeur':
                popUp.innerHTML = `
                        <div class="header-line">
                            <h2 class="title-${popupInfo}">${popupInfo}</h2>
                            <button onclick="toggleShow(this)" class="button-exit">X</button>
                        </div>
                        <form id="addProfesseur" name="addProfesseur" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
                            <table class="popup-table">
                                <tr>
                                    <td><label for="num-link">Numéro:</label></td>
                                    <td><input type="text" id="num-link" name="num-link"><br></td>
                                </tr>
                                <tr>
                                    <td><label for="prenom-link">Prénom:</label></td>
                                    <td><input type="text" id="prenom-link" name="prenom-link"><br></td>
                                </tr>
                                <tr>
                                    <td><label for="nom-link">Nom:</label></td>
                                    <td><input type="text" id="nom-link" name="nom-link"><br></td>
                                </tr>`+
                            
                                /*<tr>
                                    <td><label for="file-link0">Liste des professeurs:</label></td>
                                    <td><label for="file-link" class="button-csv" title="ligne = numéro, prénom, nom">Fichier CSV</label></td>
                                    <td><input type="file" id="file-link" name="file-link" accept=".csv" class="input-csv hide"><br></td>
                                </tr>*/
                            `</table>
                            <div class="center-line">
                            	<button type="button" class="submit-button" onclick="submitFormAjax('addProfesseur')">Ajouter</button>
                            </div>
                            <input type="hidden" name="op" value="addProfesseur">
                        </form>
                        <div class="error-message"></div>
                    `;
    
                
                togglePopUp = true;
                break;
        
        case 'Groupe':
            popUp.innerHTML = `
                    <div class="header-line">
                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
                        <button onclick="toggleShow(this)" class="button-exit">X</button>
                    </div>
                    <form id="addGroupe" name="addGroupe" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
                        <table class="popup-table">
                            <tr>
                                <td><label for="nom-groupe">Nom:</label></td>
                                <td><input type="text" id="nom-groupe" name="nom-groupe"><br></td>
                            </tr>`+
                            
                            /*<tr>
                                <td><label for="file-groupe0">Liste des étudiants:</label></td>
                                <td><label for="file-groupe" class="button-csv" title="ligne = numéro[, prénom, nom]">Fichier CSV</label></td>
                                <td><input type="file" id="file-groupe" name="file-groupe" accept=".csv" class="input-csv hide"><br></td>
                            </tr>*/
                        `</table>
                        <div class="center-line">
                            <button type="button" class="submit-button" onclick="submitFormAjax('addGroupe')">Ajouter</button>
                        </div>
                        <input type="hidden" name="op" value="addGroupe">
                    </form>
                    <div class="error-message"></div>
                `;

            
            togglePopUp = true;
            break;

        case 'Matière':
            popUp.innerHTML = `
                    <div class="header-line">
                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
                        <button onclick="toggleShow(this)" class="button-exit">X</button>
                    </div>
                    <form id="addMatiere" name="addMatiere" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
                        <table class="popup-table">`+
                            
                            /*<tr>
                                <td><label for="file-matiere0">Liste des matières:</label></td>
                                <td><label for="file-matiere" class="button-csv" title="ligne = nom">Fichier CSV</label></td>
                                <td><input type="file" id="file-matiere" name="file-matiere" accept=".csv" class="input-csv hide"><br></td>
                            </tr>
                            <tr>
                                <td><label>OU</label></td>
                            </tr>*/
                            `<tr>
                                <td><label for="nom-matiere">Nom:</label></td>
                                <td><input type="text" id="nom-matiere" name="nom-matiere"><br></td>
                            </tr>
                        </table>
                        <div class="center-line">
                            <button type="button" class="submit-button" onclick="submitFormAjax('addMatiere')">Ajouter</button>
                        </div>
                        <input type="hidden" name="op" value="addMatiere">
                    </form>
                    <div class="error-message"></div>
                `;


            togglePopUp = true;
            break;

        case 'Salle':
            popUp.innerHTML = `
                    <div class="header-line">
                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
                        <button onclick="toggleShow(this)" class="button-exit">X</button>
                    </div>
                    <form id="addSalle" name="addSalle" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
                        <table class="popup-table">`+
                            
                            /*<tr>
                                <td><label for="file-salle0">Liste des salles:</label></td>
                                <td><label for="file-salle" class="button-csv" title="ligne = nom">Fichier CSV</label></td>
                                <td><input type="file" id="file-salle" name="file-salle" accept=".csv" class="input-csv hide"><br></td>
                            </tr>
                            <tr>
                                <td><label>OU</label></td>
                            </tr>*/
                            `<tr>
                                <td><label for="nom-salle">Nom:</label></td>
                                <td><input type="text" id="nom-salle" name="nom-salle"><br></td>
                            </tr>
                        </table>
                        <div class="center-line">
                            <button type="button" class="submit-button" onclick="submitFormAjax('addSalle')">Ajouter</button>
                        </div>
                        <input type="hidden" name="op" value="addSalle">
                    </form>
                    <div class="error-message"></div>
                `;


            togglePopUp = true;
            break;

            case 'Type':
                popUp.innerHTML = `
                    <div class="header-line">
                        <h2 class="title-${popupInfo}">${popupInfo}</h2>
                        <button onclick="toggleShow(this)" class="button-exit">X</button>
                    </div>
                    <form id="addType" name="addType" method="post" action="Serv" onsubmit="return validateForm('${popupInfo}')">
                        <table class="popup-table">`+
                            
                            /*<tr>
                                <td><label for="file-type0">Liste des types:</label></td>
                                <td><label for="file-type" class="button-csv" title="ligne = nom">Fichier CSV</label></td>
                                <td><input type="file" id="file-type" name="file-type" accept=".csv" class="input-csv hide"><br></td>
                            </tr>
                            <tr>
                                <td><label>OU</label></td>
                            </tr>*/
                            `<tr>
                                <td><label for="nom-type">Nom:</label></td>
                                <td><input type="text" id="nom-type" name="nom-type"><br></td>
                            </tr>
                        </table>
                        <div class="center-line">
                            <button type="button" class="submit-button" onclick="submitFormAjax('addType')">Ajouter</button>
                        </div>
                        <input type="hidden" name="op" value="addType">
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
        /*document.querySelector('.button-csv').addEventListener('click', function(event) {
            event.preventDefault();
            document.querySelector('.input-csv').click();
        });
        document.querySelector('.input-csv').addEventListener('change', function(event) {
            var fileName = event.target.files[0].name;
            document.querySelector('.button-csv').textContent = fileName;
        });*/
          
	    popUpContainer.classList.toggle("show-flex");
    }
        
	
}

