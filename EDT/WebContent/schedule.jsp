<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pack.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Emploi Du Temps</title>
    <link rel="stylesheet" type="text/css" href="schedule_style.css">
	<link rel="icon" type="image/png" href="./assets/Logo.png">
</head>
<body>

	<div class="header">
		<h1>&#160;Emploi Du Temps&#160;</h1>
		
		<div class="user-container">

			<div class="user-profile " onclick="location.href='profile.html'"></div>
		
			<button class="sign-button button-color-1 hide" onclick="location.href='connexion.html'">
				Connexion
			</button>

			<button class="sign-button button-color-2 hide" onclick="location.href='inscription.html'">
				Inscription
			</button>

		</div>
	</div>
	
	<div class="line-container">
		<div class="mondays-container">
		</div>
	</div>
	
	
	<div class="schedule-container">
	    <div id="schedule"></div>
	    <div id="cours_container"></div>
	</div>
	
	
	
	<form action="Serv" method="get">
		<% 
		if (request.getAttribute("lCours") != null){
			Collection<Cours> lCours = (Collection<Cours>) request.getAttribute("lCours");
			for(Cours c: lCours){
				
				String couleur = c.getMatiere().getCouleur();
				String matiere = c.getMatiere().getNom();
				String type = c.getType().getNom();
				

				String salles = "";
				Collection<Salle> sallesCours = c.getSalle();
				for (Salle salle : sallesCours){
					salles.concat(salle.getNom()+",");
				}
				salles.substring(0, salles.length()-1);
				
				String groupes = "";
				Collection<Groupe> groupesCours = c.getGroupes();
				for (Groupe groupe : groupesCours){
					groupes.concat(groupe.getNom()+",");
				}
				groupes.substring(0, groupes.length()-1);
				
				String profs = "";
				Collection<LinkUtilEDT> profsCours = c.getProf();
				for (LinkUtilEDT prof : profsCours){
					profs.concat(prof.getPrenom()+" "+prof.getNom()+",");
				}
				profs.substring(0, profs.length()-1);
				
				int heureDebut = c.getDebut().getHour();
				int minuteDebut = c.getDebut().getMinute();
				int heureFin = c.getFin().getHour();
				int minuteFin = c.getFin().getMinute();
				String horaire = heureDebut+","+minuteDebut+","+heureFin+","+minuteFin;
				int jour = c.getDebut().getDayOfWeek().getValue()-1;
				out.println("<div class='testDiv' value='couleur:"+couleur+"; matiere:"+matiere+"; type:"+type+"; salle:"+salles+"; groupes:"+groupes+" prof:"+profs+"; horaire:"+horaire+"; jour:"+jour+"'></div>");
			}
		}
		%>
	</form>

<script src="schedule_script.js"></script>
</body>
</html>