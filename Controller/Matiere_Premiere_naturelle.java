public class Matiere_Premiere_naturelle extends Ingredient implements MPN_inter{
    private MPN_enum type;

    //Constructeurs
    public Matiere_Premiere_naturelle(String nom, double quantite, boolean disponibilite, MPN_enum type){
        super(nom, quantite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch (this.type) {
            case FLORAL:
                return prix = (this.quantite)*FLORAL_PAR_GRAMME;
            case BOISEE:
                return prix = (this.quantite)*BOISEE_PAR_GRAMME;
            case AGRUME:
                return prix = (this.quantite)*AGRUME_PAR_GRAMME;
            case EPICE:
                return prix = (this.quantite)*EPICE_PAR_GRAMME;
            case RESINE_BAUME:
                return prix = (this.quantite)*RESINE_BAUME_PAR_GRAMME;
            case RACINE_MOUSSE:
                return prix = (this.quantite)*RACINE_MOUSSE_PAR_GRAMME;
            case ANIMAL:
                return prix = (this.quantite)*ANIMAL_PAR_GRAMME;
            }
            return -1.0;
    }
}
