package com.sabrina.Models;

public class Film {
	
	private int id;
	private String titolo;
	private String regista;
	private String genere;
	private String locandina;
	private String trailerlink;
	private String riassunto;
	private String sfondo;
	
	public Film() {
		this.id = 0;
		this.titolo = null;
		this.regista = null;
		this.genere = null;
		this.locandina = null;
		this.trailerlink = null;
		this.riassunto = null;
		this.sfondo = null;
	}
	
	public Film(int id,String titolo,String regista,String genere,String locandina,String trailerlink,String riassunto,String sfondo) {
		this.id = id;
		this.titolo = titolo;
		this.regista = regista;
		this.genere = genere;
		this.locandina = locandina;
		this.trailerlink = trailerlink;
		this.riassunto = riassunto;
		this.sfondo = sfondo;
	}
	
	

	public String getSfondo() {
		return sfondo;
	}

	public void setSfondo(String sfondo) {
		this.sfondo = sfondo;
	}

	public String getTrailerlink() {
		return trailerlink;
	}
	public void setTrailerlink(String trailerlink) {
		this.trailerlink = trailerlink;
	}

	public String getRiassunto() {
		return riassunto;
	}

	public void setRiassunto(String riassunto) {
		this.riassunto = riassunto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getRegista() {
		return regista;
	}

	public void setRegista(String regista) {
		this.regista = regista;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getLocandina() {
		return locandina;
	}

	public void setLocandina(String locandina) {
		this.locandina = locandina;
	}
	
	
	
	
	

}
