package pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ejb.*;

/**
 * Servlet implementation class Serv
 */
@WebServlet("/Serv")
public class Serv extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
    @EJB
    Facade facade = new Facade();
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Serv() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private void printRequest(HttpServletRequest request) {
    	try (BufferedReader reader = request.getReader()) {
			String requestBody = "";
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody += line+"\n";
            }
            System.out.println(requestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//printRequest(request);
		String cookieUser = null;
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cookie-edt".equals(cookie.getName())) {
                	cookieUser = cookie.getValue();
                    break;
                }
            }
        }
		String op = request.getParameter("op");
		switch (op) {
		case "inscription":
			String mail_insc = request.getParameter("mail");
			
			if (facade.verif_doublon_utilisateur(mail_insc)) {
				String nom_insc = request.getParameter("nom");
				String prenom_insc = request.getParameter("prenom");
				String mdp_insc = request.getParameter("mdp");
				facade.ajout_utilisateur(nom_insc, prenom_insc, mdp_insc, mail_insc);
				request.getRequestDispatcher("connexion.html").forward(request, response);
			}else {
				request.getRequestDispatcher("inscription.html").forward(request, response);
			}
			break;
		
		case "connexion":
			String mail_conn = request.getParameter("mail");
			String mdp_conn = request.getParameter("mdp");
			if (facade.verif_connexion(mail_conn, mdp_conn)) {
				//response.getWriter().append("Found you !").append(request.getContextPath());
				Cookie cookie = new Cookie("cookie-edt", facade.getUserCookie(mail_conn)); // value equals the mail
				cookie.setSecure(true);
			    cookie.setHttpOnly(true);
			    cookie.setPath("/");
			    cookie.setMaxAge(60 * 60); // 1 hour
			    cookie.setComment("SameSite=None; Secure"); // Add SameSite attribute manually
			    response.addCookie(cookie);
				request.getRequestDispatcher("Serv?op=afficherEDT").forward(request, response);
			}else {
				request.getRequestDispatcher("connexion.html").forward(request, response);
			}
			break;
			
		case "getUserInfos":
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outCookie = response.getWriter();
	        if (cookieUser != null) {
	        	String nomPrenom = facade.getUserInfos(cookieUser);
	        	if (nomPrenom != null)
	        		outCookie.print("success:"+nomPrenom);
	        	else
	        		outCookie.print("error: user not found");
	        }else {
	        	outCookie.print("error: wrong cookie");
	        }
	        outCookie.flush();
	        break;
			
		case "afficherEDT":
			String mail_EDT = request.getParameter("mail");
			Collection<Cours> lCours = facade.getCoursSemaine(mail_EDT);
			request.setAttribute("lCours", lCours);
			request.getRequestDispatcher("schedule.jsp").forward(request, response);
			break;
			
		/*case "reponseTypeAJAX":
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();
	        out.print("hello this is another test.");
	        out.flush();
	        break;*/
			
	
		case "addCours":
			String heureDebut = request.getParameter("heure-cours-debut");
	        String minuteDebut = request.getParameter("minute-cours-debut");
	        String heureFin = request.getParameter("heure-cours-fin");
	        String minuteFin = request.getParameter("minute-cours-fin");
	        String jour = request.getParameter("jour-cours");
	        String mois = request.getParameter("mois-cours");
	        String annee = request.getParameter("annee-cours");
	        String typeCours = request.getParameter("type-cours");
	        String matiereCours = request.getParameter("matiere-cours");
	        String sallesCours = request.getParameter("salles-cours");
	        String profsCours = request.getParameter("profs-cours");
	        String groupesCours = request.getParameter("groupes-cours");
	        String infosSuppCours = request.getParameter("infosupp-cours");
	        String edtCours = request.getParameter("edt");
	        
	        String res = facade.verif_cours(heureDebut, minuteDebut, heureFin, minuteFin, 
                    jour, mois, annee, typeCours, matiereCours, 
                    sallesCours, profsCours, groupesCours, infosSuppCours, edtCours);
	        
	        if (res == null) {
	        	facade.ajout_cours(heureDebut, minuteDebut, heureFin, minuteFin, 
                    jour, mois, annee, typeCours, matiereCours, 
                    sallesCours, profsCours, groupesCours, infosSuppCours, edtCours);
	        	res = "displayOK";
	        }
	        response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outCours = response.getWriter();
	        outCours.print(res);
	        outCours.flush();
			break;
		
		case "addEtudiant":
			String numero = request.getParameter("num-link");
	        String prenom = request.getParameter("prenom-link");
	        String nom = request.getParameter("nom-link");
	        String edt = request.getParameter("edt");
	        facade.ajout_etudiant(numero,prenom, nom, edt);
	        break;
	        
		case "addProfesseur":
			String numero_p = request.getParameter("num-link");
	        String prenom_p = request.getParameter("prenom-link");
	        String nom_p = request.getParameter("nom-link");
	        String edt_p = request.getParameter("edt");
	        facade.ajout_prof(numero_p,prenom_p, nom_p, edt_p);
	        break;
	        
		case "addGroupe":
            String nomGroupe = request.getParameter("nom-groupe");
            Part fileGroupe = request.getPart("file-groupe");
            String edtGroupe = request.getParameter("edt");
            facade.creer_groupe(nomGroupe,fileGroupe, edtGroupe);
            break;
            
        case "getGroupesEDT":
			String edt_groupes = request.getParameter("edt");
			String groupes = facade.getGroupesEDT(edt_groupes);
			
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outGroupes = response.getWriter();
	        outGroupes.print(groupes);
	        outGroupes.flush();
	        break;    
	    
        case "getTypesEDT":
			String edt_types = request.getParameter("edt");
			String types = facade.getTypesEDT(edt_types);
			
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outTypes = response.getWriter();
	        outTypes.print(types);
	        outTypes.flush();
	        break;
	        
        case "getMatieresEDT":
			String edt_matieres = request.getParameter("edt");
			String matieres = facade.getMatieresEDT(edt_matieres);
			
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outMatieres = response.getWriter();
	        outMatieres.print(matieres);
	        outMatieres.flush();
	        break;
	        
        case "getProfsEDT":
			String edt_profs = request.getParameter("edt");
			String profs = facade.getProfsEDT(edt_profs);
			
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outProfs = response.getWriter();
	        outProfs.print(profs);
	        outProfs.flush();
	        break;
	    
        case "getSallesEDT":
			String edt_salles = request.getParameter("edt");
			String salles = facade.getSallesEDT(edt_salles);
			
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter outSalles = response.getWriter();
	        outSalles.print(salles);
	        outSalles.flush();
	        break;
	        
        case "adminEDT":
        	String edtToAdmin = request.getParameter("edt");
        	request.setAttribute("edtCodes", edtToAdmin);
			request.getRequestDispatcher("schedule-admin.jsp").forward(request, response);
			break;
        	
            
		default:
			response.getWriter().append("Served at: ").append(request.getContextPath());
			break;
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
