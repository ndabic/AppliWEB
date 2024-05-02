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
	
	
	Collection<Integer> numero; // A REVOIR
	
	@ManyToMany
	Collection<Edt> edts_prof_eleve;
	
	@ManyToMany
	Collection<Edt> edts_admin;
	
	
	@ManyToMany
	Collection<Groupe> groupes;
	
	@OneToMany(mappedBy = "prof", fetch=FetchType.EAGER)
	Collection<Cours> enseigne;
	
	
	public Utilisateur() {}
	
	public Collection<Edt> getEdts_prof_eleve() {
		return edts_prof_eleve;
	}


	public void setEdts_prof_eleve(Collection<Edt> edts_prof_eleve) {
		this.edts_prof_eleve = edts_prof_eleve;
	}


	public Collection<Edt> getEdts_admin() {
		return edts_admin;
	}


	public void setEdts_admin(Collection<Edt> edts_admin) {
		this.edts_admin = edts_admin;
	}


	public Collection<Groupe> getGroupes() {
		return groupes;
	}


	public void setGroupes(Collection<Groupe> groupes) {
		this.groupes = groupes;
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
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Collection<Integer> getNumero() {
		return numero;
	}
	public void setNumero(Collection<Integer> numero) {
		this.numero = numero;
	}

	public Collection<Cours> getEnseigne() {
		return enseigne;
	}

	public void setEnseigne(Collection<Cours> enseigne) {
		this.enseigne = enseigne;
	}
	

	
		
}
