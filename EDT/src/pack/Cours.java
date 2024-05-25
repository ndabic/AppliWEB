package pack;

import java.time.*;
import java.util.*;

import javax.persistence.*;

@Entity
public class Cours {
	
	@Id
	@GeneratedValue
	int id;
	
	LocalDateTime debut, fin;
	
	@ManyToMany
	Collection<Groupe> groupes;
	
	@ManyToMany
	Collection<LinkUtilEDT> prof; // lie le prof grâce à son numéro
	

	@ManyToOne
	Matiere matiere;
	
	@ManyToOne
	Type type;
	
	@ManyToMany
	Collection<Salle> salle;
	
	@ManyToOne
	Edt edt_associe;

	public Cours() {}
	
	public Collection<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(Collection<Groupe> groupes) {
		this.groupes = groupes;
	}

	public Collection<LinkUtilEDT> getProf() {
		return prof;
	}

	public void setProf(Collection<LinkUtilEDT> prof) {
		this.prof = prof;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDebut() {
		return debut;
	}

	public void setDebut(LocalDateTime debut) {
		this.debut = debut;
	}

	public LocalDateTime getFin() {
		return fin;
	}

	public void setFin(LocalDateTime fin) {
		this.fin = fin;
	}

	public Matiere getMatiere() {
		return matiere;
	}

	public void setMatiere(Matiere matiere) {
		this.matiere = matiere;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Collection<Salle> getSalle() {
		return salle;
	}

	public void setSalle(Collection<Salle> salle) {
		this.salle = salle;
	}

	public Edt getEdt_associe() {
		return edt_associe;
	}

	public void setEdt_associe(Edt edt_associe) {
		this.edt_associe = edt_associe;
	}
	
	

}
