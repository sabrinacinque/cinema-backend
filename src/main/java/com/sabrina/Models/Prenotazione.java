package com.sabrina.Models;

import java.sql.Date;

public class Prenotazione {

	private int id;
	private String nomeUtente;
	private String cognomeUtente;
	private String emailUtente;
	private int id_posto;
	private Date data;
	
	
	public Prenotazione() {
		this.id = 0;
		this.nomeUtente = null;
		this.cognomeUtente = null;
		this.emailUtente = null;
		this.id_posto = 0;
		this.data = null;
		
	}
	
	public Prenotazione(int id,String nomeUtente,String cognomeUtente,String emailUtente,int id_posto,Date data) {
		this.id = id;
		this.nomeUtente = nomeUtente;
		this.cognomeUtente = cognomeUtente;
		this.emailUtente = emailUtente;
		this.id_posto = id_posto;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	public String getCognomeUtente() {
		return cognomeUtente;
	}

	public void setCognomeUtente(String cognomeUtente) {
		this.cognomeUtente = cognomeUtente;
	}

	public String getEmailUtente() {
		return emailUtente;
	}

	public void setEmailUtente(String emailUtente) {
		this.emailUtente = emailUtente;
	}

	public int getId_posto() {
		return id_posto;
	}

	public void setId_posto(int id_posto) {
		this.id_posto = id_posto;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
