package model;

public class Fixateur extends Ingredient {

    private FIX_enum type;

    // Constructeurs
    public Fixateur(String nom, double quantite, boolean disponibilite, FIX_enum type) {
        super(nom, quantite, disponibilite);
        this.type = type;
    }

    // Getters
    public FIX_enum getType() { return type; }

    // Setters
    public void setType(FIX_enum type) { this.type = type; }

    // Methods
    @Override
    public double calculer_prix() {
        return prix = this.quantite * type.getPrixParGramme();
    }
}
