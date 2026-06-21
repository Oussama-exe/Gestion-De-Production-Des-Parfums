package model;

public enum MS_enum {
    MUSCS_SYNTHETIQUE(0.0),   // TODO: confirm real price/gramme
    ALDEHYDE(0.0),            // TODO: confirm real price/gramme
    CETONE(0.0),              // TODO: confirm real price/gramme
    MOLECULES_ESOLES(0.0);    // TODO: confirm real price/gramme

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    MS_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
