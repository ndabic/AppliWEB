package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Cours {
	
	@Id
	int id;
	
	Date debut, fin;
	
	@ManyToMany
	List<Groupe> groupes;
	
	@ManyToOne
	Utilisateur prof;
	

	@ManyToOne
	Matiere matiere;
	
	@ManyToOne
	Type type;
	
	@ManyToOne
	Salle salle;

	public Cours() {}
	
	public List<Groupe> getGroupes() {
		return groupes;
	}

	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}

	public Utilisateur getProf() {
		return prof;
	}

	public void setProf(Utilisateur prof) {
		this.prof = prof;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
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

	public Salle getSalle() {
		return salle;
	}

	public void setSalle(Salle salle) {
		this.salle = salle;
	}

}
