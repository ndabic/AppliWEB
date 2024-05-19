package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class LinkUtilEDT {

	
	@Id
	String numero; //numero Carte Etudiant
	
	@ManyToOne
	Edt edt;
	
	String prenom;
	String nom;
	
	@ManyToOne
	Utilisateur utilisateur;
	
	
	
	@ManyToMany(mappedBy = "prof", fetch=FetchType.EAGER)
	Collection<Cours> enseigne;
	
	@ManyToMany
	Collection<Groupe> groupes;
	
	Boolean isProf;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Edt getEdt() {
		return edt;
	}

	public void setEdt(Edt edt) {
		this.edt = edt;
	}

	public Collection<Cours> getEnseigne() {
		return enseigne;
	}

	public void setEnseigne(Collection<Cours> enseigne) {
		this.enseigne = enseigne;
	}

	public Collection<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(Collection<Groupe> groupes) {
		this.groupes = groupes;
	}

	public Boolean getIsProf() {
		return isProf;
	}

	public void setIsProf(Boolean isProf) {
		this.isProf = isProf;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
	
}
