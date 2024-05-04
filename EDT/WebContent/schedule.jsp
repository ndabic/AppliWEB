<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pack.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Weekly Schedule</title>
    <link rel="stylesheet" type="text/css" href="schedule_style.css">
</head>
<body>

	<h1>&#160;Emploi Du Temps&#160;</h1>
	
	<div class="container">
	    <div id="schedule"></div>
	    <div id="cours_container"></div>
	</div>
	
	<form action="Serv" method="get">
		<% Collection<Cours> lCours = (Collection<Cours>) request.getAttribute("lCours");
		for(Cours c: lCours){
			
			String couleur = c.getMatiere().getCouleur();
			String matiere = c.getMatiere().getNom();
			String type = c.getType().getNom();
			String salle = c.getSalle().getNom();
			String groupes = "";
			Collection<Groupe> groupesCours = c.getGroupes();
			for (Groupe groupe : groupesCours){
				groupes.concat(groupe.getNom()+",");
			}
			groupes.substring(0, groupes.length()-1);
			String prof = c.getProf().getUtilisateur().getPrenom()+" "+c.getProf().getUtilisateur().getNom();
			int heureDebut = c.getDebut().getHour();
			int minuteDebut = c.getDebut().getMinute();
			int heureFin = c.getFin().getHour();
			int minuteFin = c.getFin().getMinute();
			String horaire = heureDebut+","+minuteDebut+","+heureFin+","+minuteFin;
			int jour = c.getDebut().getDayOfWeek().getValue()-1;
			out.println("<div class='testDiv' value='couleur:"+couleur+"; matiere:"+matiere+"; type:"+type+"; salle:"+salle+"; groupes:"+groupes+" prof:"+prof+"; horaire:"+horaire+"; jour:"+jour+"'></div>");
		}%>
	</form>

<script src="schedule_script.js"></script>
</body>
</html>