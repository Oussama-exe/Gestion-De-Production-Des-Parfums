package model;

public class Matiere_synthetique extends Ingredient {

    private MS_enum type;

    // Constructeurs
    public Matiere_synthetique(String nom, double quantite, boolean disponibilite, MS_enum type) {
        super(nom, quantite, disponibilite);
        this.type = type;
    }

    // Getters
    public MS_enum getType() { return type; }

    // Setters
    public void setType(MS_enum type) { this.type = type; }

    // Methods
    @Override
    public double calculer_prix() {
        return prix = this.quantite * type.getPrixParGramme();
    }
}
