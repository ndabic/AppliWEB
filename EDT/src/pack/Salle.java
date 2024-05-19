package pack;

import java.util.Collection;

import javax.persistence.*;

@Entity
public class Salle {
	
	@Id
	String nom;
	
	//Boolean libre;
	
	@ManyToMany(mappedBy ="salle", fetch=FetchType.EAGER)
	Collection<Cours> cours_associes;
	
	
	
	public Salle() {}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Collection<Cours> getCours_associes() {
		return cours_associes;
	}

	public void setCours_associes(Collection<Cours> cours_associes) {
		this.cours_associes = cours_associes;
	}


}
