package com.sabrina.configuration;

public class Conf {

    private static final String dbhost = "localhost";
    private static final String dbport = "5432";
    private static final String dbname = "cinema";
    private static final String username = "postgres"; // <-- Cambia se usi un altro utente
    private static final String password = "Purosangue90!"; // <-- Inserisci la password corretta

    public static String getDbhost() {
        return dbhost;
    }

    public static String getDbport() {
        return dbport;
    }

    public static String getDbname() {
        return dbname;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getConnectionString() {
        return "jdbc:postgresql://" + dbhost + ":" + dbport + "/" + dbname;
    }
}
