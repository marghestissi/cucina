package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.DAOException;
import service.UtenteService;
import service.UtenteServiceImpl;
import validator.ErroreValidazione;
import validator.Validatore;

@WebServlet("/loginUtente")
public class LoginUtenteService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// invocazione al validatore per il controllo dei campi
		// invio alla jsp i mesaggi di errore
		List<ErroreValidazione> lista = Validatore.validazioneLogin(request);
		if (lista.size() != 0) {
			request.setAttribute("lista", lista);
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/loginUtente.jsp").forward(request, response);
		}

		UtenteService serviceU;
		try {
			serviceU = new UtenteServiceImpl();

			String idUtente = request.getParameter("idUtente");
			String password = request.getParameter("password");

			Utente u = serviceU.checkCredenziali(idUtente, password);

			if (u != null) {

				HttpSession session = request.getSession(false);

				if (session != null) {
					session.setAttribute("user", u);
				}
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response); // SE LA
																												// LOGIN
																												// FUNZIONA;
																												// LINK
																												// A
																												// home.jsp

			} else {

				request.setAttribute("errore", "Login failed");
				getServletContext().getRequestDispatcher("/WEB-INF/jsp/erroreGenerico.jsp").forward(request, response);
			}

		} catch (DAOException | ConnessioneException e) {
			e.printStackTrace();
			request.setAttribute("errore", e);
			getServletContext().getRequestDispatcher("/WEB-INF/jsp/erroreGenerico.jsp").forward(request, response);
		}

	}
}
