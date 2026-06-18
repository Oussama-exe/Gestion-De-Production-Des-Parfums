public class Matiere_synthetique extends Ingredient implements MS_inter{
    private MS_enum type;

    //Constructeurs
    public Matiere_synthetique(String nom, double quantite, Qualite qualite, boolean disponibilite, MS_enum type){
        super(nom, quantite, qualite, disponibilite);
        this.type = type;
    }
    //Methods
    @Override
    public double calcul_prix(){
        switch(this.type){
            case MUSCS_SYNTHETIQUE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*MUSCS_SYNTHETIQUE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*MUSCS_SYNTHETIQUE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*MUSCS_SYNTHETIQUE_PAR_GRAMME;
                }
                break;
            case ALDEHYDE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*ALDEHYDE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*ALDEHYDE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*ALDEHYDE_PAR_GRAMME;
                }
                break;
            case CETONE:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*CETONE_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*CETONE_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*CETONE_PAR_GRAMME;
                }
                break;
            case MOLECULES_ESOLES:
                switch(this.qualite){
                    case MAUVAISE:
                        return prix = (this.quantite + QUALITE_MAUVAISE)*MOLECULES_ESOLES_PAR_GRAMME;
                    case NORMALE:
                        return prix = (this.quantite + QUALITE_NORMALE)*MOLECULES_ESOLES_PAR_GRAMME;
                    case BONNE:
                        return prix = (this.quantite + QUALITE_BONNE)*MOLECULES_ESOLES_PAR_GRAMME;
                }
                break;
        }
        return -1.0;
    }
}
