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

/**
 * Implementazione dell'interfaccia {@link UtenteGenericoDAO} che consente a un utente
 * generico di interagire con il sistema di gestione voli tramite PostgreSQL.
 * Le funzionalità includono la visualizzazione dei voli, gestione prenotazioni,
 * modifica bagagli, segnalazione smarrimento e ricerca.
 */

public class UtenteGenericoImplementazionePostgresDAO implements UtenteGenericoDAO {
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = Logger.getLogger(UtenteGenericoImplementazionePostgresDAO.class.getName());
    private static final String CODICE_VOLO = "codice_volo";
    private static final String STATO_PRENOTAZIONE = "stato_prenotazione";
    private static final String STATO_BAGAGLIO = "stato_bagaglio";
    private static final String STATO_VOLO = "stato_volo";
    private static final String NUMERO_BIGLIETTO = "numero_biglietto";
    private static final String POSTO_ASSEGNATO = "posto_assegnato";
    private static final String ID_BAGAGLIO = "id_bagaglio";// Costanti aggiunte in cima alla classe:
    private static final String TIPO_VOLO = "tipo_volo";
    private static final String IN_PARTENZA = "inPartenza";
    private static final String IN_ARRIVO = "inArrivo";
    private static final String COMPAGNIA = "compagnia";
    private static final String DATA_VOLO = "data_volo";
    private static final String ORARIO_PREVISTO = "orario_previsto";
    private static final String RITARDO = "ritardo";
    private static final String AEROPORTO_ORIGINE = "aeroporto_origine";
    private static final String AEROPORTO_DESTINAZIONE = "aeroporto_destinazione";


    private Connection connection;

    /**
     * Costruttore: inizializza la connessione al database.
     */

