package pack;

import javax.ejb.Singleton;
import javax.persistence.*;

import java.time.*;
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
	
	public List<LocalDateTime> getLundiVendrediCourant() {
		LocalDate today = LocalDate.now();
		int jourSemaine = today.getDayOfWeek().getValue();
		LocalDate lundi = today.minusDays(jourSemaine-1);
		LocalDate vendredi = today.plusDays(5-jourSemaine);
        
        LocalTime matin = LocalTime.of(1, 0, 0);
        LocalDateTime lundiMatin = LocalDateTime.of(lundi, matin);
        
        LocalTime soir = LocalTime.of(23, 0, 0);
        LocalDateTime vendrediSoir = LocalDateTime.of(vendredi, soir);
		List<LocalDateTime> lundiVendredi = new ArrayList<>();
		lundiVendredi.add(lundiMatin);
		lundiVendredi.add(vendrediSoir);
		return lundiVendredi;
		
	}
	
	public Collection<Cours> getCoursSemaine(String mail_utilisateur){
		List<LocalDateTime> lundiVendredi = getLundiVendrediCourant();
		LocalDateTime lundi = lundiVendredi.get(0);
		LocalDateTime vendredi = lundiVendredi.get(1);
		Collection<Cours> coursSemaine = new ArrayList<>();
		Utilisateur utilisateur = em.find(Utilisateur.class, mail_utilisateur);
		Collection<LinkUtilEDT> links = utilisateur.getLienEDT();
		for(LinkUtilEDT link : links) {
			Collection<Groupe> groupes = link.getGroupes();
			for (Groupe groupe : groupes) {
				Collection<Cours> coursCourant = groupe.getCours_etude();
				for (Cours cours : coursCourant) {
					if (cours.getDebut().isAfter(lundi) && cours.getFin().isBefore(vendredi))
						coursSemaine.add(cours);
				}
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
	
	public void ajout_cours(String heureDebut, String minuteDebut, String heureFin, String minuteFin, 
            String jour, String mois_string, String annee_string, String type, String matiere, 
            String salle, String professeur, String groupe, String infosupp) {
		int heuredebut =  Integer.parseInt(heureDebut) ;
		int minutedebut = Integer.parseInt(minuteDebut);
		int heurefin = Integer.parseInt(heureFin);
		int minutefin = Integer.parseInt(minuteFin);
		int jours = Integer.parseInt(jour);
		int mois = Integer.parseInt(mois_string);
		int annee = Integer.parseInt(annee_string);
		LocalDateTime debut = LocalDateTime.of(annee, mois, jours, heuredebut, minutedebut, 0);
		LocalDateTime fin = LocalDateTime.of(annee, mois, jours, heurefin, minutefin, 0);
		
		Cours cours = new Cours();
        cours.setDebut(debut);
        cours.setFin(fin);
        cours.setGroupes(groupes);
        cours.setProf(prof);
        cours.setMatiere(matiere);
        cours.setType(type);
        cours.setSalle(salles);

        em.persist(cours);
	}
        
    public void ajout_etudiant(String numero,String prenom,String nom) {
    	//on crée l'étudiant
    	Utilisateur etudiant = new Utilisateur();
    	etudiant.setPrenom(prenom);
    	etudiant.setNom(nom);
    
    
    }
    
    public void ajout_prof(String numero,String prenom,String nom) {
    	//on crée l'étudiant
    	Utilisateur prof = new Utilisateur();
    	prof.setPrenom(prenom);
    	prof.setNom(nom);
    
    
    }
    
    public void creer_groupe(String nom,String liste_etudiant) {}
     
	
	
}