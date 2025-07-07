package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnessioneDatabase {

    private static final Logger LOGGER = Logger.getLogger(ConnessioneDatabase.class.getName());

    // ATTRIBUTI
    private static ConnessioneDatabase instance;
    public Connection connection;
    private static final String NOME = "postgres";
    private static final String PASSWORD = "admin";
    private static final String URL = "jdbc:postgresql://localhost:5432/aereoporto";

    // COSTRUTTORE
    private ConnessioneDatabase() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, NOME, PASSWORD);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database Connection Creation Failed: {0}", ex.getMessage());
            throw ex;
        }
    }

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
}
