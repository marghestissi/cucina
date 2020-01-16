package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Feedback;
import entity.Utente;
import exceptions.ConnessioneException;

public class FeedBackDAOImpl implements FeedbackDAO {

	private Connection conn;

	public FeedBackDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}
	
	/*
	 * inserimento di un singolo feedbak relativo ad una edizione di un corso da aprte di un utente
	 * se un utente ha già inserito un feedback per una certa edizione si solleva una eccezione
	 */
	@Override
	public void insert(Feedback feedback) throws SQLException {
		PreparedStatement ps=conn.prepareStatement("SELECT COUNT(*) FROM FEEDBACK where id_utente=? and id_edizione=?");
		ps.setString(1, feedback.getIdUtente());
		ps.setInt(2, feedback.getIdEdizione());
		
		ResultSet rs = ps.executeQuery();
		
		if(ps.getMaxRows()>1) {
			throw new SQLException("Feedback: " + feedback.getIdFeedback() + " già presente");
		}else {
		
		ps=conn.prepareStatement("INSERT INTO feedback(id_feedback,id_edizione,id_utente,descrizione,voto) VALUES (?,?,?,?,?)");
		
		ps.setInt(1, feedback.getIdFeedback());
		ps.setInt(2, feedback.getIdEdizione());
		ps.setString(3, feedback.getIdUtente());
		ps.setString(4, feedback.getDescrizione());
		ps.setInt(5, feedback.getVoto());
		
		ps.executeUpdate();
	
		}

	}

	/*
	 * modifica di tutti i dati di un singolo feedback
	 * un feedback viene individuato attraverso l'idFeedback
	 * se il feedback non esiste si solleva una eccezione
	 */
	@Override
	public void update(Feedback feedback) throws SQLException {
		PreparedStatement ps=conn.prepareStatement("UPDATE feedback SET descrizione=?, voto=? where id_feedback=?");
		ps.setString(1, feedback.getDescrizione());
		ps.setInt(2, feedback.getVoto());
		ps.setInt(3, feedback.getIdFeedback());
		
		int n = ps.executeUpdate();
		if(n==0)
			throw new SQLException("Feedback: " + feedback.getIdFeedback() + " non presente");


	}

	/*
	 * cancellazione di un feedback
	 * se il feedback non esiste si solleva una eccezione
	 */
	@Override
	public void delete(int idFeedback) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM feedback WHERE id_feedback=?");
		ps.setInt(1, idFeedback);
		int n = ps.executeUpdate();
		if(n==0)
			throw new SQLException("Feedback: " + idFeedback + " non presente");

	}
	
	/*
	 * lettura di un singolo feedback scritto da un utente per una certa edizione 
	 * se il feedback non esiste si solleva una eccezione
	 */
	@Override
	//public Feedback selectSingoloFeedback(int idUtente, int idEdizione) throws SQLException {
	public Feedback selectSingoloFeedback(String idUtente, int idEdizione) throws SQLException {
		PreparedStatement ps=conn.prepareStatement("SELECT descrizione, voto FROM feedback where id_utente=? and id_edizione=? ");
		ps.setString(1, idUtente);
		ps.setInt(2, idEdizione);
		
		ResultSet rs = ps.executeQuery();
		Feedback singoloFeedback = null;
		
		if(rs.next() && idUtente != null && idEdizione != 0) {
			
			
			String descrizione = rs.getString("descrizione");
			int voto = rs.getInt("voto");
			
			singoloFeedback  = new Feedback();
			
			singoloFeedback.setDescrizione(descrizione);
			singoloFeedback.setVoto(voto);
			
			System.out.println("L'utente " + idUtente + " dell'edizione: " + idEdizione + " dice: " + singoloFeedback.getDescrizione() + ", voto: " + singoloFeedback.getVoto());
			return singoloFeedback;
			
		}else {
			throw new SQLException("Feedback non presente");
		}
	
		
	}

	/*
	 * lettura di tutti i feedback di una certa edizione
	 * se non ci sono feedback o l'edizione non esiste si torna una lista vuota
	 */
	@Override
	public ArrayList<Feedback> selectPerEdizione(int idEdizione) throws SQLException {
		ArrayList<Feedback> tuttiFeedback = new ArrayList<Feedback>(); 

		PreparedStatement ps=conn.prepareStatement("SELECT * FROM feedback where id_edizione=?");
		
		ps.setInt(1, idEdizione);

		ResultSet rs = ps.executeQuery();
		
		
		if(rs.next()  && idEdizione!= 0){
			String idUtente = rs.getString("id_utente");
			String descrizione= rs.getString("descrizione");
			int voto= rs.getInt("voto");

			Feedback feedback = new Feedback(idEdizione, idUtente, descrizione, voto);
			tuttiFeedback.add(feedback);
		}
		
		for(Feedback f : tuttiFeedback) {
			System.out.println("Descrizione: " + f.getDescrizione() + ", voto: " +  f.getVoto());
		}
		return tuttiFeedback;
	}

	/*
	 * lettura di tutti i feedback scritti da un certo utente
	 * se non ci sono feedback o l'utente non esiste si torna una lista vuota
	 */
	@Override
	public ArrayList<Feedback> selectPerUtente(String idUtente) throws SQLException {
		ArrayList<Feedback> tuttiFeedback = new ArrayList<Feedback>(); 

		PreparedStatement ps=conn.prepareStatement("SELECT * FROM feedback where id_utente=?");
		
		ps.setString(1, idUtente);

		ResultSet rs = ps.executeQuery();
		
		
		if(rs.next()  && idUtente!= null){
			int idEdizione = rs.getInt("id_edizione");
			String descrizione= rs.getString("descrizione");
			int voto= rs.getInt("voto");

			Feedback feedback = new Feedback(idEdizione, idUtente, descrizione, voto);
			tuttiFeedback.add(feedback);
		}
		
		for(Feedback f : tuttiFeedback) {
			System.out.println("Descrizione: " + f.getDescrizione() + ", voto: " +  f.getVoto());
		}
		
		return tuttiFeedback;
	}

	/*
	 * lettura di tutti i feedback scritti per un certo corso (nota: non edizione ma corso)
	 * se non ci sono feedback o il corso non esiste si torna una lista vuota
	 */
	@Override
	public ArrayList<Feedback> selectFeedbackPerCorso(int idCorso) throws SQLException {
		ArrayList<Feedback> tuttiFeedback = new ArrayList<Feedback>(); 

		PreparedStatement ps=conn.prepareStatement("SELECT * FROM feedback, calendario, catalogo where calendario.id_corso=catalogo.id_corso and calendario.id_edizione=feedback.id_edizione and calendario.id_corso=?");
		
		ps.setInt(1, idCorso);

		ResultSet rs = ps.executeQuery();
		
		
		if(rs.next()  && idCorso!= 0){
			int idEdizione = rs.getInt("id_edizione");
			String idUtente = rs.getString("id_utente");
			String descrizione= rs.getString("descrizione");
			int voto= rs.getInt("voto");

			Feedback feedback = new Feedback(idEdizione, idUtente, descrizione, voto);
			tuttiFeedback.add(feedback);
		}
		
		for(Feedback f : tuttiFeedback) {
			System.out.println("Descrizione: " + f.getDescrizione() + ", voto: " +  f.getVoto());
		}
		
		return tuttiFeedback;
	}

}
