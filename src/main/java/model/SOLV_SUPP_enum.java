package model;

public enum SOLV_SUPP_enum {
    ALCOOL_ETHYLIQUE(0.0),  // TODO: confirm real price/gramme
    HUILE(0.0),             // TODO: confirm real price/gramme
    EAU_DISTILLEE(0.0);     // TODO: confirm real price/gramme

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    SOLV_SUPP_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
