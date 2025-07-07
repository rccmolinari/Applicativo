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
    private String inPartenza = "inPartenza";
    private String inArrivo = "inArrivo";
    private String idBagaglio = "id_bagaglio";
    private String statoBagaglio = "stato_bagaglio";

    public AmministratoreImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            throw new ConnessioneDatabaseException("Errore nella connessione al database", e);
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

        String tipo = (v instanceof VoloInPartenza) ? inPartenza : inArrivo;

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

            if (v instanceof VoloInPartenza voloinp) {
                ps.setInt(10, (voloinp.getGate()));
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();
            LOGGER.info("Volo inserito con successo: " + v.getCodiceVolo());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inserimento del volo", e);
        }
    }

    @Override
    public void aggiornaVolo(Volo v) {
        final String SQL = """
            UPDATE volo SET compagnia = ?, data_volo = ?, orario_previsto = ?, ritardo = ?,
                            stato_volo = ?, tipo_volo = ?, aeroporto_destinazione = ?, aeroporto_origine = ?, gate = ?
            WHERE codice_volo = ?
            """;

        String tipo = (v instanceof VoloInPartenza) ? inPartenza : inArrivo;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, v.getCompagnia());
            ps.setDate(2, new java.sql.Date(v.getData().getTime()));
            ps.setTime(3, v.getOrario());
            ps.setInt(4, v.getRitardo());
            ps.setString(5, v.getStato().name());
            ps.setString(6, tipo);
            ps.setString(7, v.getDestinazione());
            ps.setString(8, v.getOrigine());

            if (v instanceof VoloInPartenza voloinp) {
                ps.setInt(9, (voloinp.getGate()));
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }

            ps.setInt(10, v.getCodiceVolo());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                LOGGER.warning("Nessun volo aggiornato. Codice volo non trovato: " + v.getCodiceVolo());
            } else {
                LOGGER.info("Volo aggiornato con successo: " + v.getCodiceVolo());
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'aggiornamento del volo", e);
        }
    }

    @Override
    public void modificaGate(VoloInPartenza volo) {
        final String SQL = "UPDATE volo SET gate = ? WHERE codice_volo = ?";

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, volo.getGate());
            ps.setInt(2, volo.getCodiceVolo());

            ps.executeUpdate();
            LOGGER.info("Gate aggiornato per il volo: " + volo.getCodiceVolo());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la modifica del gate", e);
        }
    }

    @Override
    public void aggiornaBagaglio(Bagaglio bagaglio, StatoBagaglio stato) {
        final String SQL = "UPDATE bagaglio SET stato_bagaglio = ? WHERE id_bagaglio = ?";

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, stato.toString());
            ps.setInt(2, bagaglio.getCodiceBagaglio());

            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Stato bagaglio aggiornato a {0} per ID {1}", new Object[]{stato, bagaglio.getCodiceBagaglio()});

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'aggiornamento del bagaglio", e);
        }
    }

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
                b.setCodiceBagaglio(rs.getInt(idBagaglio));
                b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(statoBagaglio)));

                Prenotazione p = new Prenotazione();
                p.setNumeroBiglietto(rs.getInt("numero_biglietto"));

                b.setPrenotazione(p);
                lista.add(b);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero dei bagagli smarriti", e);
        }

        return lista;
    }

    @Override
    public ArrayList<Volo> visualizzaVoli() {
        ArrayList<Volo> lista = new ArrayList<>();
        final String SQL = "SELECT codice_volo, compagnia, data_volo, orario_previsto, ritardo, stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate " +
                "FROM volo ORDER BY data_volo, orario_previsto";


        try (PreparedStatement ps = connection.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo_volo");

                if (inPartenza.equalsIgnoreCase(tipo)) {
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
                } else if (inArrivo.equalsIgnoreCase(tipo)) {
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
            LOGGER.log(Level.SEVERE, "Errore durante la visualizzazione dei voli", e);
        }

        return lista;
    }

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
                    bag.setCodiceBagaglio(rs.getInt(idBagaglio));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(statoBagaglio)));

                    Prenotazione pren = new Prenotazione();
                    pren.setNumeroBiglietto(rs.getInt("numero_biglietto"));
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
            WHERE pr.numero_biglietto = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setInt(1, p.getNumeroBiglietto());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bagaglio bag = new Bagaglio();
                    bag.setCodiceBagaglio(rs.getInt(idBagaglio));
                    bag.setStatoBagaglio(StatoBagaglio.fromString(rs.getString(statoBagaglio)));
                    bag.setPrenotazione(p);
                    lista.add(bag);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante la ricerca bagagli per prenotazione", e);
        }

        return lista;
    }

    public static class ConnessioneDatabaseException extends RuntimeException {
        public ConnessioneDatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
