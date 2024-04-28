package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Groupe {
	
	@Id
	String nom;
	
	@ManyToMany(mappedBy = "groupes", fetch=FetchType.EAGER)
	List<Utilisateur> membres;
	
	@ManyToMany(mappedBy ="groupes", fetch=FetchType.EAGER)
	List<Cours> cours_etude;
	
	public Groupe() {}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Cours> getCours_etude() {
		return cours_etude;
	}

	public void setCours_etude(List<Cours> cours_etude) {
		this.cours_etude = cours_etude;
	}

	public List<Utilisateur> getMembres() {
		return membres;
	}

	public void setMembres(List<Utilisateur> membres) {
		this.membres = membres;
	}

}
