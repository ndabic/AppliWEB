package pack;

import javax.ejb.Singleton;
import javax.persistence.*;
import java.util.*;

@Singleton
public class Facade {
	
	@PersistenceContext
	private EntityManager em;
	
	public Facade() {}
	
	Random random = new Random();
	
	private String[] generateRandomCodes() {
		String[] codes = new String[3];
		for (int i = 0; i < 3; i++) {
			codes[i] = "";
	        for (int j = 0; j < 6; j++) {
	            int digit = random.nextInt(16);
	            String hexDigit = Integer.toHexString(digit);
	            
	            codes[i] += hexDigit;
	        }
		}
		return codes;
	}
	
	public void initEDT() {
		String[] codes = generateRandomCodes();
		Edt edt = new Edt();
		edt.setCodeAdmin(codes[0]);
		edt.setCodeEtu(codes[1]);
		edt.setCodeProf(codes[2]);
		String requete = "SELECT * FROM Edt WHERE CodeAdmin=" + codes[0] +
				"OR CodeEtu=" + codes[1] + "OR CodeProf=" + codes[2];
		
		while (!(em.createQuery(requete, Edt.class).getResultList().isEmpty())) {
			codes = generateRandomCodes();
		}
		
		em.persist(edt);
	}
	
	public Collection<Cours> getCoursSemaine(String mail_utilisateur, Date lundi, Date vendredi){
		
		Utilisateur utilisateur = em.find(Utilisateur.class, mail_utilisateur);
		Collection<Groupe> groupes = utilisateur.getGroupes();
		Collection<Cours> coursSemaine = new ArrayList<>();
		for (Groupe groupe : groupes) {
			Collection<Cours> coursCourant = groupe.getCours_etude();
			for (Cours cours : coursCourant) {
				if (cours.getDebut().after(lundi) && cours.getFin().before(vendredi))
					coursSemaine.add(cours);
			}
		}
		return coursSemaine;
		
	}
	
	public boolean verif_doublon_utilisateur(String mail_utilisateur) {
		if (em.find(Utilisateur.class, mail_utilisateur) != null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean verif_connexion(String mail_utilisateur,String mdp) {
		Utilisateur utilisateur = em.find(Utilisateur.class, mail_utilisateur);
		if (utilisateur != null) {
			if (mdp.equals(utilisateur.getMdp())){
				return true;
			} else {
				return false;
			}
		} else { 
			return false;
		}
	}
	
	public void ajout_utilisateur(String nom, String prenom, String mdp, String mail) {
		Utilisateur nouveau = new Utilisateur();
		nouveau.setNom(nom);
		nouveau.setPrenom(prenom);
		nouveau.setMdp(mdp);
		nouveau.setMail(mail);
		
		em.persist(nouveau);
	}
	
	
}