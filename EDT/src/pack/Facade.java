package pack;

import javax.ejb.Singleton;
import javax.persistence.*;
import javax.rmi.CORBA.Util;
import javax.servlet.http.Part;

import java.time.*;
import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	
	public String getUserCookie(String mail) {
		Utilisateur utilisateur = em.find(Utilisateur.class, mail);
		return utilisateur.getCookie();
	}
	
	public String getUserInfos(String cookie) {
		String requete = "SELECT u FROM Utilisateur u WHERE u.cookie = :cookie";
        TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
        query.setParameter("cookie", cookie);
        query.setMaxResults(1);

        // Execute the query and get the result list
        Collection<Utilisateur> utilisateur = query.getResultList();
        String res = null;
        if (utilisateur.isEmpty() || utilisateur.size() > 1) {
        	return res;
        }else {
        	for (Utilisateur u : utilisateur) {
				return u.getPrenom() + "," + u.getNom() + "," + u.getMail();
			}
        }
        return res;
	}
	
	
	public boolean verif_doublon_utilisateur(String mail_utilisateur) {
		return (em.find(Utilisateur.class, mail_utilisateur) == null);
	}
	
	public boolean verif_doublon_linkUtilEdt(String numero, String edt) {
		return true;
	}
	
	
	
	public boolean verif_connexion(String mail_utilisateur,String mdp) {
		Utilisateur utilisateur = em.find(Utilisateur.class, mail_utilisateur);
		return (utilisateur != null) && (mdp.equals(utilisateur.getMdp()));
	}
	
	public boolean verif_horaire_groupe(Groupe groupe, LocalDateTime nouvDebut, LocalDateTime nouvFin, Edt edtAssocie) {
		

        String requete = "SELECT * FROM Cours c WHERE c.edt_associe = :edtAssocie AND c.groupes = :groupe AND (c.debut < :nouvFin AND :nouvDebut < c.fin)";
        TypedQuery<Cours> query = em.createQuery(requete, Cours.class);
        query.setParameter("nouvDebut", nouvDebut);
        query.setParameter("nouvFin", nouvFin);
        query.setParameter("edtAssocie", edtAssocie);
        query.setParameter("groupe", groupe);
        query.setMaxResults(1);

        Collection<Cours> cours_trouves = query.getResultList();
		
		return cours_trouves.isEmpty();
	}
	
	public boolean verif_horaire_prof(LinkUtilEDT prof, LocalDateTime nouvDebut, LocalDateTime nouvFin, Edt edtAssocie) {

        String requete = "SELECT * FROM Cours c WHERE c.edt_associe = :edtAssocie AND c.prof = :prof AND (c.debut < :nouvFin AND :nouvDebut < c.fin)";
        TypedQuery<Cours> query = em.createQuery(requete, Cours.class);
        query.setParameter("nouvDebut", nouvDebut);
        query.setParameter("nouvFin", nouvFin);
        query.setParameter("edtAssocie", edtAssocie);
        query.setParameter("prof", prof);
        query.setMaxResults(1);

        Collection<Cours> cours_trouves = query.getResultList();
		
		return cours_trouves.isEmpty();
	}
	
	public boolean verif_horaire_salle(Salle salle, LocalDateTime nouvDebut, LocalDateTime nouvFin, Edt edtAssocie) {
		

        String requete = "SELECT * FROM Cours c WHERE c.edt_associe = :edtAssocie AND c.salle = :salle AND (c.debut < :nouvFin AND :nouvDebut < c.fin)";
        TypedQuery<Cours> query = em.createQuery(requete, Cours.class);
        query.setParameter("nouvDebut", nouvDebut);
        query.setParameter("nouvFin", nouvFin);
        query.setParameter("edtAssocie", edtAssocie);
        query.setParameter("salle", salle);
        query.setMaxResults(1);

        // Execute the query and get the result list
        Collection<Cours> cours_trouves = query.getResultList();
		
		return cours_trouves.isEmpty();
	}
	
	public String verif_cours(String heureDebut_string, String minuteDebut_string, String heureFin_string, String minuteFin_string, 
            String jour_string, String mois_string, String annee_string, String type_string, String matiere_string, 
            String salle_string, String professeur_string, String groupe_string, String infosupp_string, String edt) {
		
		int heuredebut =  Integer.parseInt(heureDebut_string) ;
		int minutedebut = Integer.parseInt(minuteDebut_string);
		int heurefin = Integer.parseInt(heureFin_string);
		int minutefin = Integer.parseInt(minuteFin_string);
		int jours = Integer.parseInt(jour_string);
		int mois = Integer.parseInt(mois_string);
		int annee = Integer.parseInt(annee_string);
		LocalDateTime debut = LocalDateTime.of(annee, mois, jours, heuredebut, minutedebut, 0);
		LocalDateTime fin = LocalDateTime.of(annee, mois, jours, heurefin, minutefin, 0);
		String[] groupes = groupe_string.split(",");
		String[] salles = salle_string.split(",");
		String[] profs = professeur_string.split(",");
		String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
		
		
		Collection<Groupe> list_groupe = new ArrayList<Groupe>();
		for(String g_string : groupes) {
			Groupe g = em.find(Groupe.class, g_string);
			if (g != null) {
				if (!verif_horaire_groupe(g, debut, fin, edt_associe)) {
					return ("Groupe indisponible: "+g.getNom());
				}
			}else{
				return ("Groupe introuvable: "+g.getNom());
			}
		}
		
		Collection<LinkUtilEDT> list_profs = new ArrayList<LinkUtilEDT>();
		for(String prof_string : profs) {
			LinkUtilEDT p = em.find(LinkUtilEDT.class, prof_string);
			if (p != null) {
				if (!verif_horaire_prof(p, debut, fin, edt_associe)) {
					return ("Professeur(e) indisponible: "+p.getNom());
				}
			}else{
				return ("Professeur(e) introuvable: "+p.getNom());
			}
		}
		
		Collection<Salle> list_salles = new ArrayList<Salle>();
		for(String s_string : salles) {
			Salle s = em.find(Salle.class, s_string);
			if (s != null) {
				if (!verif_horaire_salle(s, debut, fin, edt_associe)) {
					return ("Salle indisponible: "+s.getNom());
				}
			}else{
				return ("Salle introuvable: "+s.getNom());
			}
		}
		
		Matiere matiere = em.find(Matiere.class,matiere_string);
		if (matiere == null) {
			return ("Matière introuvable: "+matiere.getNom());
		}
		Type type = em.find(Type.class, type_string);
		if (type == null) {
			return ("Type introuvable: "+type.getNom());
		}
		
		
		return null;
	}
	
	
	
	
	public void ajout_utilisateur(String nom, String prenom, String mdp, String mail) {
		Utilisateur nouveau = new Utilisateur();
		nouveau.setNom(nom);
		nouveau.setPrenom(prenom);
		nouveau.setMdp(mdp);
		nouveau.setMail(mail);
		String cook = nom+prenom+mail;
		int cookie = cook.hashCode();
		cook = String.valueOf(cookie);
		nouveau.setCookie(cook);
		
		em.persist(nouveau);
	}
	
	
	
	public void ajout_cours(String heureDebut_string, String minuteDebut_string, String heureFin_string, String minuteFin_string, 
            String jour_string, String mois_string, String annee_string, String type_string, String matiere_string, 
            String salle_string, String professeur_string, String groupe_string, String infosupp_string, String edt) {
		int heuredebut =  Integer.parseInt(heureDebut_string) ;
		int minutedebut = Integer.parseInt(minuteDebut_string);
		int heurefin = Integer.parseInt(heureFin_string);
		int minutefin = Integer.parseInt(minuteFin_string);
		int jours = Integer.parseInt(jour_string);
		int mois = Integer.parseInt(mois_string);
		int annee = Integer.parseInt(annee_string);
		LocalDateTime debut = LocalDateTime.of(annee, mois, jours, heuredebut, minutedebut, 0);
		LocalDateTime fin = LocalDateTime.of(annee, mois, jours, heurefin, minutefin, 0);
		String[] groupes = groupe_string.split(",");
		String[] salles = salle_string.split(",");
		String[] profs = professeur_string.split(",");
		
		
		Collection<Groupe> list_groupe = new ArrayList<Groupe>();
		for(String g_string : groupes) {
			Groupe g = em.find(Groupe.class, g_string);
			list_groupe.add(g);
		}
		
		Collection<LinkUtilEDT> list_profs = new ArrayList<LinkUtilEDT>();
		for(String prof_string : profs) {
			LinkUtilEDT p = em.find(LinkUtilEDT.class, prof_string);
			list_profs.add(p);
		}
		
		Collection<Salle> list_salles = new ArrayList<Salle>();
		for(String s_string : salles) {
			Salle s = em.find(Salle.class, s_string);
			list_salles.add(s);
		}
		
		Matiere matiere = em.find(Matiere.class,matiere_string);
		Type type = em.find(Type.class, type_string);
		
		String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	
    	
		Cours cours = new Cours();
        cours.setDebut(debut);
        cours.setFin(fin);
        cours.setGroupes(list_groupe);
        cours.setProf(list_profs);
        cours.setMatiere(matiere);
        cours.setType(type);
        cours.setSalle(list_salles);
        cours.setEdt_associe(edt_associe);

        em.persist(cours);
	}
        
    public void ajout_etudiant(String numero,String prenom,String nom, String edt) {
    	//on crée l'étudiant
    	LinkUtilEDT etudiant = new LinkUtilEDT();
    	etudiant.setNumero(numero);
    	etudiant.setPrenom(prenom);
    	etudiant.setNom(nom);
    	etudiant.setIsProf(false);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	etudiant.setEdt(edt_associe);
    	
    	em.persist(etudiant);
    
    }
    
    public void ajout_prof(String numero,String prenom,String nom, String edt) {
    	//on crée le prof
    	LinkUtilEDT prof = new LinkUtilEDT();
    	prof.setNumero(numero);
    	prof.setPrenom(prenom);
    	prof.setNom(nom);
    	prof.setIsProf(true);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	prof.setEdt(edt_associe);
    	
    	em.persist(prof);
    
    }
    
    public void ajout_matiere(String nom, String edt) {
    	//on crée le prof
    	Matiere matiere = new Matiere();
    	matiere.setNom(nom);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	matiere.setEdt_associe(edt_associe);
    	
    	em.persist(matiere);
    
    }
    
    public void ajout_type(String nom, String edt) {
    	//on crée le prof
    	Matiere matiere = new Matiere();
    	matiere.setNom(nom);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	matiere.setEdt_associe(edt_associe);
    	
    	em.persist(matiere);
    
    }
    
    public void ajout_salle(String nom, String edt) {
    	//on crée le prof
    	Salle salle = new Salle();
    	salle.setNom(nom);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	salle.setEdt_associe(edt_associe);
    	
    	em.persist(salle);
    
    }
    
    public void creer_groupe(String nom, Part liste_etudiant_csv, String edt) {
    	Groupe groupe = new Groupe();
    	groupe.setNom(nom);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	groupe.setEdt_associe(edt_associe);
    	
    	Collection<LinkUtilEDT> liste_eleves = new ArrayList<LinkUtilEDT>();
    	
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(liste_etudiant_csv.getInputStream()));
			String ligne;
			while ((ligne = reader.readLine()) != null) {
			    // Traitement des lignes du fichier CSV
			    String[] elements = ligne.split(",");

			    LinkUtilEDT code_eleve = em.find(LinkUtilEDT.class,elements[0]);
			    liste_eleves.add(code_eleve);
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String getGroupesEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<Groupe> groupes = edt_associe.getGroupes();
    	String res = "";
    	for(Groupe g : groupes) {
    		res += ","+g;
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
    
    public String getSallesEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<Salle> salles = edt_associe.getSalles();
    	String res = "";
    	for(Salle s : salles) {
    		res += ","+s;
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
    
    public String getProfsEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<LinkUtilEDT> all_links = edt_associe.getLiens_utilisateur_EDT();
    	String res = "";
    	for(LinkUtilEDT l : all_links) {
    		if (l.getIsProf()) // keep only links that are teachers
    			res += ","+l;
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
    
    public String getMatieresEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<Matiere> matieres = edt_associe.getMatieres();
    	String res = "";
    	for(Matiere m : matieres) {
    		res += ","+m;
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
    
    public String getTypesEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<Type> types = edt_associe.getTypes();
    	String res = "";
    	for(Type t : types) {
    		res += ","+t;
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
}
    