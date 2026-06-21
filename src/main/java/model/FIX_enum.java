package model;

public enum FIX_enum {
    RESINES_NATURELLE(1.75),  // TODO: confirm real price/gramme
    MUSCS_SYNTHESE(2.22),     // TODO: confirm real price/gramme
    AMBRE(3.99);              // TODO: confirm real price/gramme

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    FIX_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
