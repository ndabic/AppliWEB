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
    <link rel="stylesheet" type="text/css" href="profile_style.css">
	<link rel="icon" type="image/png" href="./assets/Logo.png">
</head>
<body>

	<div class="header">
		<h1 onclick="location.href='schedule.html'">&#160;Emploi Du Temps&#160;</h1>
	</div>
	
    <div class="page-container">
        <div class="infos-container">
            <h2 class="edt-title">&#160;Informations Utilisateur&#160;</h2>
            <table>
                <tr>
                    <td><label for="user-name" class="label-edt" onclick="copyContent('Light')">&#160;PrÃ©nom:&#160;</label></td>
                    <td><input type="text" id="user-firstname" class="info-input" value=" Light"></input></td>
                </tr>
                <tr>
                    <td><label for="user-firstname" class="label-edt" onclick="copyContent('Yagami')">&#160;Nom:&#160;</label></td>
                    <td><input type="text" id="user-name" class="info-input" value=" Yagami"></input></td>
                </tr>
                <tr>
                    <td><label for="user-email" class="label-edt" onclick="copyContent('00567823')">&#160;Email:&#160;</label></td>
                    <td><input type="text" id="user-email" class="info-input" value=" ryuk@gmail.com"></input></td>
                </tr>

                <tr><td><button class="save-user-infos">S</button></td></tr>
                
                <tr>
                    <td class="pswrd-td"><label for="user-new-pswd" class="label-edt" onclick="copyContent('00567823')">&#160;Mot de passe:&#160;</label></td>
                    <td class="pswrd-td"><input type="text" id="user-new-pswd" class="pswrd-input" value=" Nouveau mot de passe"></input></td>
                </tr>
                <tr>
                    <td><label for="user-new-pswd-conf" class="label-edt" onclick="copyContent('00567823')">&#160;Confirmation:&#160;</label></td>
                    <td><input type="text" id="user-new-pswd-conf" class="pswrd-input" value=" Nouveau mot de passe"></input></td>
                </tr>

                <tr><td><button class="save-user-pswrd">S</button></td></tr>
            </table>
                   
        </div>

        <div class="edt-container">
            <div class="edt-header">
                <h2 class="edt-title">&#160;Gestionnaire d'emplois du temps&#160;</h2>
                <div class="edt-buttons">
                    <button class="button-add-edt" title="CrÃ©er un EDT">&#160;+&#160;</button>
                    <button class="button-link-edt" title="Se lier Ã  un EDT">&#160;-//-&#160;</button>
                </div>
            </div>
            <div class="notif hide"></div>
            <div class="edt-scroller">
                <div class="edt edt-admin 0">
                    <div class="edt-name">EDT ADMINISTRATEUR</div>
                    <div class="edt-interface ">
                        <div class="code-edt code-admin 0" onclick="copyContent('AXI567')">&#160;AXI567&#160;</div>
                        <div class="code-edt code-prof 0" onclick="copyContent('IUY543')">&#160;IUY543&#160;</div>
                        <div class="code-edt code-etu 0" onclick="copyContent('OIU987')">&#160;OIU987&#160;</div>
                        <button class="remove-edt 0" title="DÃ©truire l'edt">&#160;X&#160;</button>
                    </div>
                </div>
                <div class="edt edt-linked 1">
                    <div class="edt-name">EDT LIÃ</div>
                    <div class="edt-interface">
                        <div class="code-edt code-admin 0" onclick="copyContent('AXI567')">&#160;AXI567&#160;</div>
                        <label for="link-number" class="label-edt label-num" onclick="copyContent('00567823')">&#160;NumÃ©ro:&#160;</label>
                        <input id="link-number" class="link-edt" value=" 00567823"></input>
                        <button class="save-number 1" title="Sauvegarder le numÃ©ro courant">&#160;S&#160;</button>
                        <button class="remove-edt 1" title="DÃ©truire l'edt">&#160;X&#160;</button>
                    </div>
                </div>
                
            </div>
        </div>

    </div>

	
	

<script src="profile_script.js"></script>
</body>
</html>