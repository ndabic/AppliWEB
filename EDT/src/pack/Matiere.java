package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Matiere {
		
	@Id
	String nom;
	
	@OneToMany(mappedBy ="matiere")
	List<Cours> cours_associes;
	
	public Matiere() {}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Cours> getCours_associes() {
		return cours_associes;
	}

	public void setCours_associes(List<Cours> cours_associes) {
		this.cours_associes = cours_associes;
	}

}
