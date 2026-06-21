package model;

public class Matiere_Premiere_naturelle extends Ingredient {

    private MPN_enum type;

    // Constructeurs
    public Matiere_Premiere_naturelle(String nom, double quantite, boolean disponibilite, MPN_enum type) {
        super(nom, quantite, disponibilite);
        this.type = type;
    }

    // Getters
    public MPN_enum getType() { return type; }

    // Setters
    public void setType(MPN_enum type) { this.type = type; }

    // Methods
    @Override
    public double calculer_prix() {
        return prix = this.quantite * type.getPrixParGramme();
    }
}
