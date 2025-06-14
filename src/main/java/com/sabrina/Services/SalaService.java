package com.sabrina.Services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sabrina.Models.Sala;
import com.sabrina.configuration.Conf;

@Service
public class SalaService {

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    private Connection getConnection() {
        try {
        	Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(Conf.getConnectionString(), Conf.getUsername(), Conf.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Sala> list() {
        List<Sala> list = new ArrayList<>();
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM sale");

            while (rs.next()) {
                Sala sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setNomeSala(rs.getString("nomeSala"));
                sala.setId_film(rs.getInt("id_film"));
                list.add(sala);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Sala getById(int id) {
        Sala sala = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM sale WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sala = new Sala();
                sala.setId(rs.getInt("id"));
                sala.setNomeSala(rs.getString("nomeSala"));
                sala.setId_film(rs.getInt("id_film"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sala;
    }
    
    
    public Sala getSalaByFilmId(int id_film) {
        Sala sala = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM sale WHERE id_film = ?");
        ) {
            pstmt.setInt(1, id_film);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    sala = new Sala();
                    sala.setId(rs.getInt("id"));
                    sala.setNomeSala(rs.getString("nomeSala"));
                    sala.setId_film(rs.getInt("id_film"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sala;
    }

    
    
}