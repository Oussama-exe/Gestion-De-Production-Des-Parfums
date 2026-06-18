public class Solvant_Support extends Ingredient implements SOLV_SUPP_inter{
    private SOLV_SUPP_enum type;

    //Constructeurs
    public Solvant_Support(String nom, double quantite, Qualite qualite, boolean disponibilite, SOLV_SUPP_enum type){
        super(nom, quantite, qualite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch(this.type){
            case ALCOOL_ETHYLIQUE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*ALCOOL_ETHYLIQUE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*ALCOOL_ETHYLIQUE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*ALCOOL_ETHYLIQUE_PAR_GRAMME;
                }
                break;
            case HUILE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*HUILE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*HUILE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*HUILE_PAR_GRAMME;
                }
                break;
            case EAU_DISTILLEE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*EAU_DISTILLEE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*EAU_DISTILLEE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*EAU_DISTILLEE_PAR_GRAMME;
                }
                break;
        }
        return -1.0;
    }
}
