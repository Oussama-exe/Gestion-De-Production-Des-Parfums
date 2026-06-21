package model;

public enum FIX_enum {
    RESINES_NATURELLE(0.0),  // TODO: confirm real price/gramme
    MUSCS_SYNTHESE(0.0),     // TODO: confirm real price/gramme
    AMBRE(0.0);              // TODO: confirm real price/gramme

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    FIX_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
