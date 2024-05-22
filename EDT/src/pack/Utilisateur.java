package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Utilisateur {
	
	
	@Id 
	String mail;
	
	String nom;
	String prenom;
	String mdp;
	
	
	//Collection<Integer> numero; // A REVOIR
	
	
	@ManyToMany
	Collection<Edt> edts_admin;
	
	@OneToMany(mappedBy = "utilisateur", fetch=FetchType.EAGER)
	Collection<LinkUtilEDT> lienEDT;
	
	
	String cookie;
	
	
	public Utilisateur() {}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public Collection<Edt> getEdts_admin() {
		return edts_admin;
	}


	public void setEdts_admin(Collection<Edt> edts_admin) {
		this.edts_admin = edts_admin;
	}


	public Collection<LinkUtilEDT> getLienEDT() {
		return lienEDT;
	}


	public void setLienEDT(Collection<LinkUtilEDT> lienEDT) {
		this.lienEDT = lienEDT;
	}


	public String getCookie() {
		return cookie;
	}


	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	
	
	
}
