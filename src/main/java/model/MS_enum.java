package model;

public enum MS_enum {
    MUSCS_SYNTHETIQUE(2.77),   // TODO: confirm real price/gramme
    ALDEHYDE(4.25),            // TODO: confirm real price/gramme
    CETONE(6.44),              // TODO: confirm real price/gramme
    MOLECULES_ESOLES(4.88);    // TODO: confirm real price/gramme

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    MS_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
