package com.sabrina.configuration;

public class Conf {

    // Usa variabili d'ambiente per Railway, con fallback per sviluppo locale
    private static final String dbUrl = System.getenv("DATABASE_URL") != null 
        ? System.getenv("DATABASE_URL") 
        : "jdbc:postgresql://localhost:5432/cinema";
    
    private static final String username = System.getenv("DB_USERNAME") != null 
        ? System.getenv("DB_USERNAME") 
        : "postgres";
    
    private static final String password = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : "Purosangue90!";

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getConnectionString() {
        return dbUrl;
    }
    
    // Metodi di compatibilità per il codice esistente
    public static String getDbhost() {
        if (dbUrl.contains("localhost")) {
            return "localhost";
        }
        // Estrae l'host dall'URL del database
        try {
            String[] parts = dbUrl.split("//")[1].split("/")[0].split(":");
            return parts[0];
        } catch (Exception e) {
            return "localhost";
        }
    }

    public static String getDbport() {
        if (dbUrl.contains("localhost")) {
            return "5432";
        }
        // Estrae la porta dall'URL del database
        try {
            String[] parts = dbUrl.split("//")[1].split("/")[0].split(":");
            return parts.length > 1 ? parts[1] : "5432";
        } catch (Exception e) {
            return "5432";
        }
    }

    public static String getDbname() {
        if (dbUrl.contains("localhost")) {
            return "cinema";
        }
        // Estrae il nome del database dall'URL
        try {
            String[] parts = dbUrl.split("/");
            String dbWithParams = parts[parts.length - 1];
            return dbWithParams.split("\\?")[0]; // Rimuove i parametri se presenti
        } catch (Exception e) {
            return "cinema";
        }
    }
}