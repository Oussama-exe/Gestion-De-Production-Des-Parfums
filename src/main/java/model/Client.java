package model;
public class Client{
    private int id;
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String pwd;
    private Paiement paiement;

    //Constructeur
    public Client(){}
    
    public Client(String nom, String prenom, String email, String pwd){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
    }

    //Getters
    public int getID(){return id;}
    public String getNom(){return nom;}
    public String getPrenom(){return prenom;}
    public String getAdresse(){return adresse;}
    public String getEmail(){return email;}
    public String getPwd(){return pwd;}
    public Paiement getPaiement(){return paiement;}

    //Setters
    public void setID(int id){this.id = id;}
    public void setNom(String nom){this.nom = nom;}
    public void setPrenom(String prenom){this.prenom = prenom;}
    public void setAdresse(String adresse){this.adresse = adresse;}
    public void setEmail(String email){this.email = email;}
    public void setPwd(String pwd){this.pwd = pwd;}
    public void setPaiement(Paiement pym){this.paiement = pym;}
}