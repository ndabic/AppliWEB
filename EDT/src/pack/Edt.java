package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Edt {
	
	@Id 
	String codeAdmin;
	String codeEtu;
	String codeProf;
	
	@ManyToMany(mappedBy = "edts_prof_eleve", fetch=FetchType.EAGER)
	Collection<Utilisateur> utilisateurs_prof_eleve;
	
	@ManyToMany(mappedBy = "edts_admin", fetch=FetchType.EAGER)
	Collection<Utilisateur> admins;
	
	@OneToMany(mappedBy = "edt_associe", fetch=FetchType.EAGER)
	Collection<Cours> cours;

	public Edt() {}
	
	public String getCodeAdmin() {
		return codeAdmin;
	}

	public void setCodeAdmin(String codeAdmin) {
		this.codeAdmin = codeAdmin;
	}

	public String getCodeEtu() {
		return codeEtu;
	}

	public void setCodeEtu(String codeEtu) {
		this.codeEtu = codeEtu;
	}

	public String getCodeProf() {
		return codeProf;
	}

	public void setCodeProf(String codeProf) {
		this.codeProf = codeProf;
	}

	public Collection<Utilisateur> getUtilisateurs_prof_eleve() {
		return utilisateurs_prof_eleve;
	}

	public void setUtilisateurs_prof_eleve(Collection<Utilisateur> utilisateurs_prof_eleve) {
		this.utilisateurs_prof_eleve = utilisateurs_prof_eleve;
	}

	public Collection<Utilisateur> getAdmins() {
		return admins;
	}

	public void setAdmins(Collection<Utilisateur> admins) {
		this.admins = admins;
	}
	
	

}
