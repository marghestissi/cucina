package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Categoria;
import entity.Utente;
import exceptions.ConnessioneException;

public class CategoriaDAOImpl implements CategoriaDAO {

	private Connection conn;

	public CategoriaDAOImpl() throws ConnessioneException{
		conn = SingletonConnection.getInstance();
	}
	
	/*
	 * inserimento di una nuova categoria
	 * 
	 */
	@Override
	public void insert(String descrizione) throws SQLException {
		
		PreparedStatement ps=conn.prepareStatement("INSERT INTO categoria(descrizione) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
		String idCategoria = "";		
		ps.setString(1, descrizione);		
		
		ResultSet rs = ps.getGeneratedKeys();
        int newIdCategoria = -1;
        if (rs.next()) {
            newIdCategoria = (int) rs.getInt(1);
            idCategoria = String.valueOf(newIdCategoria);
        }
        
        int righe = ps.executeUpdate(); 
        System.out.println("Ho inserito " + righe + " categorie");

		
	}
	/*
	 * modifica del nome di una categoria.
	 * la categoria viene individuata in base al idCategoria
	 * se la categoria non esiste si solleva una eccezione
	 */
	@Override
	public void update(Categoria c) throws SQLException {
		
		PreparedStatement ps=conn.prepareStatement("UPDATE categoria SET descrizione=? WHERE id_categoria=?");
		ps.setString(1, c.getDescrizione());
		ps.setInt(2, c.getIdCategoria());
		int n = ps.executeUpdate();
		System.out.println("Ho aggiornato " + n + " categorie");
		
		if(n==0)
			throw new SQLException("categoria: " + c.getIdCategoria() + " non presente");


		
	}

	/*
	 * cancellazione di una singola categoria
	 * una categoria si può cancellare solo se non ci sono dati correlati
	 * se la categoria non esiste o non è cancellabile si solleva una eccezione
	 */
	@Override
	public void delete(int idCategoria) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("DELETE FROM categoria WHERE id_categoria=?");
		ps.setInt(1, idCategoria);
		int n = ps.executeUpdate();
		System.out.println("Ho eliminato " + n + " categorie");
		if(n==0)
			throw new SQLException("Categoria " + idCategoria + " non presente");

		
	}

	/*
	 * lettura di una singola categoria in base al suo id
	 * se la categoria non esiste si solleva una eccezione
	 */
	@Override
	public Categoria select(int idCategoria) throws SQLException {
		PreparedStatement ps=conn.prepareStatement("SELECT * FROM categoria where id_categoria =?");
		
		ps.setInt(1, idCategoria);
		ResultSet rs = ps.executeQuery();
		Categoria c =null;
		if(rs.next()){
			idCategoria = rs.getInt("id_categoria");
			String descrizione= rs.getString("descrizione");
		
		
		c= new Categoria(idCategoria, descrizione);

		return c;
		
		}
		else
			throw new SQLException("categoria: " + idCategoria + " non presente");

}
	
	/*
	 * lettura di tutte le categorie
	 * se non vi sono categoria il metodo ritorna una lista vuota
	 */
	@Override
	public ArrayList<Categoria> select() throws SQLException {
		ArrayList<Categoria> categorie = new ArrayList<>();
		PreparedStatement ps=conn.prepareStatement("SELECT * FROM categoria");
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			int idCategoria = rs.getInt("id_categoria");
			String descrizione= rs.getString("descrizione");
			

			Categoria c = new Categoria(idCategoria, descrizione);
			categorie.add(c);
		}

		return categorie;
	}
	

}