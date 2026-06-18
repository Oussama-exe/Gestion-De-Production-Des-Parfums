public class Solvant_Support extends Ingredient implements SOLV_SUPP_inter{
    private SOLV_SUPP_enum type;

    //Constructeurs
    public Solvant_Support(String nom, double quantite, boolean disponibilite, SOLV_SUPP_enum type){
        super(nom, quantite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch(this.type){
            case ALCOOL_ETHYLIQUE:
                return prix = (this.quantite)*ALCOOL_ETHYLIQUE_PAR_GRAMME;
            case HUILE:
                return prix = (this.quantite)*HUILE_PAR_GRAMME;
            case EAU_DISTILLEE:
                return prix = (this.quantite)*EAU_DISTILLEE_PAR_GRAMME;
        }
        return -1.0;
    }
}
