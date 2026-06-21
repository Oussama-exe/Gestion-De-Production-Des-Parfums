package model;

public class Solvant_Support extends Ingredient {

    private SOLV_SUPP_enum type;

    // Constructeurs
    public Solvant_Support(String nom, double quantite, boolean disponibilite, SOLV_SUPP_enum type) {
        super(nom, quantite, disponibilite);
        this.type = type;
    }

    // Getters
    public SOLV_SUPP_enum getType() { return type; }

    // Setters
    public void setType(SOLV_SUPP_enum type) { this.type = type; }

    // Methods
    @Override
    public double calculer_prix() {
        return prix = this.quantite * type.getPrixParGramme();
    }
}
