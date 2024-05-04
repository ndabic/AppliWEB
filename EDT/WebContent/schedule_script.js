const delay = ms => new Promise(res => setTimeout(res, ms));
let waitingTime = 30;

document.addEventListener("DOMContentLoaded", function() {
    const days = ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"];

    const scheduleTable = document.getElementById("schedule");

    // Create the table header
    let tableHTML = "<table class='schedule-table'>";
    tableHTML += "<tr class='weekDays'>";
    days.forEach(day => {
        tableHTML += `<th>${day}</th>`;
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
function place_cours(couleur, matiere, type, salle, groupes, prof, horaire, jour) {
    
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

                singleCase.innerHTML = "<p><span class='nom_cours'>"+type+" - "+matiere+"</span></p>"+
                                        "<p><span class='info_cours'>Salle: "+salle+"</span></p>"+
                                        "<p><span class='info_cours'>"+prof[0]+" "+prof[1]+"</span></p>"+
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