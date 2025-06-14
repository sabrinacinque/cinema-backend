package com.sabrina.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sabrina.Models.Posto;
import com.sabrina.configuration.Conf;

@Service
public class PostoService {

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(Conf.getConnectionString(), Conf.getUsername(), Conf.getPassword());
	}


    public List<Posto> list() {
        List<Posto> list = new ArrayList<>();
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM posti");

            while (rs.next()) {
                Posto p = new Posto();
                p.setId(rs.getInt("id"));
                p.setCodice(rs.getString("codice"));
                p.setDisponibile(rs.getBoolean("disponibile"));
                p.setId_sala(rs.getInt("id_sala"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Posto getById(int id) {
        Posto p = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM posti WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                p = new Posto();
                p.setId(rs.getInt("id"));
                p.setCodice(rs.getString("codice"));
                p.setDisponibile(rs.getBoolean("disponibile"));
                p.setId_sala(rs.getInt("id_sala"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    
    public List<Posto> getPostiByFilmId(int id_film) {
        List<Posto> lista = new ArrayList<>();
        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(
                "SELECT p.* FROM posti p " +
                "JOIN sale s ON p.id_sala = s.id " +
                "WHERE s.id_film = ?"
            );
            pstmt.setInt(1, id_film);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Posto p = new Posto();
                p.setId(rs.getInt("id"));
                p.setCodice(rs.getString("codice"));
                p.setDisponibile(rs.getBoolean("disponibile"));
                p.setId_sala(rs.getInt("id_sala"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }


}
