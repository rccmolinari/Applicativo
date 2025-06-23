package implementazionePostgresDAO;

import dao.UtenteGenericoDAO;
import database.ConnessioneDatabase;
import model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public ArrayList<Volo> visualizzaVoli() {
        ArrayList<Volo> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT * FROM volo WHERE stato_volo <> 'CANCELLATO' ORDER BY data_volo, orario_previsto");
            rs = ps.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo_volo");

                if ("inPartenza".equalsIgnoreCase(tipo)) {
                    VoloInPartenza v = new VoloInPartenza();
                    v.setCodiceVolo(rs.getInt("codice_volo"));
                    v.setCompagnia(rs.getString("compagnia"));
                    v.setData(rs.getDate("data_volo"));
                    v.setOrario(rs.getTime("orario_previsto"));
                    v.setRitardo(rs.getInt("ritardo"));
                    v.setStato(StatoVolo.fromString(rs.getString("stato_volo").toLowerCase()));
                    v.setDestinazione(rs.getString("aeroporto_destinazione"));
                    v.setOrigine(rs.getString("aeroporto_origine"));
                    v.setGate(rs.getInt("gate"));

                    lista.add(v);

                } else if ("inArrivo".equalsIgnoreCase(tipo)) {
                    VoloInArrivo v = new VoloInArrivo();
                    v.setCodiceVolo(rs.getInt("codice_volo"));
                    v.setCompagnia(rs.getString("compagnia"));
                    v.setData(rs.getDate("data_volo"));
                    v.setOrario(rs.getTime("orario_previsto"));
                    v.setRitardo(rs.getInt("ritardo"));
                    v.setStato(StatoVolo.fromString(rs.getString("stato_volo").toLowerCase()));
                    v.setDestinazione(rs.getString("aeroporto_destinazione"));
                    v.setOrigine(rs.getString("aeroporto_origine"));

                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return lista;
    }

    public ArrayList<Prenotazione> listaPrenotazioni(UtenteGenerico utente) {
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
            connection.setAutoCommit(false);

            psPasseggero = connection.prepareStatement(
                    "INSERT INTO passeggero (id_documento, nome, cognome, data_nascita) " +
                            "VALUES (?, ?, ?, ?) ON CONFLICT (id_documento) DO NOTHING");
            psPasseggero.setString(1, passeggero.getIdDocumento());
            psPasseggero.setString(2, passeggero.getNome());
            psPasseggero.setString(3, passeggero.getCognome());
            psPasseggero.setDate(4, new java.sql.Date(passeggero.getDataNascita().getTime()));
            int passeggeroInserito = psPasseggero.executeUpdate();

            boolean passeggeroNuovo = passeggeroInserito > 0;
            System.out.println("Passeggero " + (passeggeroNuovo ? "nuovo" : "già presente"));

            psPrenotazione = connection.prepareStatement(
                    "INSERT INTO prenotazione (username, password, documento_passeggero, stato_prenotazione, posto_assegnato, codice_volo) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    new String[]{"numero_biglietto"});
            psPrenotazione.setString(1, utente.getLogin());
            psPrenotazione.setString(2, utente.getPassword());
            psPrenotazione.setString(3, passeggero.getIdDocumento());
            psPrenotazione.setString(4, StatoPrenotazione.CONFERMATA.toString());
            psPrenotazione.setInt(5, generaPostoRandom());
            psPrenotazione.setInt(6, volo.getCodiceVolo());

            int righeInserite = psPrenotazione.executeUpdate();
            if (righeInserite == 0) {
                throw new SQLException("Prenotazione non inserita: controlla volo o documento passeggero.");
            }

            generatedKeys = psPrenotazione.getGeneratedKeys();
            int numeroBiglietto;
            if (generatedKeys.next()) {
                numeroBiglietto = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Prenotazione fallita: nessun numero biglietto generato.");
            }

            psBagaglio = connection.prepareStatement(
                    "INSERT INTO bagaglio (stato_bagaglio, numero_prenotazione) VALUES (?, ?)");
            for (Bagaglio b : bagagli) {
                psBagaglio.setString(1, b.getStatoBagaglio().toString());
                psBagaglio.setInt(2, numeroBiglietto);
                psBagaglio.addBatch();
            }
            psBagaglio.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Errore durante la prenotazione: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (psPasseggero != null) psPasseggero.close();
                if (psPrenotazione != null) psPrenotazione.close();
                if (psBagaglio != null) psBagaglio.close();
                connection.setAutoCommit(true);
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
            String sql = "SELECT pr.*, " +
                    "       pa.nome, " +
                    "       pa.cognome, " +
                    "       pa.data_nascita, " +
                    "       b.id_bagaglio, " +
                    "       b.stato_bagaglio, " +
                    "       pa.id_documento " +
                    "FROM prenotazione pr " +
                    "JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento " +
                    "LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto " +
                    "WHERE pr.numero_biglietto = ?";

            ps = connection.prepareStatement(sql);
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
                    pa.setIdDocumento(rs.getString("id_documento"));

                    // Lettura e conversione data di nascita
                    java.sql.Date sqlDate = rs.getDate("data_nascita");
                    Date dataNascita = null;
                    if (sqlDate != null) {
                        dataNascita = new Date(sqlDate.getTime());
                    }
                    pa.setDataNascita(dataNascita);

                    p = new Prenotazione();
                    p.setNumeroBiglietto(numero);
                    p.setNumeroAssegnato(rs.getInt("posto_assegnato"));
                    p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString("stato_prenotazione")));
                    p.setPasseggero(pa);
                    p.setListaBagagli(new ArrayList<>());

                    lista.add(p);
                    ultimoBiglietto = numero;
                }

                if (rs.getObject("id_bagaglio") != null) {
                    Bagaglio b = new Bagaglio();
                    b.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));
                    b.setPrenotazione(p);
                    if (p != null) {
                        p.getListaBagagli().add(b);
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
            String sql =   "SELECT pr.numero_biglietto, " +
                    "       pr.posto_assegnato, " +
                    "       pr.stato_prenotazione, " +
                    "       pr.documento_passeggero, " +
                    "       pa.nome AS nome_passeggero, " +
                    "       pa.cognome AS cognome_passeggero, " +
                    "       pa.id_documento, " +
                    "       pa.data_nascita AS aa, " +
                    "       b.id_bagaglio, " +
                    "       b.stato_bagaglio " +
                    "FROM prenotazione pr " +
                    "JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento " +
                    "LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto " +
                    "JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password " +
                    "WHERE pa.nome = ? " +
                    "AND pa.cognome = ? " +
                    "AND ug.username = ? " +
                    "AND ug.password = ? " +
                    "ORDER BY pr.numero_biglietto";

            ps = connection.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, utente.getLogin());
            ps.setString(4, utente.getPassword());

            rs = ps.executeQuery();

            Prenotazione prenotazioneCorrente = null;
            int ultimoBiglietto = -1;

            while (rs.next()) {
                if (ultimoBiglietto == -1) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int colonne = meta.getColumnCount();
                    for (int i = 1; i <= colonne; i++) {
                        System.out.println("Colonna #" + i + ": " + meta.getColumnName(i));
                    }
                }

                int numeroBiglietto = rs.getInt("numero_biglietto");
                System.out.println("Numero biglietto: " + numeroBiglietto);

                if (numeroBiglietto != ultimoBiglietto) {
                    Passeggero pa = new Passeggero();
                    pa.setNome(rs.getString("nome_passeggero"));
                    pa.setCognome(rs.getString("cognome_passeggero"));
                    pa.setIdDocumento(rs.getString("id_documento"));

                    java.sql.Date sqlDate = rs.getDate("aa");
                    Date dataNascita = null;
                    if (sqlDate != null) {
                        dataNascita = new Date(sqlDate.getTime());
                    }
                    System.out.println("Data di nascita letta: " + dataNascita);
                    pa.setDataNascita(dataNascita);

                    prenotazioneCorrente = new Prenotazione();
                    prenotazioneCorrente.setNumeroBiglietto(numeroBiglietto);
                    prenotazioneCorrente.setNumeroAssegnato(rs.getInt("posto_assegnato"));
                    prenotazioneCorrente.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString("stato_prenotazione")));
                    prenotazioneCorrente.setPasseggero(pa);
                    prenotazioneCorrente.setListaBagagli(new ArrayList<>());

                    lista.add(prenotazioneCorrente);
                    ultimoBiglietto = numeroBiglietto;
                }

                if (rs.getObject("id_bagaglio") != null && prenotazioneCorrente != null) {
                    Bagaglio b = new Bagaglio();
                    b.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));
                    b.setPrenotazione(prenotazioneCorrente);
                    prenotazioneCorrente.getListaBagagli().add(b);
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

    public void modificaPrenotazione(Prenotazione prenotazione, ArrayList<Bagaglio> nuoviBagagli) {
        PreparedStatement psDelete = null;
        PreparedStatement psInsert = null;

        try {
            connection.setAutoCommit(false);

            psDelete = connection.prepareStatement(
                    "DELETE FROM bagaglio WHERE numero_prenotazione = ?");
            psDelete.setInt(1, prenotazione.getNumeroBiglietto());
            psDelete.executeUpdate();

            psInsert = connection.prepareStatement(
                    "INSERT INTO bagaglio (stato_bagaglio, numero_prenotazione) VALUES (?, ?)");
            for (Bagaglio b : nuoviBagagli) {
                psInsert.setString(1, b.getStatoBagaglio().toString());
                psInsert.setInt(2, prenotazione.getNumeroBiglietto());
                psInsert.addBatch();
            }
            psInsert.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Errore durante modifica prenotazione: " + e.getMessage());
        } finally {
            try {
                if (psDelete != null) psDelete.close();
                if (psInsert != null) psInsert.close();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b) {
        ArrayList<Bagaglio> lista = new ArrayList<>();
        String sql = "SELECT b.*, pr.numero_biglietto FROM bagaglio b " +
                "LEFT JOIN prenotazione pr ON b.numero_prenotazione = pr.numero_biglietto " +
                "WHERE b.id_bagaglio = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, b.getCodiceBagaglio());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bagaglio bag = new Bagaglio();
                    bag.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));

                    Prenotazione pren = new Prenotazione();
                    pren.setNumeroBiglietto(rs.getInt("numero_biglietto"));
                    bag.setPrenotazione(pren);

                    lista.add(bag);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    @Override
    public ArrayList<Bagaglio> cercaBagaglio(Prenotazione p) {
        ArrayList<Bagaglio> lista = new ArrayList<>();

        String sql = "SELECT b.*, pr.numero_biglietto " +
                "FROM bagaglio b " +
                "JOIN prenotazione pr ON b.numero_prenotazione = pr.numero_biglietto " +
                "WHERE pr.numero_biglietto = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, p.getNumeroBiglietto());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bagaglio bag = new Bagaglio();
                    bag.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato_bagaglio")));
                    bag.setPrenotazione(p);
                    lista.add(bag);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }



    private int generaPostoRandom() {
        return (int) (Math.random() * 100 + 1);
    }
}