    public UtenteGenericoImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nella connessione al database", e);
        }
    }

    /**
     * Recupera dal database l'elenco di tutti i voli (anche già decollati o cancellati).
     * Ogni volo viene istanziato in base al tipo (arrivo o partenza), e vengono popolati i campi
     * rilevanti come compagnia, data, orario, gate, origine, destinazione e stato.
     *
     * @return una lista di oggetti {@link Volo}, ordinata per data e orario previsto.
     * @throws CustomExc se si verifica un errore durante l'accesso al database.
     */

    @Override
    public ArrayList<Volo> visualizzaVoli() {
        ArrayList<Volo> lista = new ArrayList<>();
        final String QUERY = "SELECT codice_volo, compagnia, data_volo, orario_previsto, ritardo, stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate FROM volo WHERE stato_volo <> 'CANCELLATO' AND stato_volo <> 'DECOLLATO' ORDER BY data_volo, orario_previsto";

        try (PreparedStatement ps = connection.prepareStatement(QUERY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString(TIPO_VOLO);

                Volo v;
                if (IN_PARTENZA.equalsIgnoreCase(tipo)) {
                    v = new VoloInPartenza();
                    ((VoloInPartenza) v).setGate(rs.getInt("gate"));
                } else if (IN_ARRIVO.equalsIgnoreCase(tipo)) {
                    v = new VoloInArrivo();
                } else {
                    continue; // ignora tipi sconosciuti
                }

                v.setCodiceVolo(rs.getInt(CODICE_VOLO));
                v.setCompagnia(rs.getString(COMPAGNIA));
                v.setData(rs.getDate(DATA_VOLO));
                v.setOrario(rs.getTime(ORARIO_PREVISTO));
                v.setRitardo(rs.getInt(RITARDO));
                v.setStato(StatoVolo.fromString(rs.getString(STATO_VOLO)));
                v.setDestinazione(rs.getString(AEROPORTO_DESTINAZIONE));
                v.setOrigine(rs.getString(AEROPORTO_ORIGINE));

                lista.add(v);
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante la visualizzazione dei voli", e);
        }

        return lista;
    }

    /**
     * Recupera tutte le prenotazioni effettuate da un utente specifico.
     * Ogni prenotazione include le informazioni del passeggero e la lista dei bagagli,
     * se presenti. I risultati sono aggregati per numero di biglietto.
     *
     * @param utente utente autenticato (username e password usati per identificare le prenotazioni).
     * @return lista delle prenotazioni associate all'utente.
     * @throws CustomExc se si verifica un errore durante la lettura dei dati dal database.
     */

    @Override
    public ArrayList<Prenotazione> listaPrenotazioni(UtenteGenerico utente) {
        ArrayList<Prenotazione> lista = new ArrayList<>();
        final String QUERY = """
        SELECT * FROM prenotazione pr
        JOIN passeggero pa ON pr.documento_passeggero = pa.id_documento
        JOIN volo v ON pr.codice_volo = v.codice_volo
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

                        // Costruzione volo
                        Volo volo;
                        String tipo = rs.getString(TIPO_VOLO);

                        if (IN_PARTENZA.equalsIgnoreCase(tipo)) {
                            volo = new VoloInPartenza();
                            ((VoloInPartenza) volo).setGate(rs.getInt("gate"));
                        } else if (IN_ARRIVO.equalsIgnoreCase(tipo)) {
                            volo = new VoloInArrivo();
                        } else {
                            continue;
                        }

                        volo.setCodiceVolo(rs.getInt(CODICE_VOLO));
                        volo.setCompagnia(rs.getString(COMPAGNIA));
                        volo.setData(rs.getDate(DATA_VOLO));
                        volo.setOrario(rs.getTime(ORARIO_PREVISTO));
                        volo.setRitardo(rs.getInt(RITARDO));
                        volo.setStato(StatoVolo.fromString(rs.getString(STATO_VOLO)));
                        volo.setOrigine(rs.getString(AEROPORTO_ORIGINE));
                        volo.setDestinazione(rs.getString(AEROPORTO_DESTINAZIONE));

                        p = new Prenotazione();
                        p.setNumeroBiglietto(numeroBiglietto);
                        p.setNumeroAssegnato(rs.getInt(POSTO_ASSEGNATO));
                        p.setStatoPrenotazione(StatoPrenotazione.fromString(rs.getString(STATO_PRENOTAZIONE)));
                        p.setPasseggero(passeggero);
                        p.setVolo(volo);

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
            throw new CustomExc("Errore durante il recupero delle prenotazioni", e);
        }

        return lista;
    }


    /**
     * Esegue la prenotazione di un volo da parte di un utente autenticato.
     * L'operazione viene eseguita in una transazione che comprende:
     *   -Verifica della validità del volo
     *   -Inserimento del passeggero (se nuovo)
     *   -Creazione della prenotazione
     *   -Inserimento dei bagagli
     * In caso di errore, viene effettuato il rollback.
     *
     * @param utente utente autenticato che esegue la prenotazione.
     * @param volo volo da prenotare.
     * @param passeggero passeggero a cui è intestata la prenotazione.
     * @param bagagli lista di bagagli da registrare per la prenotazione.
     * @throws CustomExc se la prenotazione fallisce in qualsiasi punto della transazione.
     */

    @Override
    public void prenotaVolo(UtenteGenerico utente, Volo volo, Passeggero passeggero, ArrayList<Bagaglio> bagagli) {
        final String check= "SELECT 1 FROM volo WHERE codice_volo = ? AND stato_volo <> 'CANCELLATO' AND stato_volo <> 'DECOLLATO'";
        try (PreparedStatement psCheck = connection.prepareStatement(check)) {
            psCheck.setInt(1, volo.getCodiceVolo());
            ResultSet rs = psCheck.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Volo non esistente, non disponibile o già decollato.");
            }
        } catch (SQLException e) {
            throw new CustomExc("Volo non esistente, non disponibile o già decollato.", e);
        }
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
            if(passeggeroInserito > 0) {
                LOGGER.info("Passeggero inserito con successo: " + passeggero.getIdDocumento());
            } else {
                LOGGER.info("Passeggero già esistente: " + passeggero.getIdDocumento());
            }

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
            throw new CustomExc("Errore durante la prenotazione", e);
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
    public ArrayList<Volo> cercaVolo(Volo v) {
        ArrayList<Volo> risultati = new ArrayList<>();
        final String QUERY_CODICE = "SELECT * FROM volo WHERE codice_volo = ?";
        final String QUERY_ORIG_DEST = "SELECT * FROM volo WHERE aeroporto_origine = ? AND aeroporto_destinazione = ?";

        try {
            PreparedStatement ps;
            Integer codice = v.getCodiceVolo();

            if (codice != null && codice > 0) {
                ps = connection.prepareStatement(QUERY_CODICE);
                ps.setInt(1, codice);
            } else if (v.getOrigine() != null && v.getDestinazione() != null) {
                ps = connection.prepareStatement(QUERY_ORIG_DEST);
                ps.setString(1, v.getOrigine());
                ps.setString(2, v.getDestinazione());
            } else {
                return risultati; // lista vuota se parametri assenti
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString(TIPO_VOLO);
                    Volo risultato;

                    if (IN_PARTENZA.equalsIgnoreCase(tipo)) {
                        risultato = new VoloInPartenza();
                        ((VoloInPartenza) risultato).setGate(rs.getInt("gate"));
                    } else if (IN_ARRIVO.equalsIgnoreCase(tipo)) {
                        risultato = new VoloInArrivo();
                    } else {
                        continue;
                    }

                    risultato.setCodiceVolo(rs.getInt(CODICE_VOLO));
                    risultato.setCompagnia(rs.getString(COMPAGNIA));
                    risultato.setData(rs.getDate(DATA_VOLO));
                    risultato.setOrario(rs.getTime(ORARIO_PREVISTO));
                    risultato.setRitardo(rs.getInt(RITARDO));
                    risultato.setStato(StatoVolo.fromString(rs.getString(STATO_VOLO)));
                    risultato.setOrigine(rs.getString(AEROPORTO_ORIGINE));
                    risultato.setDestinazione(rs.getString(AEROPORTO_DESTINAZIONE));

                    risultati.add(risultato);
                }
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante la ricerca dei voli", e);
        }

        return risultati;
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
            throw new CustomExc("Errore durante la ricerca della prenotazione", e);
        }

        return lista;
    }

    /**
     * Cerca tutte le prenotazioni effettuate da un utente in cui il passeggero
     * corrisponde al nome e cognome forniti. Vengono incluse anche le informazioni
     * sui bagagli e sul volo associato.
     *
     * @param utente utente autenticato.
     * @param nome nome del passeggero.
     * @param cognome cognome del passeggero.
     * @return lista delle prenotazioni corrispondenti.
     * @throws CustomExc se si verifica un errore durante la ricerca.
     */

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
            throw new CustomExc("Errore durante la ricerca della prenotazione per nome e cognome", e);
        }

        return lista;
    }

    /**
     * Permette all'utente di segnalare come smarrito un bagaglio registrato
     * a suo nome. L'aggiornamento avviene solo se il bagaglio è effettivamente
     * associato a una prenotazione dell'utente.
     *
     * @param bagaglio bagaglio da marcare come smarrito.
     * @param utente utente autenticato proprietario del bagaglio.
     * @throws SQLException se il bagaglio non appartiene all'utente o in caso di errore durante l'update.
     */


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
            throw new SQLException(msg, e);
        }

    }

    /**
     * Aggiorna l'elenco dei bagagli associati a una prenotazione.
     * Elimina tutti i bagagli precedenti e inserisce quelli nuovi.
     * L'intera operazione è eseguita in transazione. Viene eseguito un rollback in caso di errore.
     *
     * @param prenotazione prenotazione da aggiornare.
     * @param nuoviBagagli nuova lista di bagagli da associare.
     * @throws CustomExc se si verifica un errore durante l’operazione o durante il rollback.
     */

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
            throw new CustomExc("Errore durante modifica prenotazione: " + e.getMessage(), e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Errore nel ripristinare AutoCommit", ex);
            }
        }
    }

    /**
     * Cerca un bagaglio specifico per ID, verificando che appartenga all’utente.
     * Restituisce una lista con il bagaglio se trovato.
     *
     * @param b bagaglio da cercare (contenente l’ID).
     * @param u utente autenticato.
     * @return lista contenente il bagaglio trovato.
     * @throws CustomExc se si verifica un errore durante l'accesso al database.
     */


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
            throw new CustomExc("Errore durante la ricerca del bagaglio", e);
        }

        return lista;
    }

    /**
     * Recupera tutti i bagagli associati a una prenotazione specifica.
     * Vengono mostrati solo se la prenotazione appartiene all’utente autenticato.
     *
     * @param p prenotazione di riferimento.
     * @param u utente autenticato.
     * @return lista dei bagagli associati alla prenotazione.
     * @throws CustomExc se si verifica un errore durante la query o nella connessione.
     */

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
            throw new CustomExc("Errore durante la ricerca dei bagagli per la prenotazione", e);
        }

        return lista;
    }

    /**
     * Genera in modo pseudo-casuale un numero di posto tra 1 e 100.
     * Questo metodo è usato per assegnare un numero di posto casuale
     * a una prenotazione nel momento della conferma.
     *
     * @return numero intero compreso tra 1 e 100.
     */


    private int generaPostoRandom() {
        return RANDOM.nextInt(100) + 1;
    }

    /**
     * Eccezione personalizzata per gestire errori specifici dell'implementazione.
     * Viene lanciata quando si verificano problemi durante le operazioni di accesso al database.
     */

    private static class CustomExc extends RuntimeException {
        public CustomExc(String message, Throwable cause) {
            super(message, cause);
        }
    }


}

