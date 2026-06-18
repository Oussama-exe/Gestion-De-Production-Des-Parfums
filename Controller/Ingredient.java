public abstract  class Ingredient{
    protected int id;
    protected String nom;
    protected double quantite;
    protected Qualite qualite;
    protected boolean disponilite;
    protected double prix;

    //Constructeurs
    public Ingredient(String nom, double quantite, Qualite qualite, boolean disponibilite){
        this.nom = nom;
        this.quantite = quantite;
        this.qualite = qualite;
        this.disponilite = disponibilite;
    }

    //Getters
    public String getNom(){return nom;}
    public double getQuantite(){return quantite;}
    public Qualite getQualite(){return qualite;}
    public boolean getDisponibilite(){return  disponilite;}
    
    //Setters
    public void setNom(String nom){this.nom = nom;}
    public void setQuantite(double quantite){ this.quantite = quantite;}
    public void setQualite(Qualite qualite){ this.qualite = qualite;}
    public void setDisponibilite(boolean disponibilite){ this.disponilite = disponibilite;}

    //Methods
    public abstract double calcul_prix();
}