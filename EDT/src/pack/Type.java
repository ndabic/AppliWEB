package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Type {
	
	@Id
	String nom;
	
	@OneToMany(mappedBy ="type", fetch=FetchType.EAGER)
	List<Cours> cours_associes;
	
	public Type() {}

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
