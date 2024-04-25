package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Groupe {
	
	@Id
	String nom;
	
	@ManyToMany(mappedBy = "groupes")
	List<Utilisateur> membres;
	
	@ManyToMany(mappedBy ="groupes")
	List<Cours> cours_etude;
	
	public Groupe() {}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Utilisateur> getMembres() {
		return membres;
	}

	public void setMembres(List<Utilisateur> membres) {
		this.membres = membres;
	}

}
