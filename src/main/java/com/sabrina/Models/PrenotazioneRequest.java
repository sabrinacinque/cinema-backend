package com.sabrina.Models;

import java.util.List;

public class PrenotazioneRequest {
    private String nomeUtente;
    private String cognomeUtente;
    private String emailUtente;
    private List<Integer> idPosti;

    // getters / setters
    public String getNomeUtente() { return nomeUtente; }
    public void setNomeUtente(String nomeUtente) { this.nomeUtente = nomeUtente; }
    public String getCognomeUtente() { return cognomeUtente; }
    public void setCognomeUtente(String cognomeUtente) { this.cognomeUtente = cognomeUtente; }
    public String getEmailUtente() { return emailUtente; }
    public void setEmailUtente(String emailUtente) { this.emailUtente = emailUtente; }
    public List<Integer> getIdPosti() { return idPosti; }
    public void setIdPosti(List<Integer> idPosti) { this.idPosti = idPosti; }
}
