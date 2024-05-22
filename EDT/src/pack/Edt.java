package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Edt {
	
	@Id 
	String codeAdmin;
	String codeEtu;
	String codeProf;
	
	String nom;
	
	@OneToMany(mappedBy = "edt", fetch=FetchType.EAGER)
	Collection<LinkUtilEDT> liens_utilisateur;  // Numéros étudiants (liens entre l'étudiant et son emploi du temps)
	
	@ManyToMany(mappedBy = "edts_admin", fetch=FetchType.EAGER)
	Collection<Utilisateur> admins;
	
	@OneToMany(mappedBy = "edt_associe", fetch=FetchType.EAGER)
	Collection<Cours> cours;
	
	@OneToMany(mappedBy = "edt_associe", fetch=FetchType.EAGER)
	Collection<Matiere> matieres;
	
	@OneToMany(mappedBy = "edt_associe", fetch=FetchType.EAGER)
	Collection<Type> types;
	
	@OneToMany(mappedBy = "edt_associe", fetch=FetchType.EAGER)
	Collection<Groupe> groupes;
	
	@OneToMany(mappedBy = "edt_associe", fetch=FetchType.EAGER)
	Collection<Salle> salles;

	public Edt() {}
	
	public String getCodeAdmin() {
		return codeAdmin;
	}

	public void setCodeAdmin(String codeAdmin) {
		this.codeAdmin = codeAdmin;
	}

	public String getCodeEtu() {
		return codeEtu;
	}

	public void setCodeEtu(String codeEtu) {
		this.codeEtu = codeEtu;
	}

	public String getCodeProf() {
		return codeProf;
	}

	public void setCodeProf(String codeProf) {
		this.codeProf = codeProf;
	}


	public Collection<Utilisateur> getAdmins() {
		return admins;
	}

	public void setAdmins(Collection<Utilisateur> admins) {
		this.admins = admins;
	}

	public Collection<LinkUtilEDT> getLiens_utilisateur_EDT() {
		return liens_utilisateur;
	}

	public void setLiens_utilisateur_EDT(Collection<LinkUtilEDT> liens_utilisateur_EDT) {
		this.liens_utilisateur = liens_utilisateur_EDT;
	}

	public Collection<Cours> getCours() {
		return cours;
	}

	public void setCours(Collection<Cours> cours) {
		this.cours = cours;
	}

	public Collection<LinkUtilEDT> getLiens_utilisateur() {
		return liens_utilisateur;
	}

	public void setLiens_utilisateur(Collection<LinkUtilEDT> liens_utilisateur) {
		this.liens_utilisateur = liens_utilisateur;
	}

	public Collection<Matiere> getMatieres() {
		return matieres;
	}

	public void setMatieres(Collection<Matiere> matieres) {
		this.matieres = matieres;
	}

	public Collection<Type> getTypes() {
		return types;
	}

	public void setTypes(Collection<Type> types) {
		this.types = types;
	}

	public Collection<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(Collection<Groupe> groupes) {
		this.groupes = groupes;
	}

	public Collection<Salle> getSalles() {
		return salles;
	}

	public void setSalles(Collection<Salle> salles) {
		this.salles = salles;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}


}
