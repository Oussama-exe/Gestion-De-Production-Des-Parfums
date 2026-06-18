public class Matiere_Premiere_naturelle extends Ingredient implements MPN_inter{
    private MPN_enum type;

    //Constructeurs
    public Matiere_Premiere_naturelle(String nom, double quantite, Qualite qualite, boolean disponibilite, MPN_enum type){
        super(nom, quantite, qualite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch (this.type) {
            case FLORAL:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*FLORAL_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*FLORAL_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*FLORAL_PAR_GRAMME;
                }
                break;
            case BOISEE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*BOISEE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*BOISEE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*BOISEE_PAR_GRAMME;
                }
                break;
            case AGRUME:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*AGRUME_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*AGRUME_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*AGRUME_PAR_GRAMME;
                }
                break;
            case EPICE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*EPICE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*EPICE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*EPICE_PAR_GRAMME;
                }
                break;
            case RESINE_BAUME:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*RESINE_BAUME_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*RESINE_BAUME_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*RESINE_BAUME_PAR_GRAMME;
                }
                break;
            case RACINE_MOUSSE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*RACINE_MOUSSE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*RACINE_MOUSSE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*RACINE_MOUSSE_PAR_GRAMME;
                }
                break;
            case ANIMAL:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*ANIMAL_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*ANIMAL_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*ANIMAL_PAR_GRAMME;
                }
                break;
            default:
                throw new AssertionError();
            }
        return -1.0;
    }
}
