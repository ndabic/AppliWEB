package pack;

import javax.persistence.*;

@Entity
public class Utilisateur {
	
	public enum Statut {
		  ADMIN, ENSEIGNANT, ETUDIANT
		}
	
	@Id @GeneratedValue
	int id;
	
	String nom;
	String prenom;
	String mdp;
	String mail;
	int numero;
	Statut statut;
	
	public Utilisateur() {}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Statut getStatut() {
		return statut;
	}
	public void setStatut(Statut statut) {
		this.statut = statut;
	}
	
		
}
