package com.sabrina.Models;

public class Sala {
	
	private int id;	
	private String nomeSala;
	private int id_film;
	
	
	public Sala() {
		this.id = 0;
		this.nomeSala = null;
		this.id_film = 0;
	}
	
	public Sala(int id,String nomeSala,int id_film) {
		this.id = id;
		this.nomeSala = nomeSala;
		this.id_film = id_film;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeSala() {
		return nomeSala;
	}

	public void setNomeSala(String nomeSala) {
		this.nomeSala = nomeSala;
	}

	public int getId_film() {
		return id_film;
	}

	public void setId_film(int id_film) {
		this.id_film = id_film;
	}
	
	
	

}
