package model;

public enum StatoVolo {
    PROGRAMMATO("programmato"),
    DECOLLATO("decollato"),
    RITARDO("ritardo"),
    CANCELLATO("cancellato"); // ho corretto 'cacellato'

    private final String label;

    StatoVolo(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static StatoVolo fromString(String stato) {
        for (StatoVolo s : values()) {
            if (s.label.equalsIgnoreCase(stato)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Valore non valido per StatoVolo: " + stato);
    }
}
