package pack;

import java.util.List;

import javax.persistence.*;

@Entity
public class Salle {
	
	@Id
	String nom;
	
	//Boolean libre;
	
	@OneToMany(mappedBy ="salle", fetch=FetchType.EAGER)
	List<Cours> cours_associes;
	
	
	
	public Salle() {}

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
