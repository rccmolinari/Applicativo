package model;

public enum StatoBagaglio {

    REGISTRATO("registrato"),
    IMBARCATO("imbarcato"),
    CONSEGNATO("consegnato"),
    SMARRITO("smarrito");

    private final String label;

    StatoBagaglio(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static StatoBagaglio fromString(String stato) {
        for (StatoBagaglio s : values()) {
            if (s.label.equalsIgnoreCase(stato)) return s;
        }
        throw new IllegalArgumentException("Stato bagaglio non valido: " + stato);
    }
}
