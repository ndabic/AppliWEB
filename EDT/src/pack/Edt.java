package pack;

import java.util.*;

import javax.persistence.*;

@Entity
public class Edt {
	
	@Id 
	String codeAdmin, codeEtu, codeProf;
	
	@ManyToMany(mappedBy = "edts_prof_eleve")
	List<Utilisateur> utilisateurs_prof_eleve;
	
	@ManyToMany(mappedBy = "edts_admin")
	List<Utilisateur> admins;

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

	public List<Utilisateur> getUtilisateurs_prof_eleve() {
		return utilisateurs_prof_eleve;
	}

	public void setUtilisateurs_prof_eleve(List<Utilisateur> utilisateurs_prof_eleve) {
		this.utilisateurs_prof_eleve = utilisateurs_prof_eleve;
	}

	public List<Utilisateur> getAdmins() {
		return admins;
	}

	public void setAdmins(List<Utilisateur> admins) {
		this.admins = admins;
	}
	
	

}
