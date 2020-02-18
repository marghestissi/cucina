package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.base.Array;

import entity.Edizione;
import entity.Utente;
import exceptions.ConnessioneException;
import exceptions.UtenteGiaRegistrato;
import sun.nio.cs.ext.ISCII91;

public class IscrizioneUtenteDAOImpl implements IscrizioneUtenteDAO {

	private Connection conn;

	public IscrizioneUtenteDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}



	/*
	 * iscrizione di un certo utente ad una certa edizione di un corso.
	 * sia l'utente che l'edizione devono già essere stati registrati in precedenza
	 * se l'utente e/o l'edizione non esistono o l'utente è già iscritto a quella edizione si solleva una eccezione
	 */
	@Override
	public void iscriviUtente(int idEdizione, String idUtente) {

		Utente utente=null;
		Edizione edizione=null;
		try {
			
			RegistrazioneUtenteDAOImpl ur = new RegistrazioneUtenteDAOImpl();
			 utente = ur.select(idUtente);
			CalendarioDAOImpl calendario = new CalendarioDAOImpl();
			edizione = calendario.selectEdizione(idEdizione);
			

			
			
			if(utente!=null && edizione !=null) {
				ArrayList<Edizione> edizioniUtente = selectIscrizioniUtente(idUtente);
				for(Edizione ed : edizioniUtente) {
					if(idEdizione==ed.getCodice()) {
						throw new UtenteGiaRegistrato();
					}
					
				}
				//
				PreparedStatement ps= conn.prepareStatement("INSERT INTO `iscritti` (`id_edizione`, `id_utente`) VALUES(?, ?)");
				ps.setInt(1, idEdizione);
				ps.setString(2, idUtente);
				ps.executeUpdate();
			}
			
		} catch (ConnessioneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(UtenteGiaRegistrato e) {
			e.printStackTrace();
		} 
		
	}

	/*
	 * cancellazione di una iscrizione ad una edizione
	 * nota: quando si cancella l'iscrizione, sia l'utente che l'edizione non devono essere cancellati
	 * se l'utente e/o l'edizione non esistono si solleva una eccezione
	 */
	@Override
	public void cancellaIscrizioneUtente(int idEdizione, String idUtente) throws SQLException {
		Utente utente=null;
		Edizione edizione=null;
		RegistrazioneUtenteDAOImpl ur;
		CalendarioDAOImpl calendario;
		
		try {
			ur = new RegistrazioneUtenteDAOImpl();
			calendario = new CalendarioDAOImpl();

			utente = ur.select(idUtente);
			edizione = calendario.selectEdizione(idEdizione);
			
			if(utente!=null && edizione!= null) {
			
				PreparedStatement ps= conn.prepareStatement("delete from iscritti where id_edizione=? AND id_utente=?");
				ps.setInt(1, idEdizione);
				ps.setString(2, idUtente);
			
				//
				ps.executeUpdate();
			}
			
		} catch (ConnessioneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	/*
	 * lettura di tutte le edizioni a cui è iscritto un utente
	 * se l'utente non esiste o non è iscritto a nessuna edizione si torna una lista vuota
	 */
	@Override
	public ArrayList<Edizione> selectIscrizioniUtente(String idUtente) throws SQLException {
		
		ArrayList<Edizione> edizioneUtente= new ArrayList<Edizione>();
		PreparedStatement ps= conn.prepareStatement("select id_edizione from iscritti where id_utente=?");
		ps.setString(1, idUtente);
		
		ResultSet rs = ps.executeQuery();
		CalendarioDAOImpl calendario;
		try {
			calendario = new CalendarioDAOImpl();
			while(rs.next()) {
				
				int i=rs.getInt(1);
				edizioneUtente.add(calendario.selectEdizione(i));
			
			}
		
		} catch (ConnessioneException e) {
	
			e.printStackTrace();
		}
		
		
	
		
		
		return edizioneUtente;
	}
	
	/*
	 * lettura di tutti gli utenti iscritti ad una certa edizione
	 * se l'edizione non esiste o non vi sono utenti iscritti si torna una lista vuota
	 */
	@Override
	public ArrayList<Utente> selectUtentiPerEdizione(int idEdizione) throws SQLException {
		ArrayList<Utente> utenti = new ArrayList<Utente>();
		RegistrazioneUtenteDAOImpl rg;
		try {
			rg = new RegistrazioneUtenteDAOImpl();
			PreparedStatement ps= conn.prepareStatement("select id_utente from iscritti where id_edizione=?");
			ps.setInt(1, idEdizione);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				utenti.add(rg.select(rs.getString(1)));
			}
			
		} catch (ConnessioneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return utenti;
	}

	/*
	 * ritorna il numero di utenti iscritti ad una certa edizione
	 */
	@Override
	public int getNumeroIscritti(int idEdizione) throws SQLException {
		
		int somma=0;
		PreparedStatement ps= conn.prepareStatement("select count(*) from iscritti where id_edizione=?");
		
		ps.setInt(1, idEdizione);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			somma=rs.getInt(1);
			
			
		}
		
		return somma;
	}
	
	public static void main(String[] args) throws ConnessioneException, SQLException {
		IscrizioneUtenteDAOImpl in= new IscrizioneUtenteDAOImpl();

		in.iscriviUtente(93, "Ing_Ruben");

		
		System.out.println(in.selectUtentiPerEdizione(93).toString());
		System.out.println(in.getNumeroIscritti(93));
	}


}
