public class Fixateur extends Ingredient implements Fix_inter{
    private FIX_enum type;
    
    //Constructeurs
    public Fixateur(String nom, double quantite, Qualite qualite, boolean disponibilite, FIX_enum type){
        super(nom, quantite, qualite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch(this.type){
            case RESINES_NATURELLE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*RESINES_NATURELLE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*RESINES_NATURELLE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*RESINES_NATURELLE_PAR_GRAMME;
                }
                break;
            case MUSCS_SYNTHESE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*MUSCS_SYNTHESE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*MUSCS_SYNTHESE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*MUSCS_SYNTHESE_PAR_GRAMME;
                }
                break;
            case AMBRE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*AMBRE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*AMBRE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*AMBRE_PAR_GRAMME;
                }
                break;
        }
        return -1.0;
    }
}
