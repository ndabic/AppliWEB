package pack;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.*;
import javax.rmi.CORBA.Util;
import javax.servlet.http.Part;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
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
	
	public String initEDT(String cookie, String nomEdt) {
		String requete = "SELECT u FROM Utilisateur u WHERE u.cookie = :cookie";
        TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
        query.setParameter("cookie", cookie);
        query.setMaxResults(1);

        // Execute the query and get the result list
        Collection<Utilisateur> utilisateur = query.getResultList();
        String res = null;
        Utilisateur u = new Utilisateur();
        if (utilisateur.isEmpty() || utilisateur.size() > 1) {
        	return res;
        }else {
        	for (Utilisateur u2 : utilisateur) {
				u = u2;
			}
        }
		String[] codes = generateRandomCodes();
		
		
		String requete2 = "SELECT e FROM Edt e WHERE e.codeAdmin = :codeAdmin OR e.codeEtu = :codeEtu OR e.codeProf = :codeProf";
		TypedQuery<Edt> query2 = em.createQuery(requete2, Edt.class);
        query2.setParameter("codeAdmin", codes[0]);
        query2.setParameter("codeEtu", codes[1]);
        query2.setParameter("codeProf", codes[2]);
		
		while (!query2.getResultList().isEmpty()) {
			codes = generateRandomCodes();
			requete2 = "SELECT e FROM Edt e WHERE e.codeAdmin = :codeAdmin OR e.codeEtu = :codeEtu OR e.codeProf = :codeProf";
			query2 = em.createQuery(requete, Edt.class);
	        query2.setParameter("codeAdmin", codes[0]);
	        query2.setParameter("codeEtu", codes[1]);
	        query2.setParameter("codeProf", codes[2]);
		}
		
		Edt edt = new Edt();
		edt.setCodeAdmin(codes[0]);
		edt.setCodeEtu(codes[1]);
		edt.setCodeProf(codes[2]);
		edt.setNom(nomEdt);
		
		em.persist(edt);
		
		Collection<Edt> edts = u.getEdts_admin();
		edts.add(edt);
		String EDT = "0" + "," + edt.getNom() + "," + edt.getCodeAdmin() + "," + edt.getCodeProf() + "," + edt.getCodeEtu();
		return EDT;
	}
	
	public String linkEDT(String cookie, String codeEdt, String numLink) {
		String requete = "SELECT u FROM Utilisateur u WHERE u.cookie = :cookie";
        TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
        query.setParameter("cookie", cookie);
        query.setMaxResults(1);

        // Execute the query and get the result list
        Collection<Utilisateur> utilisateur = query.getResultList();
        String res = null;
        Utilisateur u = new Utilisateur();
        if (utilisateur.isEmpty() || utilisateur.size() > 1) {
        	return res;
        }else {
        	for (Utilisateur u2 : utilisateur) {
        		//System.out.println("###################################################");
        		//System.out.println(u2.getMail());
				u = u2;
			}
        }
        
        String requete2 = "SELECT e FROM Edt e WHERE e.codeEtu = :codeEdt";
        TypedQuery<Edt> query2 = em.createQuery(requete2, Edt.class);
        query2.setParameter("codeEdt", codeEdt);
        query2.setMaxResults(1);

        // Execute the query and get the result list
        Collection<Edt> edt = query2.getResultList();
        Edt e = new Edt();
        if (edt.isEmpty() || edt.size() > 1) {
        	return res;
        }else {
        	for (Edt e2 : edt) {
        		//System.out.println("###################################################");
        		//System.out.println(e2.getNom());
				e = e2;
			}
        }
        Collection<LinkUtilEDT> links = e.getLiens_utilisateur();
        boolean found = false;
        boolean isProf = false;
        for (LinkUtilEDT link : links) {
        	//System.out.println("###################################################");
    		//System.out.println(link.getNumero());
			if (link.getNumero().equals(numLink)) {
				link.setUtilisateur(u);
				isProf = link.getIsProf();
				found = true;
				break;
			}
		}
        
        if (!found) {
        	return res;
        }
		
		String EDT = "1" + "," + e.getNom() + ",";
		if(isProf) {
			EDT += e.getCodeProf();
		}else {
			EDT += e.getCodeEtu();
		}
		EDT += "," + numLink;
		
		return EDT;
	}
	
	/*public List<LocalDateTime> getLundiVendrediCourant() {
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
	
	public Collection<Cours> getCoursSemaineV0(String mail_utilisateur){
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
		
	}*/
	
	public void modif_mdp(String new_mdp, String new_mdp_conf, String cookies_modif) {
        if (new_mdp.equals("new_mdp_conf")) {
	        String jpql = "Select u FROM Utilisateur u WHERE u.cookie = :cookieValue";
	        
	        TypedQuery<Utilisateur> query = em.createQuery(jpql, Utilisateur.class);
	        query.setParameter("cookieValue", cookies_modif);

	        Utilisateur u = query.getSingleResult();
	        u.setMdp(new_mdp);
	        
        }
    }

    public void modif_profile(String new_prenom, String new_nom, String new_email, String cookies_modif) {
        String jpql = "Select u FROM Utilisateur u WHERE u.cookie = :cookieValue";
        TypedQuery<Utilisateur> query = em.createQuery(jpql, Utilisateur.class);
        query.setParameter("cookieValue", cookies_modif);
        
        Utilisateur u = query.getSingleResult();
        u.setMail(new_email);
        u.setNom(new_nom);
        u.setPrenom(new_prenom);
        
    }
	
	public List<LocalDateTime> getLundiVendredi(String lundiSemaine) {
		String[] lundiDate = lundiSemaine.split(",");
		LocalDate lundi = LocalDate.of(Integer.parseInt(lundiDate[2]), Integer.parseInt(lundiDate[1]), Integer.parseInt(lundiDate[0]));
		LocalDate vendredi = lundi.plusDays(4);
        
        LocalTime matin = LocalTime.of(1, 0, 0);
        LocalDateTime lundiMatin = LocalDateTime.of(lundi, matin);
        
        LocalTime soir = LocalTime.of(23, 0, 0);
        LocalDateTime vendrediSoir = LocalDateTime.of(vendredi, soir);
		List<LocalDateTime> lundiVendredi = new ArrayList<>();
		lundiVendredi.add(lundiMatin);
		lundiVendredi.add(vendrediSoir);
		return lundiVendredi;
		
	}
	
	public String getCoursSemaine(String cookie, String lundiSemaine){
		List<LocalDateTime> lundiVendredi = getLundiVendredi(lundiSemaine);
		LocalDateTime lundi = lundiVendredi.get(0);
		LocalDateTime vendredi = lundiVendredi.get(1);
		String coursSemaine = "";
		
		String requete = "SELECT u FROM Utilisateur u WHERE u.cookie = :cookie";
        TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
        query.setParameter("cookie", cookie);
        query.setMaxResults(1);
        Utilisateur utilisateur = query.getSingleResult();
		
		Collection<LinkUtilEDT> links = utilisateur.getLienEDT();
		for(LinkUtilEDT link : links) {
			Collection<Groupe> groupes = link.getGroupes();
			for (Groupe groupe : groupes) {
				Collection<Cours> coursCourant = groupe.getCours_etude();
				for (Cours cours : coursCourant) {
					if (cours.getDebut().isAfter(lundi) && cours.getFin().isBefore(vendredi)) {
						
						coursSemaine += cours.getMatiere().getCouleur() + "," + cours.getMatiere().getNom() + "," + cours.getType().getNom() + ",";
						for (Salle s : cours.getSalle()) {
							coursSemaine += s.getNom() + "#";
						}
						coursSemaine += ",";
						for (Groupe g : cours.getGroupes()) {
							coursSemaine += g.getNom() + "#";
						}
						coursSemaine += ",";
						for (LinkUtilEDT l : cours.getProf()) {
							coursSemaine += l.getPrenom() + " " + l.getNom() + "#";
						}
						coursSemaine += "," + cours.getDebut().getHour() + "," + cours.getDebut().getMinute();
						coursSemaine += "," + cours.getFin().getHour() + "," + cours.getFin().getMinute();
						coursSemaine += "," + cours.getDebut().getDayOfWeek().getValue() + ";";
					}
				
				}
			}
		}	
		
		return coursSemaine;
		
	}
	
	
	public String getCoursGroupes(String cookie, String lundiSemaine, String gS, String edtS){
		List<LocalDateTime> lundiVendredi = getLundiVendredi(lundiSemaine);
		LocalDateTime lundi = lundiVendredi.get(0);
		LocalDateTime vendredi = lundiVendredi.get(1);
		String coursSemaine = "";
		
		String[] codes = edtS.split(",");
		Edt edt = em.find(Edt.class, codes[0]);
		Collection<Groupe> groupes = new ArrayList<>();
		
		String[] groupesS = gS.split(",");
		for (int i = 0; i < groupesS.length; i++) {
			String gr = groupesS[i];
			Groupe groupe = em.find(Groupe.class, gr);
			if (groupe != null)
				groupes.add(groupe);
		}
		
		
		for (Groupe groupe : groupes) {
			Collection<Cours> coursCourant = groupe.getCours_etude();
			for (Cours cours : coursCourant) {
				if (cours.getDebut().isAfter(lundi) && cours.getFin().isBefore(vendredi)) {
					
					coursSemaine += cours.getMatiere().getCouleur() + "," + cours.getMatiere().getNom() + "," + cours.getType().getNom() + ",";
					for (Salle s : cours.getSalle()) {
						coursSemaine += s.getNom() + "#";
					}
					coursSemaine += ",";
					for (Groupe g : cours.getGroupes()) {
						coursSemaine += g.getNom() + "#";
					}
					coursSemaine += ",";
					for (LinkUtilEDT l : cours.getProf()) {
						coursSemaine += l.getPrenom() + " " + l.getNom() + "#";
					}
					coursSemaine += "," + cours.getDebut().getHour() + "," + cours.getDebut().getMinute();
					coursSemaine += "," + cours.getFin().getHour() + "," + cours.getFin().getMinute();
					coursSemaine += "," + cours.getDebut().getDayOfWeek().getValue() + ";";
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
	
	public String getEDTs(String cookie) {
		String requete = "SELECT u FROM Utilisateur u WHERE u.cookie = :cookie";
        TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
        query.setParameter("cookie", cookie);
        query.setMaxResults(1);

        // Execute the query and get the result list
        Utilisateur u = query.getSingleResult();
    	String EDTs = "";
    	Collection<Edt> edtsAdmin = u.getEdts_admin();
    	for (Edt edt : edtsAdmin) {
			EDTs += "0" + "," + edt.getNom() + "," + edt.getCodeAdmin() + "," + edt.getCodeProf() + "," + edt.getCodeEtu() + ";";
		}
    	Collection<LinkUtilEDT> edtsLinks = u.getLienEDT();
    	for (LinkUtilEDT linkUtilEDT : edtsLinks) {
    		EDTs += "1" + "," + linkUtilEDT.getEdt().getNom() + ",";
    		
			if (linkUtilEDT.getIsProf()) {
				EDTs += linkUtilEDT.getEdt().getCodeProf() + ",";
			}else {
				EDTs += linkUtilEDT.getEdt().getCodeEtu() + ",";
			}
			EDTs += linkUtilEDT.getNumero() + ";";
		}
    	return EDTs;
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
		

		String requete = "SELECT c FROM Cours c WHERE c.edt_associe = :edtAssocie AND :groupe MEMBER OF c.groupes AND (c.debut < :nouvFin AND :nouvDebut < c.fin)";
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

        String requete = "SELECT c FROM Cours c WHERE c.edt_associe = :edtAssocie AND :prof MEMBER OF c.prof AND (c.debut < :nouvFin AND :nouvDebut < c.fin)";
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
		

        String requete = "SELECT c FROM Cours c WHERE c.edt_associe = :edtAssocie AND :salle MEMBER OF c.salle AND (c.debut < :nouvFin AND :nouvDebut < c.fin)";
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
			String[] prof = prof_string.split(" ");

			
			LinkUtilEDT p = em.find(LinkUtilEDT.class, prof[0]);
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
			String[] prof = prof_string.split(" ");
			LinkUtilEDT p = em.find(LinkUtilEDT.class, prof[0]);
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
        
    public void ajout_etudiant(String numero,String prenom,String nom, String edt, String groupes) {
    	//on crée l'étudiant
    	LinkUtilEDT etudiant = new LinkUtilEDT();
    	etudiant.setNumero(numero);
    	etudiant.setPrenom(prenom);
    	etudiant.setNom(nom);
    	etudiant.setIsProf(false);
    	Collection<Groupe> grEtu = new ArrayList<>();
    	String[] groupesS = groupes.split(",");
    	for(String gS : groupesS) {
    		Groupe g = em.find(Groupe.class, gS);
    		if (g != null) {
    			grEtu.add(g);
    		}
    	}
    	etudiant.setGroupes(grEtu);
    	
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
    
    public String generateColor() {
    	int colorMax = 610;
    	int colorMin = 150;
    	int colorI = random.nextInt(colorMax - colorMin + 1) + colorMin;
    	int[] colors = new int[3];
    	
    	colors[0] = random.nextInt(256);
    	colorMin -= colors[0];
    	colorMax -= colors[0];
    	colors[1] = random.nextInt(256);
    	colorMin -= colors[1];
    	colorMax -= colors[1];
    	
    	int max = Math.min(255, colorMax);
    	int min = Math.max(0, colorMin);
    	colors[2] = random.nextInt(max - min + 1) + min;
    	
    	for (int i = colors.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = colors[i];

            colors[i] = colors[index];
            colors[index] = temp;
        }
    	
    	String color = colors[0] + "," + colors[1] + "," + colors[2];
    	
    	return color;
    }
    
    public void ajout_matiere(String nom, String edt) {
    	//on crée le prof
    	Matiere matiere = new Matiere();
    	matiere.setNom(nom);
    	String couleur = generateColor();
    	String requete = "SELECT m FROM Matiere m WHERE m.couleur = :couleur";
        TypedQuery<Matiere> query = em.createQuery(requete, Matiere.class);
        query.setParameter("couleur", couleur);
        query.setMaxResults(1);

        // Execute the query and get the result list
        Collection<Matiere> mat = query.getResultList();
        while (!mat.isEmpty()) {
        	couleur = generateColor();
        	requete = "SELECT m FROM Matiere m WHERE m.couleur = :couleur";
            query = em.createQuery(requete, Matiere.class);
            query.setParameter("couleur", couleur);
            query.setMaxResults(1);

            // Execute the query and get the result list
            mat = query.getResultList();
        }
    	
        matiere.setCouleur(couleur);
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	matiere.setEdt_associe(edt_associe);
    	
    	em.persist(matiere);
    
    }
    
    public void ajout_type(String nom, String edt) {
    	//on crée le prof
    	Type type = new Type();
    	type.setNom(nom);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	type.setEdt_associe(edt_associe);
    	
    	em.persist(type);
    
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
    
    public void creer_groupe(String nom, String edt) {
    	Groupe groupe = new Groupe();
    	groupe.setNom(nom);
    	
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	groupe.setEdt_associe(edt_associe);
    	
    	/*Collection<LinkUtilEDT> liste_eleves = new ArrayList<LinkUtilEDT>();
    	
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
		}*/
    	
    	em.persist(groupe);
    }
    
    public String getGroupesEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<Groupe> groupes = edt_associe.getGroupes();
    	String res = "";
    	for(Groupe g : groupes) {
    		res += ","+g.getNom();
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
    		res += ","+s.getNom();
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
    
    public String getProfsEDT(String edt) {
    	String[] codes = edt.split(",");
    	Edt edt_associe = em.find(Edt.class, codes[0]);
    	Collection<LinkUtilEDT> all_links = edt_associe.getLiens_utilisateur();
    	String res = "";
    	for(LinkUtilEDT l : all_links) {
    		if (l.getIsProf()) // keep only links that are teachers
    			res += ","+l.getNumero()+" "+l.getPrenom()+" "+l.getNom();
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
    		res += ","+m.getNom();
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
    		res += ","+t.getNom();
    	}
    	if (res != "") {
    		res = res.substring(1);
    	}
    	return res;
    }
    
    public String getWeek(String lundi) {
    	String[] date = lundi.split(",");
    	LocalDate monday = LocalDate.of(Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0]));
        
        // Stocker les dates de la semaine
        String weekDates = "";
        for (int i = 0; i < 5; i++) {
        	weekDates += monday.getDayOfMonth() + "/" + monday.getMonthValue() + ";";
            monday = monday.plusDays(1);
        }
        return weekDates;
        
    }
    
    public String getAllMondaysYear() {
    	LocalDate today = LocalDate.now();
    	LocalDate fin = LocalDate.of(today.getYear(), 8, 28);
    	LocalDate debut;
    	if (today.isBefore(fin)) {
    		debut = LocalDate.of(today.minusYears(1).getYear(), 8, 28);
    	}else {
    		debut = fin;
    		fin = LocalDate.of(today.plusYears(1).getYear(), 8, 28);
    	}
        LocalDate monday = debut.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        
        LocalDate lundiThisWeek = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
    	
        // Stocker les dates de la semaine
        String lundis = ""+lundiThisWeek.getDayOfMonth()+"/"+lundiThisWeek.getMonthValue()+"/"+lundiThisWeek.getYear()+":";
        while (monday.isBefore(fin)) {
        	lundis += monday.getDayOfMonth() + "/" + monday.getMonthValue() + "/" + monday.getYear() + ";";
        	monday = monday.plusDays(7);
		}
        return lundis;
        
    }
}
    