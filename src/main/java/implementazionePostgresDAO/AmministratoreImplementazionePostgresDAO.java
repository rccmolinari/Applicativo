package implementazionePostgresDAO;

import database.ConnessioneDatabase;
import dao.AmministratoreDAO;
import model.*;

import java.sql.*;
import java.util.ArrayList;

public class AmministratoreImplementazionePostgresDAO implements AmministratoreDAO {

    private Connection connection;

    public AmministratoreImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserisciVolo(Volo v) {
        String tipo = "";

        if (v instanceof VoloInPartenza) {
            tipo = "inPartenza";
        } else if (v instanceof VoloInArrivo) {
            tipo = "inArrivo";
        }

        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(
                    "INSERT INTO volo (codice_volo, compagnia, data_volo, orario_previsto, ritardo, stato_volo, tipo_volo, aeroporto_destinazione, aeroporto_origine, gate) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setInt(1, v.getCodiceVolo());
            ps.setString(2, v.getCompagnia());
            ps.setDate(3, new java.sql.Date(v.getData().getTime()));
            ps.setTime(4, v.getOrario());
            ps.setInt(5, v.getRitardo());
            ps.setString(6, v.getStato());
            ps.setString(7, tipo);
            ps.setString(8, v.getDestinazione());
            ps.setString(9, v.getOrigine());

            if (v instanceof VoloInPartenza) {
                ps.setInt(10, ((VoloInPartenza) v).getGate());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void aggiornaVolo(Volo v) {
        String tipo = "";

        if (v instanceof VoloInPartenza) {
            tipo = "inPartenza";
        } else if (v instanceof VoloInArrivo) {
            tipo = "inArrivo";
        }

        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(
                    "UPDATE volo SET compagnia = ?, data_volo = ?, orario_previsto = ?, ritardo = ?, stato_volo = ?, tipo_volo = ?, aeroporto_destinazione = ?, aeroporto_origine = ?, gate = ? " +
                            "WHERE codice_volo = ?"
            );

            ps.setString(1, v.getCompagnia());
            ps.setDate(2, new java.sql.Date(v.getData().getTime()));
            ps.setTime(3, v.getOrario());
            ps.setInt(4, v.getRitardo());
            ps.setString(5, v.getStato());
            ps.setString(6, tipo);
            ps.setString(7, v.getDestinazione());
            ps.setString(8, v.getOrigine());

            if (v instanceof VoloInPartenza) {
                ps.setInt(9, ((VoloInPartenza) v).getGate());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }

            ps.setInt(10, v.getCodiceVolo());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void modificaGate(VoloInPartenza volo) {
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(
                    "UPDATE volo SET gate = ? WHERE codice_volo = ?"
            );

            ps.setInt(1, volo.getGate());
            ps.setInt(2, volo.getCodiceVolo());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void aggiornaBagaglio(Bagaglio bagaglio, StatoBagaglio stato) {
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(
                    "UPDATE bagaglio SET stato = ? WHERE id_bagaglio = ?"
            );

            ps.setString(1, stato.toString());
            ps.setInt(2, bagaglio.getCodiceBagaglio());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<Bagaglio> visualizzaSmarrimento() {
        ArrayList<Bagaglio> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(
                    "SELECT b.id_bagaglio, b.stato, b.id_prenotazione, p.numero_biglietto " +
                            "FROM bagaglio b " +
                            "JOIN prenotazione p ON b.id_prenotazione = p.id_prenotazione " +
                            "WHERE b.stato = 'smarrito'"
            );

            rs = ps.executeQuery();

            while (rs.next()) {
                Bagaglio b = new Bagaglio();
                b.setCodiceBagaglio(rs.getInt("id_bagaglio"));
                b.setStatoBagaglio(StatoBagaglio.fromString(rs.getString("stato")));

                Prenotazione p = new Prenotazione();
                p.setNumeroBiglietto(rs.getInt("numero_biglietto"));

                b.setPrenotazione(p);
                lista.add(b);
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

    public ArrayList<Volo> visualizzaVoli() {
        ArrayList<Volo> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("SELECT * FROM volo");
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
                    v.setStato(StatoVolo.fromString(rs.getString("stato_volo")));
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
                    v.setStato(StatoVolo.fromString(rs.getString("stato_volo")));
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
}
