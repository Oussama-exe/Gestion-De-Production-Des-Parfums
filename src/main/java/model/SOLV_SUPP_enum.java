package model;

public enum SOLV_SUPP_enum {
    ALCOOL_ETHYLIQUE(2.88),  // TODO: confirm real price/gramme
    HUILE(1.99),             // TODO: confirm real price/gramme
    EAU_DISTILLEE(1.75);     // TODO: confirm real price/gramme

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    SOLV_SUPP_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
