package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entity.Utente;
import exceptions.ConnessioneException;

import java.sql.Date;

public class RegistrazioneUtenteDAOImpl implements RegistrazioneUtenteDAO {

	private Connection conn;

	public RegistrazioneUtenteDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}
	
	/*
	 * registrazione di un nuovo utente alla scuola di formazione 
	 * se l'utente già esiste si solleva una eccezione
	 */
	@Override
	public void insert(Utente u) throws SQLException {
		
		try {
		PreparedStatement ps=conn.prepareStatement("INSERT INTO registrati(id_utente, password, nome, cognome, dataNascita, email, telefono) VALUES (?,?,?,?,?,?,?)");
		
		ps.setString(1, u.getIdUtente());
		ps.setString(2, u.getPassword());
		ps.setString(3, u.getNome());
		ps.setString(4, u.getCognome());
		ps.setDate (5, new java.sql.Date(u.getDataNascita().getTime()));
		ps.setString(6, u.getEmail());
		ps.setString(7, u.getTelefono());
		
		ps.executeUpdate();
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Ho inserito: " + u.getIdUtente());
	}

	/*
	 * modifica di tutti i dati di un utente
	 * l'utente viene individuato dal suo idUtente
	 * se l'utente non esiste si solleva una exception
	 */
	@Override
	public void update(Utente u) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("UPDATE registrati SET password=?, nome=?, cognome=?, dataNascita=?, email=?, telefono=? where id_utente=?");
		ps.setString(1, u.getPassword());
		ps.setString(2, u.getNome());
		ps.setString(3, u.getCognome());
		ps.setDate(4, new java.sql.Date(u.getDataNascita().getTime()));
		ps.setString(5, u.getEmail());
		ps.setString(6, u.getTelefono());
		ps.setString(7, u.getIdUtente());
		
		int n = ps.executeUpdate();
		
		if(n==0)
			throw new SQLException("utente: " + u.getIdUtente() + " non presente");
		
	}

	/*
	 * cancellazione di un singolo utente
	 * l'utente si può cancellare solo se non è correlato ad altri dati
	 * se l'utente non esiste o non è cancellabile si solleva una eccezione
	 */
	@Override
	public void delete(String idUtente) throws SQLException {
	
		PreparedStatement ps = conn.prepareStatement("DELETE FROM registrati WHERE id_utente=?");
		ps.setString(1, idUtente);
		int n = ps.executeUpdate();
		if(n==0)
			throw new SQLException("utente " + idUtente + " non presente");
	
	}
	
	/*
	 * lettura di tutti gli utenti registrati
	 * se non ci sono utenti registrati il metodo ritorna una lista vuota
	 */
	@Override
	public ArrayList<Utente> select() throws SQLException {
		
		ArrayList<Utente> registrati = new ArrayList<Utente>();
		
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM registrati");
		
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			String idUtente = rs.getString("id_utente");
			String password = rs.getString("password");
			String nome = rs.getString("nome");
			String cognome = rs.getString("cognome");
			Date dataNascita = rs.getDate("dataNascita");
			String email = rs.getString("email");
			String telefono = rs.getString("telefono");
			
			
			Utente registrato = new Utente(idUtente, password, nome, cognome, dataNascita, email, telefono, false);
			registrati.add(registrato);
			
		}
		
		return registrati;
	
	}
	
	/*
	 * lettura dei dati di un singolo utente
	 * se l'utente non esiste si solleva una eccezione
	 */
	@Override
	public Utente select(String idUtente) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM registrati where id_utente =?");
		
		ps.setString(1, idUtente);
		
		ResultSet rs = ps.executeQuery();
		Utente registrato = null;
		
		if(rs.next()){
			String password= rs.getString("password");
			String nome= rs.getString("nome");
			String cognome= rs.getString("cognome");
			Date dataNascita = rs.getDate("dataNascita");
			String email= rs.getString("email");
			String telefono= rs.getString("telefono");

			registrato = new Utente(idUtente, password, nome, cognome, dataNascita, email, telefono, false);
			return registrato;
		}
		else
			throw new SQLException("utente: " + idUtente + " non presente");
	}
	
	public static void main(String[] args) throws Exception{
		
	}

}
