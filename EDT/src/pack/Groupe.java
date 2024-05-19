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
	
	@ManyToOne
	Edt edt_associe;
	
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

	public Edt getEdt_associe() {
		return edt_associe;
	}

	public void setEdt_associe(Edt edt_associe) {
		this.edt_associe = edt_associe;
	}
	
	

}
