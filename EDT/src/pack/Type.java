package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Type {
	
	@Id
	String nom;
	
	@OneToMany(mappedBy ="type", fetch=FetchType.EAGER)
	Collection<Cours> cours_associes;
	
	@ManyToOne
	Edt edt_associe;
	
	public Type() {}

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

	public Edt getEdt_associe() {
		return edt_associe;
	}

	public void setEdt_associe(Edt edt_associe) {
		this.edt_associe = edt_associe;
	}
	
	
}
