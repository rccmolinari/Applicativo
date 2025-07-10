package implementazionePostgresDAO;

import dao.LoginDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione dell'interfaccia {@link LoginDAO} per l'accesso e la registrazione
 * degli utenti su database PostgreSQL.
 */
public class LoginImplementazionePostgresDAO implements LoginDAO {

    private static final Logger LOGGER = Logger.getLogger(LoginImplementazionePostgresDAO.class.getName());
    private Connection connection;

    /**
     * Costruttore della classe. Inizializza la connessione al database.
     */
    public LoginImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nella connessione al database", e);
        }
    }

    /**
     * Verifica le credenziali di accesso per un utente o un amministratore.
     *
     * @param email    l'email (username) dell'utente.
     * @param password la password dell'utente.
     * @return 0 se il login fallisce, 1 se è un utente generico, 2 se è un amministratore.
     */

    @Override
    public int login(String email, String password) {
        final String QUERY_UTENTE = "SELECT username, password FROM utente_generico WHERE username = ? AND password = ?";
        final String QUERY_ADMIN = "SELECT username, password FROM amministratore WHERE username = ? AND password = ?";

        try (
                PreparedStatement psUtente = connection.prepareStatement(QUERY_UTENTE)
        ) {
            psUtente.setString(1, email);
            psUtente.setString(2, password);

            try (ResultSet rs = psUtente.executeQuery()) {
                if (rs.next()) return 1;
            }

            try (
                    PreparedStatement psAdmin = connection.prepareStatement(QUERY_ADMIN)
            ) {
                psAdmin.setString(1, email);
                psAdmin.setString(2, password);

                try (ResultSet rsAdmin = psAdmin.executeQuery()) {
                    if (rsAdmin.next()) return 2;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il login", e);
        }

        return 0;
    }

    /**
     * Registra un nuovo utente nel database.
     *
     * @param email l'email (o username) da registrare.
     * @param password la password associata.
     * @return true se la registrazione ha avuto successo, false se l'utente è già esistente o c'è stato un errore.
     */
    @Override
    public boolean registrazione(String email, String password) {
        final String QUERY_CHECK = "SELECT username FROM utente_generico WHERE username = ?";
        final String QUERY_INSERT = "INSERT INTO utente_generico (username, password) VALUES (?, ?)";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(QUERY_CHECK)
        ) {
            checkStmt.setString(1, email);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return false; // Utente già registrato
                }
            }

            try (
                    PreparedStatement insertStmt = connection.prepareStatement(QUERY_INSERT)
            ) {
                insertStmt.setString(1, email);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la registrazione", e);
        }

        return false;
    }
}
