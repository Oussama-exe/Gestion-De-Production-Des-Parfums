public class Commande<T>{
    private int id;
    private HashSet<T> elemnts_cmd;
    private double montant_total;
    private LocalDate date_cmd;
    private LOcalDAte date_livraison;

    //Constructeurs
    public Commande(int yyyy,int mm,int dd){
        this.elements_cmd = new HashSet<>();
        this.date_cmd = LocalDate.now();
        this.date_livraison = LocalDate.of(yyyy, mm, dd);
    }
    public Commande(Commande<T> cmd){
        this.elemnts_cmd.addAll(cmd.elemnts_cmd);
        this.date_cmd = cmd.date_cmd;
        this.date_livraison = cmd.date_livraison;
    }

    //Getters
    public int getID(){return id;}
    public HashSet<T> getElemnts_cmd(){return elemnts_cmd;}
    public double getMontant_total(){return montant_total;}
    public LocalDAte getDate_cmd(){return date_cmd;}
    public LocalDAte getDate_livraison(){return date_livraison;}

    //Setters
    public void setElemnts_cmd(HashSet<T> elem_cmd){this.elemnts_cmd.addAll(elem_cmd);}
    public void setDate_cmd(){this.date_cmd = LocalDate.now();}
    public void setDate_livraison1(LocalDate date_liv){this.date_livraison = date_liv;}
    public void setDate_livraison2(int yyyy, int mm,int dd){this.date_livraison = LocalDate.of(yyyy, mm, dd);}

    //Methods
    public double calcul_montant_total(){
        double mtt = 0.0;
        for(T i : this.elemnts_cmd){
            mtt += i.calculer_prix();
        }
        return this.montant_total = mtt;
    }
}