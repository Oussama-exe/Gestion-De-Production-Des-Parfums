package model;

public enum MPN_enum {
    FLORAL(5.5),
    BOISEE(0.5),
    AGRUME(0.33),
    EPICE(2.55),
    RESINE_BAUME(0.22),
    RACINE_MOUSSE(2.1),
    ANIMAL(25.0);

    // Prix en Dollars $$$ par gramme
    private final double prixParGramme;

    MPN_enum(double prixParGramme) {
        this.prixParGramme = prixParGramme;
    }

    public double getPrixParGramme() {
        return prixParGramme;
    }
}
