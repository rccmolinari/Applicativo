package model;

public enum StatoPrenotazione {

    CONFERMATA("confermata"),
    IN_ATTESA("in attesa"),
    CANCELLATA("cancellata");

    private final String label;

    StatoPrenotazione(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static StatoPrenotazione fromString(String stato) {
        for (StatoPrenotazione s : values()) {
            if (s.label.equalsIgnoreCase(stato)) return s;
        }
        throw new IllegalArgumentException("Stato prenotazione non valido: " + stato);
    }
}
