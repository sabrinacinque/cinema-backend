package com.sabrina.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sabrina.Models.Film;
import com.sabrina.configuration.Conf;


@Service
public class FilmService {
	
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	private Connection getConnection()
	{
		
		try {
			
			   Class.forName("org.postgresql.Driver");
			   conn = DriverManager.getConnection(Conf.getConnectionString(), Conf.getUsername(), Conf.getPassword());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public List<Film> list()
	{
		ArrayList<Film> list = new ArrayList<Film>();
		try
		{
			conn = this.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM film";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setTitolo(rs.getString("titolo"));
                film.setRegista(rs.getString("regista"));
                film.setGenere(rs.getString("genere"));
                film.setLocandina(rs.getString("locandina"));
                film.setTrailerlink(rs.getString("trailerlink"));
                film.setRiassunto(rs.getString("riassunto"));
                film.setSfondo(rs.getString("sfondo"));
                list.add(film);
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Film getById(Long id) {
	    Film f = null;
	    try {
	        conn = this.getConnection();
	        pstmt = conn.prepareStatement("SELECT * FROM film WHERE id = ?");
	        pstmt.setLong(1, id);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            f = new Film();
	            f.setId(rs.getInt("id"));
	            f.setTitolo(rs.getString("titolo"));
	            f.setRegista(rs.getString("regista"));
	            f.setGenere(rs.getString("genere"));
	            f.setLocandina(rs.getString("locandina"));
	            f.setTrailerlink(rs.getString("trailerlink"));
                f.setRiassunto(rs.getString("riassunto"));
                f.setSfondo(rs.getString("sfondo"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return f;
	}

	
	
	

}
