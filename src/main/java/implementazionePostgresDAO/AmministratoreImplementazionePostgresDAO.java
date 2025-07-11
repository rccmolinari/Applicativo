package implementazionePostgresDAO;

import database.ConnessioneDatabase;
import dao.AmministratoreDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmministratoreImplementazionePostgresDAO implements AmministratoreDAO {

    private static final Logger LOGGER = Logger.getLogger(AmministratoreImplementazionePostgresDAO.class.getName());
    private final Connection connection;

    // Costanti colonna
    private static final String IN_PARTENZA = "inPartenza";
    private static final String IN_ARRIVO = "inArrivo";
    private static final String ID_BAGAGLIO = "id_bagaglio";
    private static final String STATO_BAGAGLIO = "stato_bagaglio";
    private static final String COL_CODICE_VOLO = "codice_volo";
    private static final String COL_COMPAGNIA = "compagnia";
    private static final String COL_DATA_VOLO = "data_volo";
    private static final String COL_ORARIO = "orario_previsto";
    private static final String COL_RITARDO = "ritardo";
    private static final String COL_STATO_VOLO = "stato_volo";
    private static final String COL_TIPO_VOLO = "tipo_volo";
    private static final String COL_DESTINAZIONE = "aeroporto_destinazione";
    private static final String COL_ORIGINE = "aeroporto_origine";
    private static final String COL_NUMERO_BIGLIETTO = "numero_biglietto";

    public AmministratoreImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            throw new CustomExc("Errore nella connessione al database", e);
        }
    }

    @Override
    public void inserisciVolo(Volo v) {
        final String SQL = """
            INSERT INTO volo (
                codice_volo, compagnia, data_volo, orario_previsto, ritardo,
                stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        String tipo = (v instanceof VoloInPartenza) ? IN_PARTENZA : IN_ARRIVO;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, v.getCodiceVolo());
            ps.setString(2, v.getCompagnia());
            ps.setDate(3, new java.sql.Date(v.getData().getTime()));
            ps.setTime(4, v.getOrario());
            ps.setInt(5, v.getRitardo());
            ps.setString(6, v.getStato().name());
            ps.setString(7, tipo);
            ps.setString(8, v.getDestinazione());
            ps.setString(9, v.getOrigine());
            ps.setObject(10, (v instanceof VoloInPartenza voloPart) ? voloPart.getGate() : null);
            ps.executeUpdate();
            LOGGER.info("Volo inserito con successo: " + v.getCodiceVolo());
        } catch (SQLException e) {
            throw new CustomExc("Errore durante l'inserimento del volo: " + v.getCodiceVolo(), e);
        }
    }

    @Override
    public void aggiornaVolo(Volo v) {
        final String SQL = """
            UPDATE volo SET compagnia = ?, data_volo = ?, orario_previsto = ?, ritardo = ?,
                            stato_volo = ?, tipo_volo = ?, aeroporto_destinazione = ?, aeroporto_origine = ?, gate = ?
            WHERE codice_volo = ?
            """;

        String tipo = (v instanceof VoloInPartenza) ? IN_PARTENZA : IN_ARRIVO;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, v.getCompagnia());
            ps.setDate(2, new java.sql.Date(v.getData().getTime()));
            ps.setTime(3, v.getOrario());
            ps.setInt(4, v.getRitardo());
            ps.setString(5, v.getStato().name());
            ps.setString(6, tipo);
            ps.setString(7, v.getDestinazione());
            ps.setString(8, v.getOrigine());
            ps.setObject(9, (v instanceof VoloInPartenza voloPart) ? voloPart.getGate() : null);
            ps.setInt(10, v.getCodiceVolo());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                LOGGER.warning("Nessun volo aggiornato. Codice volo non trovato: " + v.getCodiceVolo());
            } else {
                LOGGER.info("Volo aggiornato con successo: " + v.getCodiceVolo());
            }
        } catch (SQLException e) {
            throw new CustomExc("Errore durante l'aggiornamento del volo: " + v.getCodiceVolo(), e);
        }
    }

    @Override
    public void modificaGate(VoloInPartenza volo) {
        final String CHECK_SQL = "SELECT tipo_volo FROM volo WHERE codice_volo = ?";
        final String UPDATE_SQL = "UPDATE volo SET gate = ? WHERE codice_volo = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_SQL)) {
            checkStmt.setInt(1, volo.getCodiceVolo());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next() || !IN_PARTENZA.equalsIgnoreCase(rs.getString(COL_TIPO_VOLO))) {
                    throw new CustomExc("Gate modificabile solo per voli in partenza.", new RuntimeException());
                }
            }

            try (PreparedStatement updateStmt = connection.prepareStatement(UPDATE_SQL)) {
                updateStmt.setInt(1, volo.getGate());
                updateStmt.setInt(2, volo.getCodiceVolo());
                updateStmt.executeUpdate();
                LOGGER.info("Gate aggiornato per il volo: " + volo.getCodiceVolo());
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante la modifica del gate per il volo: " + volo.getCodiceVolo(), e);
        }
    }

    @Override
    public void aggiornaBagaglio(Bagaglio bagaglio, StatoBagaglio stato) {
        final String CHECK_SQL = "SELECT 1 FROM bagaglio WHERE id_bagaglio = ?";
        final String UPDATE_SQL = "UPDATE bagaglio SET stato_bagaglio = ? WHERE id_bagaglio = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_SQL);
             PreparedStatement updateStmt = connection.prepareStatement(UPDATE_SQL)) {

            checkStmt.setInt(1, bagaglio.getCodiceBagaglio());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new CustomExc("Bagaglio con ID " + bagaglio.getCodiceBagaglio() + " non esiste.", new RuntimeException());
                }
            }

            updateStmt.setString(1, stato.toString());
            updateStmt.setInt(2, bagaglio.getCodiceBagaglio());
            updateStmt.executeUpdate();
            LOGGER.log(Level.INFO, "Stato bagaglio aggiornato a {0} per ID {1}", new Object[]{stato, bagaglio.getCodiceBagaglio()});

        } catch (SQLException e) {
            throw new CustomExc("Errore durante l'aggiornamento del bagaglio con ID: " + bagaglio.getCodiceBagaglio(), e);
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
                    String tipo = rs.getString(COL_TIPO_VOLO);
                    Volo risultato;

                    if (IN_PARTENZA.equalsIgnoreCase(tipo)) {
                        risultato = new VoloInPartenza();
                        ((VoloInPartenza) risultato).setGate(rs.getInt("gate"));
                    } else if (IN_ARRIVO.equalsIgnoreCase(tipo)) {
                        risultato = new VoloInArrivo();
                    } else {
                        continue;
                    }

                    risultato.setCodiceVolo(rs.getInt(COL_CODICE_VOLO));
                    risultato.setCompagnia(rs.getString(COL_COMPAGNIA));
                    risultato.setData(rs.getDate(COL_DATA_VOLO));
                    risultato.setOrario(rs.getTime(COL_ORARIO));
                    risultato.setRitardo(rs.getInt(COL_RITARDO));
                    risultato.setStato(StatoVolo.fromString(rs.getString(COL_STATO_VOLO)));
                    risultato.setOrigine(rs.getString(COL_ORIGINE));
                    risultato.setDestinazione(rs.getString(COL_DESTINAZIONE));

                    risultati.add(risultato);
                }
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante la ricerca dei voli", e);
        }

        return risultati;
    }


    /**
     * Recupera tutti i bagagli che risultano attualmente smarriti nel sistema.
     * Vengono restituite anche le informazioni minime sulla prenotazione associata.
     *
     * @return lista dei bagagli marcati con stato "smarrito".
     * @throws CustomExc se si verifica un errore durante la lettura dal database.
     */

    @Override
    public ArrayList<Bagaglio> visualizzaSmarrimento() {
        ArrayList<Bagaglio> lista = new ArrayList<>();
        final String SQL = """
            SELECT b.id_bagaglio, b.stato_bagaglio, b.numero_prenotazione, p.numero_biglietto
            FROM bagaglio b
            JOIN prenotazione p ON b.numero_prenotazione = p.numero_biglietto
            WHERE b.stato_bagaglio = 'smarrito'
            """;

        try (PreparedStatement ps = connection.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bagaglio b = new Bagaglio();
                b.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));

                Prenotazione p = new Prenotazione();
                p.setNumeroBiglietto(rs.getInt(COL_NUMERO_BIGLIETTO));

                b.setPrenotazione(p);
                lista.add(b);
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante il recupero dei bagagli smarriti", e);
        }

        return lista;
    }

    /**
     * Recupera l'elenco completo di tutti i voli presenti nel database,
     * indipendentemente dal loro stato. I voli vengono ordinati per data e orario.
     * Ogni riga viene mappata su un oggetto {@link VoloInPartenza} o {@link VoloInArrivo}.
     *
     * @return lista di voli disponibili nel sistema.
     * @throws CustomExc se si verifica un errore durante l'accesso al database.
     */


    @Override
    public ArrayList<Volo> visualizzaVoli() {
        ArrayList<Volo> lista = new ArrayList<>();
        final String SQL = "SELECT codice_volo, compagnia, data_volo, orario_previsto, ritardo, stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate " +
                "FROM volo ORDER BY data_volo, orario_previsto";


        try (PreparedStatement ps = connection.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString(COL_TIPO_VOLO);

                if (IN_PARTENZA.equalsIgnoreCase(tipo)) {
                    VoloInPartenza v = new VoloInPartenza();
                    v.setCodiceVolo(rs.getInt(COL_CODICE_VOLO));
                    v.setCompagnia(rs.getString(COL_COMPAGNIA));
                    v.setData(rs.getDate(COL_DATA_VOLO));
                    v.setOrario(rs.getTime(COL_ORARIO));
                    v.setRitardo(rs.getInt(COL_RITARDO));
                    v.setStato(StatoVolo.fromString(rs.getString(COL_STATO_VOLO).toLowerCase()));
                    v.setDestinazione(rs.getString(COL_DESTINAZIONE));
                    v.setOrigine(rs.getString(COL_ORIGINE));
                    v.setGate(rs.getInt("gate"));
                    lista.add(v);
                } else if (IN_ARRIVO.equalsIgnoreCase(tipo)) {
                    VoloInArrivo v = new VoloInArrivo();
                    v.setCodiceVolo(rs.getInt(COL_CODICE_VOLO));
                    v.setCompagnia(rs.getString(COL_COMPAGNIA));
                    v.setData(rs.getDate(COL_DATA_VOLO));
                    v.setOrario(rs.getTime(COL_ORARIO));
                    v.setRitardo(rs.getInt(COL_RITARDO));
                    v.setStato(StatoVolo.fromString(rs.getString(COL_STATO_VOLO).toLowerCase()));
                    v.setDestinazione(rs.getString(COL_DESTINAZIONE));
                    v.setOrigine(rs.getString(COL_ORIGINE));
                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante la visualizzazione dei voli", e);
        }

        return lista;
    }

    /**
     * Cerca un bagaglio in base al suo identificativo.
     * Poiché è un'operazione amministrativa, non è vincolata all'utente.
     *
     * @param b oggetto contenente almeno l'ID del bagaglio da cercare.
     * @param u parametro ignorato (richiesto dall'interfaccia ma non utilizzato qui).
     * @return lista con il bagaglio trovato, se esiste.
     * @throws CustomExc se si verifica un errore nella query.
     */


    @Override
    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b, UtenteGenerico u) {
        ArrayList<Bagaglio> lista = new ArrayList<>();
        final String SQL = """
            SELECT b.*, pr.numero_biglietto
            FROM bagaglio b
            LEFT JOIN prenotazione pr ON b.numero_prenotazione = pr.numero_biglietto
            WHERE b.id_bagaglio = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, b.getCodiceBagaglio());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bagaglio bag = new Bagaglio();
                    bag.setCodiceBagaglio(rs.getInt(ID_BAGAGLIO));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(STATO_BAGAGLIO)));

                    Prenotazione pren = new Prenotazione();
                    pren.setNumeroBiglietto(rs.getInt(COL_NUMERO_BIGLIETTO));
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
     * Recupera tutti i bagagli associati a una determinata prenotazione.
     * Poiché l'operazione è lato amministratore, l'utente non viene validato.
     *
     * @param p prenotazione da cui recuperare i bagagli.
     * @param u parametro ignorato (richiesto dall'interfaccia ma non utilizzato qui).
     * @return lista dei bagagli collegati alla prenotazione specificata.
     * @throws CustomExc se si verifica un errore durante la ricerca.
     */


    @Override
    public ArrayList<Bagaglio> cercaBagaglio(Prenotazione p, UtenteGenerico u) {
        ArrayList<Bagaglio> lista = new ArrayList<>();
        final String SQL = """
            SELECT b.*, pr.numero_biglietto
            FROM bagaglio b
            JOIN prenotazione pr ON b.numero_prenotazione = pr.numero_biglietto
            WHERE pr.numero_biglietto = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, p.getNumeroBiglietto());

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
            throw new CustomExc("Errore durante la ricerca bagagli per prenotazione", e);
        }

        return lista;
    }
    @Override
    public ArrayList<Prenotazione> cercaPasseggero(Passeggero passeggero) {
        ArrayList<Prenotazione> lista = new ArrayList<>();
        final String QUERY = """
        SELECT p.numero_biglietto, p.stato_prenotazione, 
               v.*, pa.nome, pa.cognome
        FROM prenotazione p
        JOIN volo v ON p.codice_volo = v.codice_volo
        JOIN passeggero pa ON p.documento_passeggero = pa.id_documento
        WHERE pa.id_documento = ?
        ORDER BY p.numero_biglietto
    """;

        try (PreparedStatement ps = connection.prepareStatement(QUERY)) {
            ps.setString(1, passeggero.getIdDocumento());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crea il volo dinamico in base al tipo
                    Volo volo;
                    String tipo = rs.getString(COL_TIPO_VOLO);

                    if (IN_PARTENZA.equalsIgnoreCase(tipo)) {
                        volo = new VoloInPartenza();
                        ((VoloInPartenza) volo).setGate(rs.getInt("gate"));
                    } else if (IN_ARRIVO.equalsIgnoreCase(tipo)) {
                        volo = new VoloInArrivo();
                    } else {
                        continue; // tipo non valido
                    }

                    volo.setCodiceVolo(rs.getInt(COL_CODICE_VOLO));
                    volo.setCompagnia(rs.getString(COL_COMPAGNIA));
                    volo.setData(rs.getDate(COL_DATA_VOLO));
                    volo.setOrario(rs.getTime(COL_ORARIO));
                    volo.setRitardo(rs.getInt(COL_RITARDO));
                    volo.setStato(StatoVolo.fromString(rs.getString(COL_STATO_VOLO)));
                    volo.setOrigine(rs.getString(COL_ORIGINE));
                    volo.setDestinazione(rs.getString(COL_DESTINAZIONE));

                    // Costruisci il passeggero
                    Passeggero p = new Passeggero();
                    p.setIdDocumento(passeggero.getIdDocumento());
                    p.setNome(rs.getString("nome"));
                    p.setCognome(rs.getString("cognome"));

                    // Crea la prenotazione
                    Prenotazione pren = new Prenotazione();
                    pren.setNumeroBiglietto(rs.getInt(COL_NUMERO_BIGLIETTO));
                    pren.setStatoPrenotazione(StatoPrenotazione.valueOf(rs.getString("stato_prenotazione").toUpperCase()));
                    pren.setPasseggero(p);
                    pren.setVolo(volo);

                    lista.add(pren);
                }
            }

        } catch (SQLException e) {
            throw new CustomExc("Errore durante la ricerca delle prenotazioni del passeggero", e);
        }

        return lista;
    }



    /**
     * Eccezione personalizzata per mascherare le eccezioni SQL come {@link RuntimeException}.
     * Utilizzata per propagare errori critici durante l’accesso al database.
     */

    public static class CustomExc extends RuntimeException {
        public CustomExc(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
