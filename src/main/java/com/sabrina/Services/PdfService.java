package com.sabrina.Services;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.sabrina.Models.Film;
import com.sabrina.Models.Posto;
import com.sabrina.Models.Prenotazione;
import com.sabrina.Models.Sala;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generaPdf(Prenotazione prenotazione, Posto posto, Sala sala, Film film) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("🎟️ CONFERMA PRENOTAZIONE").setBold().setFontSize(16));
            document.add(new Paragraph("Nome: " + prenotazione.getNomeUtente()));
            document.add(new Paragraph("Cognome: " + prenotazione.getCognomeUtente()));
            document.add(new Paragraph("Email: " + prenotazione.getEmailUtente()));
            document.add(new Paragraph("Film: " + film.getTitolo()));
            document.add(new Paragraph("Sala: " + sala.getNomeSala()));
            document.add(new Paragraph("Posto: " + posto.getCodice()));
            document.add(new Paragraph("Data: " + prenotazione.getData()));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
