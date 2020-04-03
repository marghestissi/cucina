package validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;


public class Validatore{

	private static ResourceBundle bundle = ResourceBundle.getBundle("risorse/info");
	
	/*
	 * regole di validazione per la registrazione dell'utente
	 */
	public static List<ErroreValidazione> validazioneUtente(HttpServletRequest request){
		List<ErroreValidazione> lista = new ArrayList<>();
		
		//leggo i parametri da registrazione.jsp
		String idUtente = request.getParameter("idUtente");
		if(idUtente == null || idUtente.length()==0)
			lista.add(new ErroreValidazione("idUtente", "idUtente " + bundle.getString("error.required")));

		String password = request.getParameter("password");
		if(password == null || password.length()==0)
			lista.add(new ErroreValidazione("password", "password " + bundle.getString("error.required")));
		else if(password.length()<8)
				//il controllo viene fatto solo se la password è stata inserita
				lista.add(new ErroreValidazione("password", bundle.getString("error.minlength") + " 8"));
		
		//TODO: continuare con gli eventuali controlli di validità che si ritiene necessari
		String nome = request.getParameter("nome");
		if(nome == null || nome.length()==0)
			lista.add(new ErroreValidazione("nome", "nome " + bundle.getString("error.required")));

		String cognome = request.getParameter("cognome");
		if(cognome == null || cognome.length()==0)
			lista.add(new ErroreValidazione("cognome", "cognome " + bundle.getString("error.required")));

		String anno = request.getParameter("anno");
		if(anno == null || anno.length()==0)
			lista.add(new ErroreValidazione("anno", "anno " + bundle.getString("error.required")));

		String telefono = request.getParameter("telefono");
		if(telefono == null || telefono.length()==0)
			lista.add(new ErroreValidazione("telefono", "telefono " + bundle.getString("error.required")));

		String email = request.getParameter("email");
		if(email == null || email.length()==0)
			lista.add(new ErroreValidazione("email", "email " + bundle.getString("error.required")));

		try {
			long numeroTelefono = Long.parseLong(telefono);
		} catch (Exception e) {
			lista.add(new ErroreValidazione("telefono", "telefono " + bundle.getString("error.type.long")));

		}
		
		
		return lista;
	}

	//TODO: aggiungere tutti i controlli per le diverse form del sito
	public static List<ErroreValidazione> validazioneLogin(HttpServletRequest request){
		List<ErroreValidazione> lista = new ArrayList<>();
		
		//leggo i parametri da registrazione.jsp
		String idUtente = request.getParameter("idUtente");
		if(idUtente == null || idUtente.length()==0)
			lista.add(new ErroreValidazione("idUtente", "idUtente " + bundle.getString("error.required")));

		String password = request.getParameter("password");
		if(password == null || password.length()==0)
			lista.add(new ErroreValidazione("password", "password " + bundle.getString("error.required")));
		else if(password.length()<8)
				//il controllo viene fatto solo se la password è stata inserita
				lista.add(new ErroreValidazione("password", bundle.getString("error.minlength") + " 8"));
		
		
		return lista;
	}
}
