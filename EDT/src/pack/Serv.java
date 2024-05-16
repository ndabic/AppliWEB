package pack;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				response.getWriter().append("Found you !").append(request.getContextPath());
				request.getRequestDispatcher("Serv?op=afficherEDT").forward(request, response);
			}else {
				request.getRequestDispatcher("connexion.html").forward(request, response);
			}
			break;
			
		case "afficherEDT":
			String mail_EDT = request.getParameter("mail");
			Collection<Cours> lCours = facade.getCoursSemaine(mail_EDT);
			request.setAttribute("lCours", lCours);
			request.getRequestDispatcher("schedule.jsp").forward(request, response);
			break;
			
		case "addCours":
			response.setContentType("text/html");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();
	        out.print("hello this is a test");
	        out.flush();
	        break;
			
	
		case "addCoursqsjd":
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
	
			facade.ajout_cours(heureDebut, minuteDebut, heureFin, minuteFin, 
                    jour, mois, annee, typeCours, matiereCours, 
                    sallesCours, profsCours, groupesCours, infosSuppCours);
			break;
		
		case "addEtudiant":
			String numero = request.getParameter("num-link");
	        String prenom = request.getParameter("prenom-link");
	        String nom = request.getParameter("nom-link");
	        facade.ajout_etudiant(numero,prenom, nom);
	        break;
	        
		case "addProfesseur":
			String numero_p = request.getParameter("num-link");
	        String prenom_p = request.getParameter("prenom-link");
	        String nom_p = request.getParameter("nom-link");
	        facade.ajout_prof(numero_p,prenom_p, nom_p);
	        break;
	        
		case "addGroupe":
            String nomGroupe = request.getParameter("nom-groupe");
            String fileGroupe = request.getParameter("file-groupe");
            facade.creer_groupe(nomGroupe,fileGroupe);
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
