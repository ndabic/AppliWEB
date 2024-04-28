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
			int heureDebut = c.getDebut().getHours();
			int minuteDebut = c.getDebut().getMinutes();
			int heureFin = c.getFin().getHours();
			int minuteFin = c.getFin().getMinutes();
			int jour = c.getDebut().getDay();
			Collection<Groupe> groupes = c.getGroupes();
			out.println("<div class='hiddenDiv' value='color:"+c.getId()+"'></div>");
		}%>
	</form>

<script src="schedule_script.js"></script>
</body>
</html>