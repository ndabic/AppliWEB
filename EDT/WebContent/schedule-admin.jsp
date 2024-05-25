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
    <link rel="stylesheet" type="text/css" href="schedule-admin_style.css">
	<link rel="icon" type="image/png" href="./assets/Logo.png">
</head>
<body>

	<div class="header">
		<h1>Emploi Du Temps</h1>
		
		<div class="notif hide"></div>
		
		<div class="user-container connected">

			<div class="user-profile" onclick="location.href='profile.html'"></div>
		
			<button class="sign-button button-color-1 hide" onclick="location.href='connexion.html'">
				Connexion
			</button>

			<button class="sign-button button-color-2 hide" onclick="location.href='inscription.html'">
				Inscription
			</button>

		</div>
	</div>
	
	<div class="admin-button">
		
		<div class="dropdown">
			<form class="hide" id="getCoursGroupes" name="getCoursGroupes" method="post" action="Serv">
				<input type="text" id="groups-schedule-view" name="groups-schedule-view">
            </form>
			<button onclick="toggleDropdownSendForm('dropdown-div6')" class="add-button view-button">Vue Groupes:</button>
			<div id="dropdown-div6" class="view-content">
			  
		   	</div>
	  	</div>

		<button onclick="toggleShow(this)" class="add-button">Ajouter Cours</button>
		<div class="dropdown">
		  
			  <button onclick="toggleDropdown('dropdown-div')" class="add-button drop-button">Ajouter Autre</button>
			  <div id="dropdown-div" class="dropdown-content">
				<a onclick="toggleShow(this)">Étudiant</a>
				<a onclick="toggleShow(this)">Professeur</a>
			    <a onclick="toggleShow(this)">Groupe</a>
			    <a onclick="toggleShow(this)">Matière</a>
			    <a onclick="toggleShow(this)">Salle</a>
			    <a onclick="toggleShow(this)">Type</a>
			    
			 </div>
		</div>

		<div class="mondays-container"></div>
	
	</div>
	
	<div class="popup-container">
		<div class="popup">
			
	        
		</div>
	</div>
	

	
	

	<div class="schedule-container">
	    <div id="schedule"></div>
	    <div id="cours_container"></div>
	</div>
	
	<form action="Serv" method="get">
		<% 
		if (request.getAttribute("edtCodes") != null){
			String edtCodes = (String) request.getAttribute("edtCodes");
			out.println("<div id='edtCodes' class='hide'>"+edtCodes+"</div>");
		}
		%>
	</form>
	

<script src="schedule-admin_script.js"></script>
</body>
</html>