package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Groupe {
	
	@Id
	String nom;
	
	@ManyToMany(mappedBy = "groupes", fetch=FetchType.EAGER)
	Collection<LinkUtilEDT> membres;
	
	@ManyToMany(mappedBy ="groupes", fetch=FetchType.EAGER)
	Collection<Cours> cours_etude;
	
	public Groupe() {}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Collection<Cours> getCours_etude() {
		return cours_etude;
	}

	public void setCours_etude(Collection<Cours> cours_etude) {
		this.cours_etude = cours_etude;
	}

	public Collection<LinkUtilEDT> getMembres() {
		return membres;
	}

	public void setMembres(Collection<LinkUtilEDT> membres) {
		this.membres = membres;
	}

}
