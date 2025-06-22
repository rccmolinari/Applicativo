package implementazionePostgresDAO;

import dao.AmministratoreDAO;
import dao.LoginDAO;
import database.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginImplementazionePostgresDAO implements LoginDAO {

    private Connection connection;

    public LoginImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 0 = fallito, 1 = utente, 2 = admin
    public int login(String email, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(
                    "SELECT * FROM utente_generico WHERE username = ? AND password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) return 1;

            ps = connection.prepareStatement(
                    "SELECT * FROM amministratore WHERE username = ? AND password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) return 2;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return 0;
    }



    @Override
    public boolean registrazione(String email, String password) {
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            // Controlla se esiste già un utente con quella email
            checkStmt = connection.prepareStatement(
                    "SELECT username FROM utente_generico WHERE username = ?"
            );
            checkStmt.setString(1, email);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Esiste già un utente con questa email
                return false;
            }

            // Inserisce nuovo utente
            insertStmt = connection.prepareStatement(
                    "INSERT INTO utente_generico (username, password) VALUES (?, ?)"
            );
            insertStmt.setString(1, email);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate(); // CORRETTO: per INSERT

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (insertStmt != null) insertStmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    }


