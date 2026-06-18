public abstract  class Ingredient{
    protected int id;
    protected String nom;
    protected double quantite;
    protected boolean disponilite;
    protected double prix;

    //Constructeurs
    public Ingredient(String nom, double quantite, boolean disponibilite){
        this.nom = nom;
        this.quantite = quantite;
        this.disponilite = disponibilite;
    }

    //Getters
    public String getNom(){return nom;}
    public double getQuantite(){return quantite;}
    public boolean getDisponibilite(){return  disponilite;}
    
    //Setters
    public void setNom(String nom){this.nom = nom;}
    public void setQuantite(double quantite){ this.quantite = quantite;}
    public void setDisponibilite(boolean disponibilite){ this.disponilite = disponibilite;}

    //Methods
    public abstract double calcul_prix();
}