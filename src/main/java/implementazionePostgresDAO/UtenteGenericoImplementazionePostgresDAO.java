package implementazionePostgresDAO;

import dao.UtenteGenericoDAO;
import database.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.util.ArrayList;

public class UtenteGenericoImplementazionePostgresDAO implements UtenteGenericoDAO {
    private Connection connection;

    public UtenteGenericoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Prenotazione> ListaPrenotazioni(UtenteGenerico utente) {
        ArrayList<Prenotazione> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(
                    "SELECT * FROM prenotazione pr " +
                            "JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento " +
                            "LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto " +
                            "WHERE username = ? AND password = ? " +
                            "ORDER BY pr.numero_biglietto"
            );
            ps.setString(1, utente.getLogin());
            ps.setString(2, utente.getPassword());

            rs = ps.executeQuery();

            int ultimoBiglietto = -1;
            Prenotazione p = null;

            while (rs.next()) {
                int numeroBiglietto = rs.getInt("numero_biglietto");

                if (numeroBiglietto != ultimoBiglietto) {
                    Passeggero pa = new Passeggero();
                    pa.setNome(rs.getString("nome"));
                    pa.setCognome(rs.getString("cognome"));

                    p = new Prenotazione();
                    p.setNumeroBiglietto(numeroBiglietto);
                    p.setNumeroAssegnato(rs.getInt("posto_assegnato"));
                    p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString("stato_prenotazione")));
                    p.setPasseggero(pa);

                    lista.add(p);
                    ultimoBiglietto = numeroBiglietto;
                }

                if (rs.getObject("id_bagaglio") != null) {
                    Bagaglio b = new Bagaglio();
                    b.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));
                    b.setPrenotazione(p);
                    if (p != null) {
                        p.listaBagagli.add(b);
                    }
                }
            }

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

        return lista;
    }

    public void prenotaVolo(UtenteGenerico utente, Volo volo, Passeggero passeggero, ArrayList<Bagaglio> bagagli) {
        PreparedStatement psPasseggero = null;
        PreparedStatement psPrenotazione = null;
        PreparedStatement psBagaglio = null;
        ResultSet generatedKeys = null;

        try {
            psPasseggero = connection.prepareStatement(
                    "INSERT INTO passeggero (id_documento, nome, cognome, data_nascita) " +
                            "VALUES (?, ?, ?, ?) ON CONFLICT (id_documento) DO NOTHING"
            );
            psPasseggero.setString(1, passeggero.getIdDocumento());
            psPasseggero.setString(2, passeggero.getNome());
            psPasseggero.setString(3, passeggero.getCognome());
            psPasseggero.setDate(4, new java.sql.Date(passeggero.getDataNascita().getTime()));
            psPasseggero.executeUpdate();

            psPrenotazione = connection.prepareStatement(
                    "INSERT INTO prenotazione (username, password, documento_passeggero, stato_prenotazione, posto_assegnato, codice_volo) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    new String[] { "numero_biglietto" }
            );
            psPrenotazione.setString(1, utente.getLogin());
            psPrenotazione.setString(2, utente.getPassword());
            psPrenotazione.setString(3, passeggero.getIdDocumento());
            psPrenotazione.setString(4, StatoPrenotazione.CONFERMATA.toString());
            psPrenotazione.setInt(5, generaPostoRandom());
            psPrenotazione.setInt(6, volo.getCodiceVolo());

            psPrenotazione.executeUpdate();

            int numeroBiglietto;
            generatedKeys = psPrenotazione.getGeneratedKeys();
            if (generatedKeys.next()) {
                numeroBiglietto = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Prenotazione fallita: nessun numero biglietto generato.");
            }

            psBagaglio = connection.prepareStatement(
                    "INSERT INTO bagaglio (stato_bagaglio, numero_prenotazione) VALUES (?, ?)"
            );
            for (Bagaglio b : bagagli) {
                psBagaglio.setString(1, b.getStatoBagaglio().toString());
                psBagaglio.setInt(2, numeroBiglietto);
                psBagaglio.addBatch();
            }
            psBagaglio.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la prenotazione: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (psPasseggero != null) psPasseggero.close();
                if (psPrenotazione != null) psPrenotazione.close();
                if (psBagaglio != null) psBagaglio.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, int numeroBiglietto) {
        ArrayList<Prenotazione> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(
                    "SELECT * FROM prenotazione pr " +
                            "JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento " +
                            "LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto " +
                            "WHERE pr.numero_biglietto = ?"
            );
            ps.setInt(1, numeroBiglietto);

            rs = ps.executeQuery();

            int ultimoBiglietto = -1;
            Prenotazione p = null;

            while (rs.next()) {
                int numero = rs.getInt("numero_biglietto");

                if (numero != ultimoBiglietto) {
                    Passeggero pa = new Passeggero();
                    pa.setNome(rs.getString("nome"));
                    pa.setCognome(rs.getString("cognome"));

                    p = new Prenotazione();
                    p.setNumeroBiglietto(numero);
                    p.setNumeroAssegnato(rs.getInt("posto_assegnato"));
                    p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString("stato_prenotazione")));
                    p.setPasseggero(pa);

                    lista.add(p);
                    ultimoBiglietto = numero;
                }

                if (rs.getObject("id_bagaglio") != null) {
                    Bagaglio b = new Bagaglio();
                    b.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));
                    b.setPrenotazione(p);
                    if (p != null) {
                        p.listaBagagli.add(b);
                    }
                }
            }

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

        return lista;
    }

    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, String nome, String cognome) {
        ArrayList<Prenotazione> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(
                    "SELECT * FROM prenotazione pr " +
                            "JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento " +
                            "LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto " +
                            "JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password " +
                            "WHERE pa.nome = ? AND pa.cognome = ? AND ug.username = ? AND ug.password = ? " +
                            "ORDER BY pr.numero_biglietto"
            );
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, utente.getLogin());
            ps.setString(4, utente.getPassword());

            rs = ps.executeQuery();

            int ultimoBiglietto = -1;
            Prenotazione p = null;

            while (rs.next()) {
                int numeroBiglietto = rs.getInt("numero_biglietto");

                if (numeroBiglietto != ultimoBiglietto) {
                    Passeggero pa = new Passeggero();
                    pa.setNome(rs.getString("nome"));
                    pa.setCognome(rs.getString("cognome"));
                    pa.setIdDocumento(rs.getString("documento_passeggero"));
                    pa.setDataNascita(rs.getDate("data_nascita"));

                    p = new Prenotazione();
                    p.setNumeroBiglietto(numeroBiglietto);
                    p.setNumeroAssegnato(rs.getInt("posto_assegnato"));
                    p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString("stato_prenotazione")));
                    p.setPasseggero(pa);

                    lista.add(p);
                    ultimoBiglietto = numeroBiglietto;
                }

                if (rs.getObject("id_bagaglio") != null) {
                    Bagaglio b = new Bagaglio();
                    b.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));
                    b.setPrenotazione(p);
                    if (p != null) {
                        p.listaBagagli.add(b);
                    }
                }
            }

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

        return lista;
    }


    public void segnalaSmarrimento(Bagaglio bagaglio) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE bagaglio SET stato_bagaglio = ? WHERE id_bagaglio = ?")) {
            ps.setString(1, StatoBagaglio.SMARRITO.toString());
            ps.setInt(2, bagaglio.getCodiceBagaglio());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int generaPostoRandom() {
        return (int) (Math.random() * 100 + 1);
    }
}