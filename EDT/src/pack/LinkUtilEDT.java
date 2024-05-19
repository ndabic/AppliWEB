package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class LinkUtilEDT {

	@Id @GeneratedValue
	int id;
	
	
	int numero; //numero Carte Etudiant
	
	@ManyToOne
	Utilisateur utilisateur;
	
	@ManyToOne
	Edt edt;
	
	@ManyToMany(mappedBy = "prof", fetch=FetchType.EAGER)
	Collection<Cours> enseigne;
	
	@ManyToMany
	Collection<Groupe> groupes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Edt getEdt() {
		return edt;
	}

	public void setEdt(Edt edt) {
		this.edt = edt;
	}

	public Collection<Cours> getEnseigne() {
		return enseigne;
	}

	public void setEnseigne(Collection<Cours> enseigne) {
		this.enseigne = enseigne;
	}

	public Collection<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(Collection<Groupe> groupes) {
		this.groupes = groupes;
	}
	
	
	
}
