package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Utilisateur {
	
	
	@Id @GeneratedValue
	int id;
	
	String nom;
	String prenom;
	String mdp;
	String mail;
	int numero;
	
	@ManyToMany
	List<Edt> edts_prof_eleve;
	
	@ManyToMany
	List<Edt> edts_admin;
	
	
	@ManyToMany
	List<Groupe> groupes;
	
	@OneToMany(mappedBy = "prof", fetch=FetchType.EAGER)
	List<Cours> enseigne;
	
	
	public Utilisateur() {}
	
	public List<Edt> getEdts_prof_eleve() {
		return edts_prof_eleve;
	}


	public void setEdts_prof_eleve(List<Edt> edts_prof_eleve) {
		this.edts_prof_eleve = edts_prof_eleve;
	}


	public List<Edt> getEdts_admin() {
		return edts_admin;
	}


	public void setEdts_admin(List<Edt> edts_admin) {
		this.edts_admin = edts_admin;
	}


	public List<Groupe> getGroupes() {
		return groupes;
	}


	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}
	

	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}

	public List<Cours> getEnseigne() {
		return enseigne;
	}

	public void setEnseigne(List<Cours> enseigne) {
		this.enseigne = enseigne;
	}
	

	
		
}
