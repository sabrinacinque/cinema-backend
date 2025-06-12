package com.sabrina.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sabrina.Models.Prenotazione;
import com.sabrina.Models.PrenotazioneRequest;
import com.sabrina.Services.PrenotazioneService;
import com.sabrina.Services.EmailService;

@RestController
@RequestMapping("/prenotazioni")
@CrossOrigin(origins = "*", methods = {
    RequestMethod.GET,
    RequestMethod.POST,
    RequestMethod.DELETE
})
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private EmailService emailService;

    /** 1.a) Lista tutte le prenotazioni */
    @GetMapping("/tutte")
    public List<Prenotazione> getAll() {
        return prenotazioneService.list();
    }

    /** 1.b) Recupera una singola prenotazione per ID */
    @GetMapping("/{id}")
    public ResponseEntity<Prenotazione> getById(@PathVariable int id) {
        Prenotazione p = prenotazioneService.getById(id);
        return p == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(p);
    }

    /** 2) Batch insert + il service invia già la mail */
    @PostMapping("/inserisci")
    public ResponseEntity<String> createBatch(@RequestBody PrenotazioneRequest req) {
        try {
            // chiama il batch che inserisce i posti e invia UNA sola email
            List<Integer> newIds = prenotazioneService.insertPrenotazioniBatch(
                req.getNomeUtente(),
                req.getCognomeUtente(),
                req.getEmailUtente(),
                req.getIdPosti()
            );

            // rispondo con un messaggio semplice (oppure con newIds se ti serve)
            return ResponseEntity.ok("Prenotazione completata con successo!");

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Errore durante la prenotazione. Riprova.");
        }
    }

    /** 3) Cancellazione prenotazione */
    @DeleteMapping("/eliminaprenotazione/{id}")
    public ResponseEntity<Prenotazione> delete(@PathVariable int id) {
        Prenotazione p = prenotazioneService.delete(id);
        return p == null
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(p);
    }
    
    
    @DeleteMapping("/elimina-prenotazione-completa/{reservationCode}")
    public ResponseEntity<Void> deleteComplete(@PathVariable String reservationCode) {
        try {
            prenotazioneService.annullaPrenotazioneCompleta(reservationCode);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
