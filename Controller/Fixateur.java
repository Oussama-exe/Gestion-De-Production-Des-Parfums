public class Fixateur extends Ingredient implements Fix_inter{
    private FIX_enum type;
    
    //Constructeurs
    public Fixateur(String nom, double quantite, boolean disponibilite, FIX_enum type){
        super(nom, quantite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch(this.type){
            case RESINES_NATURELLE:
                return prix = (this.quantite)*RESINES_NATURELLE_PAR_GRAMME;
            case MUSCS_SYNTHESE:
                return prix = (this.quantite)*MUSCS_SYNTHESE_PAR_GRAMME;
            case AMBRE:
                return prix = (this.quantite)*AMBRE_PAR_GRAMME;
        }
        return -1.0;
    }
}
