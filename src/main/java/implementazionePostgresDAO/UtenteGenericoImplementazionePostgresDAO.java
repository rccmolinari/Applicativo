package implementazionePostgresDAO;

import dao.UtenteGenericoDAO;
import database.ConnessioneDatabase;
import model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtenteGenericoImplementazionePostgresDAO implements UtenteGenericoDAO {
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = Logger.getLogger(UtenteGenericoImplementazionePostgresDAO.class.getName());
    private static final String CODICE_VOLO = "codice_volo";
    private static final String STATO_PRENOTAZIONE = "stato_prenotazione";
    private static final String STATO_BAGAGLIO = "stato_bagaglio";
    private static final String STATO_VOLO = "stato_volo";
    private static final String NUMERO_BIGLIETTO = "numero_biglietto";
    private static final String POSTO_ASSEGNATO = "posto_assegnato";
    private static final String ID_BAGAGLIO = "id_bagaglio";
    private Connection connection;

    public UtenteGenericoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nella connessione al database", e);
        }
    }

    @Override
    public ArrayList<Volo> visualizzaVoli() {
        ArrayList<Volo> lista = new ArrayList<>();
        final String QUERY = "SELECT codice_volo, compagnia, data_volo, orario_previsto, ritardo, stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate FROM volo WHERE stato_volo <> 'CANCELLATO' ORDER BY data_volo, orario_previsto";

        try (PreparedStatement ps = connection.prepareStatement(QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo_volo");

                Volo v;
                if ("inPartenza".equalsIgnoreCase(tipo)) {
                    v = new VoloInPartenza();
                    ((VoloInPartenza) v).setGate(rs.getInt("gate"));
                } else if ("inArrivo".equalsIgnoreCase(tipo)) {
                    v = new VoloInArrivo();
                } else {
                    continue; // ignora tipi sconosciuti
                }

                v.setCodiceVolo(rs.getInt(CODICE_VOLO));
                v.setCompagnia(rs.getString("compagnia"));
                v.setData(rs.getDate("data_volo"));
                v.setOrario(rs.getTime("orario_previsto"));
                v.setRitardo(rs.getInt("ritardo"));
                v.setStato(StatoVolo.fromString(rs.getString(STATO_VOLO)));
                v.setDestinazione(rs.getString("aeroporto_destinazione"));
                v.setOrigine(rs.getString("aeroporto_origine"));

                lista.add(v);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la visualizzazione dei voli", e);
        }

        return lista;
    }


    @Override
    public ArrayList<Prenotazione> listaPrenotazioni(UtenteGenerico utente) {
        ArrayList<Prenotazione> lista = new ArrayList<>();
        final String QUERY = """
        SELECT * FROM prenotazione pr
        JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento
        LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto
        WHERE username = ? AND password = ?
        ORDER BY pr.numero_biglietto
        """;

        try (PreparedStatement ps = connection.prepareStatement(QUERY)) {
            ps.setString(1, utente.getLogin());
            ps.setString(2, utente.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                int ultimoBiglietto = -1;
                Prenotazione p = null;

                while (rs.next()) {
                    int numeroBiglietto = rs.getInt(NUMERO_BIGLIETTO);

                    if (numeroBiglietto != ultimoBiglietto) {
                        Passeggero passeggero = new Passeggero();
                        passeggero.setNome(rs.getString("nome"));
                        passeggero.setCognome(rs.getString("cognome"));

                        p = new Prenotazione();
                        p.setNumeroBiglietto(numeroBiglietto);
                        p.setNumeroAssegnato(rs.getInt(POSTO_ASSEGNATO));
                        p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString(STATO_PRENOTAZIONE)));
                        p.setPasseggero(passeggero);

                        lista.add(p);
                        ultimoBiglietto = numeroBiglietto;
                    }

                    if (rs.getObject(ID_BAGAGLIO) != null && p != null) {
                        Bagaglio b = new Bagaglio();
                        b.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                        b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));
                        b.setPrenotazione(p);
                        p.listaBagagli.add(b);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero delle prenotazioni", e);
        }

        return lista;
    }

    @Override
    public void prenotaVolo(UtenteGenerico utente, Volo volo, Passeggero passeggero, ArrayList<Bagaglio> bagagli) {
        final String SQL_PASSEGGERO = """
        INSERT INTO passeggero (id_documento, nome, cognome, data_nascita)
        VALUES (?, ?, ?, ?)
        ON CONFLICT (id_documento) DO NOTHING
        """;

        final String SQL_PRENOTAZIONE = """
        INSERT INTO prenotazione (username, password, documento_passeggero, stato_prenotazione, posto_assegnato, codice_volo)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        final String SQL_BAGAGLIO = """
        INSERT INTO bagaglio (stato_bagaglio, numero_prenotazione)
        VALUES (?, ?)
        """;

        try (
                PreparedStatement psPasseggero = connection.prepareStatement(SQL_PASSEGGERO);
                PreparedStatement psPrenotazione = connection.prepareStatement(SQL_PRENOTAZIONE, new String[]{NUMERO_BIGLIETTO});
                PreparedStatement psBagaglio = connection.prepareStatement(SQL_BAGAGLIO)
        ) {
            connection.setAutoCommit(false);

            // Inserimento passeggero (se nuovo)
            psPasseggero.setString(1, passeggero.getIdDocumento());
            psPasseggero.setString(2, passeggero.getNome());
            psPasseggero.setString(3, passeggero.getCognome());
            psPasseggero.setDate(4, new java.sql.Date(passeggero.getDataNascita().getTime()));
            int passeggeroInserito = psPasseggero.executeUpdate();

            LOGGER.info(String.format("Passeggero %s", passeggeroInserito > 0 ? "nuovo inserito" : "già esistente"));

            // Inserimento prenotazione
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

            int numeroBiglietto;
            try (ResultSet generatedKeys = psPrenotazione.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    numeroBiglietto = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Prenotazione fallita: nessun numero biglietto generato.");
                }
            }

            // Inserimento bagagli (batch)
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
                LOGGER.log(Level.SEVERE, "Transazione annullata per errore", e);
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Errore nel rollback", rollbackEx);
            }
            throw new PrenotazioneException("Errore durante la prenotazione", e);
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Errore nel ripristinare AutoCommit", ex);
            }
        }
    }

    @Override
    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, int numeroBiglietto) {
        ArrayList<Prenotazione> lista = new ArrayList<>();

        final String SQL = """
        SELECT pr.*,
               pa.nome,
               pa.cognome,
               pa.data_nascita,
               pa.id_documento,
               b.id_bagaglio,
               b.stato_bagaglio,
               pr.codice_volo
        FROM prenotazione pr
        JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento
        LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto
        JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password
        WHERE pr.numero_biglietto = ?
          AND ug.username = ?
          AND ug.password = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, numeroBiglietto);
            ps.setString(2, utente.getLogin());
            ps.setString(3, utente.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                int ultimoBiglietto = -1;
                Prenotazione p = null;

                while (rs.next()) {
                    int numero = rs.getInt(NUMERO_BIGLIETTO);

                    if (numero != ultimoBiglietto) {
                        Passeggero pa = new Passeggero();
                        pa.setNome(rs.getString("nome"));
                        pa.setCognome(rs.getString("cognome"));
                        pa.setIdDocumento(rs.getString("id_documento"));

                        java.sql.Date sqlDate = rs.getDate("data_nascita");
                        if (sqlDate != null) {
                            pa.setDataNascita(new Date(sqlDate.getTime()));
                        }

                        p = new Prenotazione();
                        p.setNumeroBiglietto(numero);
                        p.setNumeroAssegnato(rs.getInt(POSTO_ASSEGNATO));
                        p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString(STATO_PRENOTAZIONE)));
                        p.setPasseggero(pa);

                        Volo v = new Volo();
                        v.setCodiceVolo(rs.getInt(CODICE_VOLO));
                        p.setVolo(v);
                        p.setListaBagagli(new ArrayList<>());

                        lista.add(p);
                        ultimoBiglietto = numero;
                    }

                    if (rs.getObject(ID_BAGAGLIO) != null && p != null) {
                        Bagaglio b = new Bagaglio();
                        b.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                        b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));
                        b.setPrenotazione(p);
                        p.getListaBagagli().add(b);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca della prenotazione", e);
        }

        return lista;
    }

    @Override
    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, String nome, String cognome) {
        ArrayList<Prenotazione> lista = new ArrayList<>();

        final String SQL = """
        SELECT pr.numero_biglietto,
               pr.posto_assegnato,
               pr.stato_prenotazione,
               pr.documento_passeggero,
               pa.nome AS nome_passeggero,
               pa.cognome AS cognome_passeggero,
               pa.id_documento,
               pa.data_nascita AS aa,
               b.id_bagaglio,
               b.stato_bagaglio,
               pr.codice_volo
        FROM prenotazione pr
        JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento
        LEFT JOIN bagaglio b ON b.numero_prenotazione = pr.numero_biglietto
        JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password
        WHERE pa.nome = ?
          AND pa.cognome = ?
          AND ug.username = ?
          AND ug.password = ?
        ORDER BY pr.numero_biglietto
        """;

        try (
                PreparedStatement ps = connection.prepareStatement(SQL)
        ) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, utente.getLogin());
            ps.setString(4, utente.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                Prenotazione prenotazioneCorrente = null;
                int ultimoBiglietto = -1;

                while (rs.next()) {
                    int numeroBiglietto = rs.getInt(NUMERO_BIGLIETTO);

                    if (numeroBiglietto != ultimoBiglietto) {
                        Passeggero pa = new Passeggero();
                        pa.setNome(rs.getString("nome_passeggero"));
                        pa.setCognome(rs.getString("cognome_passeggero"));
                        pa.setIdDocumento(rs.getString("id_documento"));

                        java.sql.Date sqlDate = rs.getDate("aa");
                        if (sqlDate != null) {
                            pa.setDataNascita(new Date(sqlDate.getTime()));
                        }

                        prenotazioneCorrente = new Prenotazione();
                        prenotazioneCorrente.setNumeroBiglietto(numeroBiglietto);
                        prenotazioneCorrente.setNumeroAssegnato(rs.getInt(POSTO_ASSEGNATO));
                        prenotazioneCorrente.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString(STATO_PRENOTAZIONE)));
                        prenotazioneCorrente.setPasseggero(pa);
                        prenotazioneCorrente.setListaBagagli(new ArrayList<>());

                        Volo v = new Volo();
                        v.setCodiceVolo(rs.getInt(CODICE_VOLO));
                        prenotazioneCorrente.setVolo(v);

                        lista.add(prenotazioneCorrente);
                        ultimoBiglietto = numeroBiglietto;
                    }

                    if (rs.getObject(ID_BAGAGLIO) != null && prenotazioneCorrente != null) {
                        Bagaglio b = new Bagaglio();
                        b.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                        b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));
                        b.setPrenotazione(prenotazioneCorrente);
                        prenotazioneCorrente.getListaBagagli().add(b);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca della prenotazione per nome e cognome", e);
        }

        return lista;
    }

    @Override
    public void segnalaSmarrimento(Bagaglio bagaglio, UtenteGenerico utente) throws SQLException {
        final String SQL = """
        UPDATE bagaglio b
        SET stato_bagaglio = ?
        FROM prenotazione pr
        JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password
        WHERE b.numero_prenotazione = pr.numero_biglietto
          AND ug.username = ?
          AND ug.password = ?
          AND b.id_bagaglio = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, StatoBagaglio.SMARRITO.toString());
            ps.setString(2, utente.getLogin());
            ps.setString(3, utente.getPassword());
            ps.setInt(4, bagaglio.getCodiceBagaglio());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.warning("Tentativo di segnalazione smarrimento fallito: bagaglio non trovato o non autorizzato");
                throw new SQLException("Bagaglio non trovato o non appartenente all’utente.");
            } else {
                LOGGER.info("Stato bagaglio aggiornato a SMARRITO (ID: " + bagaglio.getCodiceBagaglio() + ")");
            }

        } catch (SQLException e) {
            String msg = "Errore durante la segnalazione di smarrimento";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SQLException(msg, e);
        }

    }

    @Override
    public void modificaPrenotazione(Prenotazione prenotazione, ArrayList<Bagaglio> nuoviBagagli) {
        final String DELETE_SQL = "DELETE FROM bagaglio WHERE numero_prenotazione = ?";
        final String INSERT_SQL = "INSERT INTO bagaglio (stato_bagaglio, numero_prenotazione) VALUES (?, ?)";

        try (
                PreparedStatement psDelete = connection.prepareStatement(DELETE_SQL);
                PreparedStatement psInsert = connection.prepareStatement(INSERT_SQL)
        ) {
            connection.setAutoCommit(false);

            // Elimina bagagli esistenti
            psDelete.setInt(1, prenotazione.getNumeroBiglietto());
            psDelete.executeUpdate();

            // Inserisce i nuovi bagagli
            for (Bagaglio b : nuoviBagagli) {
                psInsert.setString(1, b.getStatoBagaglio().toString());
                psInsert.setInt(2, prenotazione.getNumeroBiglietto());
                psInsert.addBatch();
            }
            psInsert.executeBatch();

            connection.commit();
            LOGGER.info("Bagagli aggiornati con successo per la prenotazione n° " + prenotazione.getNumeroBiglietto());

        } catch (SQLException e) {
            try {
                connection.rollback();
                LOGGER.log(Level.SEVERE, "Rollback effettuato a causa di errore nella modifica della prenotazione", e);
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Errore durante il rollback", rollbackEx);
            }
            throw new ModificaPrenotazioneException("Errore durante modifica prenotazione: " + e.getMessage(), e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Errore nel ripristinare AutoCommit", ex);
            }
        }
    }

    @Override
    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b, UtenteGenerico u) {
        ArrayList<Bagaglio> lista = new ArrayList<>();

        final String SQL = """
        SELECT b.*, pr.numero_biglietto
        FROM bagaglio b
        LEFT JOIN prenotazione pr ON b.numero_prenotazione = pr.numero_biglietto
        JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password
        WHERE b.id_bagaglio = ?
          AND ug.username = ?
          AND ug.password = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, b.getCodiceBagaglio());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bagaglio bag = new Bagaglio();
                    bag.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));

                    Prenotazione pren = new Prenotazione();
                    pren.setNumeroBiglietto(rs.getInt(NUMERO_BIGLIETTO));
                    bag.setPrenotazione(pren);

                    lista.add(bag);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca del bagaglio", e);
        }

        return lista;
    }

    @Override
    public ArrayList<Bagaglio> cercaBagaglio(Prenotazione p, UtenteGenerico u) {
        ArrayList<Bagaglio> lista = new ArrayList<>();

        final String SQL = """
        SELECT b.*, pr.numero_biglietto
        FROM bagaglio b
        JOIN prenotazione pr ON b.numero_prenotazione = pr.numero_biglietto
        JOIN utente_generico ug ON ug.username = pr.username AND ug.password = pr.password
        WHERE pr.numero_biglietto = ?
          AND ug.username = ?
          AND ug.password = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, p.getNumeroBiglietto());
            ps.setString(2, u.getLogin());
            ps.setString(3, u.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bagaglio bag = new Bagaglio();
                    bag.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));
                    bag.setPrenotazione(p);
                    lista.add(bag);
                }
            }

        } catch (SQLException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, String.format("Errore durante la ricerca dei bagagli per la prenotazione n° %d", p.getNumeroBiglietto()), e);
            }
        }

        return lista;
    }


    private int generaPostoRandom() {
        return RANDOM.nextInt(100) + 1;
    }


    private static class PrenotazioneException extends RuntimeException {
        public PrenotazioneException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class ModificaPrenotazioneException extends RuntimeException {
        public ModificaPrenotazioneException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}

