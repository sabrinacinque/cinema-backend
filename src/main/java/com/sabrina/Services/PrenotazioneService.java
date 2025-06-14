package com.sabrina.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sabrina.Models.Prenotazione;
import com.sabrina.configuration.Conf;

@Service
public class PrenotazioneService {

	private final JavaMailSender mailSender;

	@Value("${FRONTEND_URL:http://localhost:4200}")
	private String frontendBase;

	public PrenotazioneService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// ------------------------------------------------------------------------
	// Connessione al database
	// ------------------------------------------------------------------------
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(Conf.getConnectionString(), Conf.getUsername(), Conf.getPassword());
	}

	// ------------------------------------------------------------------------
	// 1) LIST ALL
	// ------------------------------------------------------------------------
	public List<Prenotazione> list() {
		List<Prenotazione> lista = new ArrayList<>();
		String sql = "SELECT * FROM prenotazioni ORDER BY data DESC";
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				Prenotazione p = new Prenotazione();
				p.setId(rs.getInt("id"));
				p.setNomeUtente(rs.getString("nomeUtente"));
				p.setCognomeUtente(rs.getString("cognomeUtente"));
				p.setEmailUtente(rs.getString("emailUtente"));
				p.setId_posto(rs.getInt("id_posto"));
				p.setData(rs.getDate("data"));
				lista.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	// ------------------------------------------------------------------------
	// 2) GET BY ID
	// ------------------------------------------------------------------------
	public Prenotazione getById(int id) {
		Prenotazione p = null;
		String sql = "SELECT * FROM prenotazioni WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					p = new Prenotazione();
					p.setId(rs.getInt("id"));
					p.setNomeUtente(rs.getString("nomeUtente"));
					p.setCognomeUtente(rs.getString("cognomeUtente"));
					p.setEmailUtente(rs.getString("emailUtente"));
					p.setId_posto(rs.getInt("id_posto"));
					p.setData(rs.getDate("data"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	// ------------------------------------------------------------------------
	// 3) BATCH INSERT + SINGLE HTML EMAIL WITH CANCEL LINKS
	// ------------------------------------------------------------------------
	@Transactional
	public List<Integer> insertPrenotazioniBatch(String nomeUtente, String cognomeUtente, String emailUtente,
			List<Integer> idPosti) throws Exception {
		// 1) Genero un UUID unico per questa prenotazione
		String reservationCode = UUID.randomUUID().toString();

		String insertSql = "INSERT INTO prenotazioni "
				+ "(nomeUtente, cognomeUtente, emailUtente, id_posto, data, reservation_code) "
				+ "VALUES (?, ?, ?, ?, NOW(), ?)";
		String updateSql = "UPDATE posti SET disponibile = false WHERE id = ? AND disponibile = true";

		List<Integer> newIds = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
			conn.setAutoCommit(false);

			// 2) Ciclo inserimenti + update disponibilità
			for (Integer idPosto : idPosti) {
				insertStmt.setString(1, nomeUtente);
				insertStmt.setString(2, cognomeUtente);
				insertStmt.setString(3, emailUtente);
				insertStmt.setInt(4, idPosto);
				insertStmt.setObject(5, UUID.fromString(reservationCode));
				insertStmt.executeUpdate();

				try (ResultSet keys = insertStmt.getGeneratedKeys()) {
					if (keys.next())
						newIds.add(keys.getInt(1));
				}

				updateStmt.setInt(1, idPosto);
				if (updateStmt.executeUpdate() == 0) {
					throw new SQLException("Posto già occupato: " + idPosto);
				}
			}

			conn.commit();

			// 3) Invio UNICA email di conferma con logo e doppi link
			sendBatchConfirmationEmail(nomeUtente, cognomeUtente, emailUtente, idPosti, newIds, reservationCode);

			return newIds;
		}
	}

	private void sendBatchConfirmationEmail(String nomeUtente, String cognomeUtente, String emailUtente,
	        List<Integer> idPosti, List<Integer> newIds, String reservationCode) throws Exception {
	    // 1) Costruisco l'elenco <li> con link di cancellazione singola
	    StringBuilder elenco = new StringBuilder();
	    for (int i = 0; i < idPosti.size(); i++) {
	        String[] det = getDettagliPrenotazione(idPosti.get(i));
	        String codice = det[0], sala = det[1], film = det[2];
	        String linkSingolo = frontendBase + "/elimina-prenotazione/" + newIds.get(i);
	        elenco.append(String.format("<li>Posto <b>%s</b> in sala <b>%s</b> per <i>%s</i> – "
	                + "<a href=\"%s\">Annulla questo posto</a></li>", codice, sala, film, linkSingolo));
	    }

	    // 2) Corpo HTML completo
	    String html = """
	            <html>
	              <body>
	                <div style="text-align:center;">
	                   <img src='cid:logoCinema' alt='Logo Cinema' style='width:150px;margin-bottom:20px;'/><br/>
	                </div>
	                <p>Ciao <b>%s %s</b>,</p>
	                <p>Grazie per aver scelto il nostro cinema!</p>
	                <p>Ecco il riepilogo della tua prenotazione:</p>
	                <ul>
	                  %s
	                </ul>
	                <p>
	                  Per annullare <strong>tutta</strong> la prenotazione,
	                  <a href="%s/elimina-prenotazione-completa/%s">clicca qui</a>.
	                </p>
	                <p>Buona visione!</p>
	              </body>
	            </html>
	            """.formatted(nomeUtente, cognomeUtente, elenco.toString(), frontendBase, reservationCode);

	    // 3) Costruisco la mail e incollo il logo inline
	    MimeMessage msg = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

	    helper.setTo(emailUtente);
	    helper.setSubject("Conferma prenotazione Cinemando");
	    String plainText = "Hai prenotato i tuoi posti. Controlla il client email per i dettagli.";
	    helper.setText(plainText, html);


	 // Aggiungi l'immagine inline
	 
	    try {
	        ClassPathResource logo = new ClassPathResource("logoGrande.png");
	        
	        if (logo.exists()) {
	            helper.addInline("logoCinema", logo, "image/png");
	            System.out.println("Logo aggiunto da classpath!");
	        } else {
	            // Fallback per sviluppo locale
	            java.io.File logoFile = new java.io.File("src/main/resources/logoGrande.png");
	            if (logoFile.exists()) {
	                helper.addInline("logoCinema", new org.springframework.core.io.FileSystemResource(logoFile), "image/png");
	                System.out.println("Logo aggiunto da filesystem!");
	            } else {
	                System.err.println("Logo non trovato");
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Errore durante l'aggiunta del logo: " + e.getMessage());
	        e.printStackTrace();
	    }

	    mailSender.send(msg);
	}

	@Transactional
	public void annullaPrenotazioneCompleta(String reservationCode) throws Exception {
		// 1) recupera tutti gli id_posto coinvolti
		String selectSql = "SELECT id_posto FROM prenotazioni WHERE reservation_code = ?";
		List<Integer> posti = new ArrayList<>();
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(selectSql)) {
			ps.setObject(1, UUID.fromString(reservationCode));
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					posti.add(rs.getInt("id_posto"));
				}
			}
		}

		// 2) ripristina disponibilità
		String updateSql = "UPDATE posti SET disponibile = true WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement psUpd = conn.prepareStatement(updateSql)) {
			for (int postoId : posti) {
				psUpd.setInt(1, postoId);
				psUpd.executeUpdate();
			}
		}

		// 3) elimina tutte le righe in prenotazioni
		String deleteSql = "DELETE FROM prenotazioni WHERE reservation_code = ?";
		try (Connection conn = getConnection(); PreparedStatement psDel = conn.prepareStatement(deleteSql)) {
			psDel.setObject(1, UUID.fromString(reservationCode));
			psDel.executeUpdate();
		}
	}

	// ------------------------------------------------------------------------
	// 4) GET SINGLE POSTO DETAILS
	// ------------------------------------------------------------------------
	public String[] getDettagliPrenotazione(int id_posto) {
		String[] result = new String[3];
		String sql = "SELECT p.codice AS codice, s.nomeSala AS nomeSala, f.titolo AS titolo " + "FROM posti p "
				+ " JOIN sale s ON p.id_sala = s.id " + " JOIN film f ON s.id_film = f.id " + "WHERE p.id = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id_posto);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result[0] = rs.getString("codice");
					result[1] = rs.getString("nomeSala");
					result[2] = rs.getString("titolo");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ------------------------------------------------------------------------
	// 5) DELETE + RESTORE POSTO
	// ------------------------------------------------------------------------
	public Prenotazione delete(int id) {
		Prenotazione p = getById(id);
		if (p == null)
			return null;

		String updSql = "UPDATE posti SET disponibile = TRUE WHERE id = ?";
		String delSql = "DELETE FROM prenotazioni WHERE id = ?";

		try (Connection conn = getConnection();
				PreparedStatement updStmt = conn.prepareStatement(updSql);
				PreparedStatement delStmt = conn.prepareStatement(delSql)) {
			conn.setAutoCommit(false);

			updStmt.setInt(1, p.getId_posto());
			updStmt.executeUpdate();

			delStmt.setInt(1, id);
			delStmt.executeUpdate();

			conn.commit();
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
