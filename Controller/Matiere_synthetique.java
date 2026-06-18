public class Matiere_synthetique extends Ingredient implements MS_inter{
    private MS_enum type;

    //Constructeurs
    public Matiere_synthetique(String nom, double quantite, boolean disponibilite, MS_enum type){
        super(nom, quantite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch(this.type){
            case MUSCS_SYNTHETIQUE:
                return prix = (this.quantite)*MUSCS_SYNTHETIQUE_PAR_GRAMME;
            case ALDEHYDE:
                return prix = (this.quantite)*ALDEHYDE_PAR_GRAMME;
            case CETONE:
                return prix = (this.quantite)*CETONE_PAR_GRAMME;
            case MOLECULES_ESOLES:
                return prix = (this.quantite)*MOLECULES_ESOLES_PAR_GRAMME;
        }
        return -1.0;
    }
}
