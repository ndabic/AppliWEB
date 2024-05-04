package pack;

import java.io.IOException;
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
