const delay = ms => new Promise(res => setTimeout(res, ms));
let waitingTime = 30;

const days = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

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

function getCoursSemaineAjax() {
    return new Promise((resolve, reject) => {
        var formData = new FormData();

        formData.append("op", "getCoursSemaine");
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
                        var coursS = ress[1].split(";");
                        
                        for (let i = 0; i < coursS.length-1; i++) {
                        	var cours = coursS[i].split(",");
                        	
                            var matiere = cours[0];
                            var type = cours[1];
                            	var sallesSTR = cours[2].split("#");
                            var salles = [];
                            for(let j = 0; j < sallesSTR.lenght-1; j++){
                            	salles.push(sallesSTR[j]);
                            }
                            
                            	var groupesSTR = cours[3].split("#");
                            var groupes = [];
                            for(let j = 0; j < groupesSTR.lenght-1; j++){
                            	groupes.push(groupesSTR[j]);
                            }
                            
                            	var profsSTR = cours[4].split("#");
                            var profs = [];
                            for(let j = 0; j < profsSTR.lenght-1; j++){
                            	profs.push(profsSTR[j]);
                            }
                            var horaire = [[cours[5], cours[6]], [cours[7], cours[8]]];
                            var jour = cours[9] - 1;
                            
                            place_cours("rgba(255, 45, 100, 0.5)", matiere, type, salles, groupes, profs, horaire, jour);
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
                            	mondays.innerHTML += `<button class="monday-button clicked" onclick="toggleClickedOne(this)">${day}</button>`;
                            	buttonToScrollTo = document.querySelector(".monday-button.clicked");
                            }else{
                            	mondays.innerHTML += `<button class="monday-button" onclick="toggleClickedOne(this)">${day}</button>`;
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
                        document.querySelector(".user-container").classList.add("connected");

                        document.querySelectorAll(".sign-button").forEach(function (button) {
                            button.classList.add("hide");
                        });
                    }else {
                		console.error("cookies différents:", res);
                	    document.querySelector(".user-profile").classList.add("hide");
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

async function updateSchedule() {
    try {
        await getWeekAjax();
        console.log("getWeekAjax OK");
        await getCoursSemaineAjax();
        console.log("getCoursSemaineAjax OK");
    } catch (error) {
        console.error("An error occurred:", error);
    }
}

function toggleClickedOne(self) {
	var clickedOne = document.querySelector(".monday-button.clicked");
	if (self != clickedOne){
		clickedOne.classList.remove("clicked");
		self.classList.add("clicked");
		updateSchedule();
	}
    
}






document.addEventListener("DOMContentLoaded", function() {
	
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
            await getCoursSemaineAjax();
            console.log("getCoursSemaineAjax OK");
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
    //place_cours("rgba(255, 45, 100, 0.5)", "WEB", "TP", "A002", ["M1", "M2"], ["Daniel HAGIMONT"], [[8, 0],[15,45]], 2);
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
                    groupesHTML += groupe
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
                    sallesHTML += salle
                    if (index2 + 1 < salles.length)
                    	sallesHTML += ", ";

                }
                sallesHTML += "</span></p>";
                
                let profsHTML = "<p><span class='info_cours'>";
                for (let index2 = 0; index2 < profs.length; index2++) {
                    const prof = profs[index2];
                    profsHTML += prof
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



