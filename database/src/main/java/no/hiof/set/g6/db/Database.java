package no.hiof.set.g6.db;


import java.sql.Connection;

/**
 * @author Frederik Dahl
 * 07/10/2024
 */


public abstract class Database {

    protected String url;
    protected String user;
    protected String password;
    protected Connection connection;
    
    
}
