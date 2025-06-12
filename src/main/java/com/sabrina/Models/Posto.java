package com.sabrina.Models;

public class Posto {
	
	private int id;
	private String codice;
	private boolean disponibile;
	private int id_sala;
	
	
	public Posto() {
		this.id = 0;
		this.codice = null;
		this.disponibile = false;
		this.id_sala = 0;
	}
	
	
	public Posto(int id,String codice,boolean disponibile,int id_sala) {
		this.id = id;
		this.codice = codice;
		this.disponibile = disponibile;
		this.id_sala = id_sala;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCodice() {
		return codice;
	}


	public void setCodice(String codice) {
		this.codice = codice;
	}


	public boolean isDisponibile() {
		return disponibile;
	}


	public void setDisponibile(boolean disponibile) {
		this.disponibile = disponibile;
	}


	public int getId_sala() {
		return id_sala;
	}


	public void setId_sala(int id_sala) {
		this.id_sala = id_sala;
	}
	
	
	
	

}
