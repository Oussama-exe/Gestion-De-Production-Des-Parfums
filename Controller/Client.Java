public class Client{
    private int id;
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private Commande cmd; //tab de cmds
    private Paiement paiement;//enum
    private String commentaire;

    //Constructeurs
    public Client(String nom, String prenom, String adresse, String email){
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
    }
    public Client(Commande cmd, Paiement pym, String cmt){
        this.cmd = new Commande(cmd);
        this.paiement = pym;
        this.commentaire = cmt;
    }
    public Client(String nom, String prenom, String adresse, String email, Commande cmd, Paiement pym, String cmt){
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.email = email;
        this.cmd = new Commande(cmd);
        this.paiement = pym;
        this.commentaire = cmt;
    }

    //Getters
    public int getID(){return id;}
    public String getNom(){return nom;}
    public String getPrenom(){return prenom;}
    public String getAdresse(){return adresse;}
    public String getEmail(){return email;}
    public Commande getCommande(){return cmd;}
    public Paiement getPaiement(){return paiement;}
    public String getCommentaire(){return commentaire;}

    //Setters
    public void setNom(String nom){this.nom = nom;}
    public void setPrenom(String prenom){this.prenom = prenom;}
    public void setAdresse(String adresse){this.adresse = adresse;}
    public void setEmail(String email){this.email = email;}
    public void setCommande(Commande cmd){this.cmd = cmd;}
    public void setPaiement(Paiement pym){this.paiement = pym;}
    public void setCommentaire(String cmt){this.commentaire = cmt;}

    //Methods
    public void commander(HashSet<T> cmd, LocalDate date_liv){
        this.cmd.setElemnts_cmd(cmd);
        this.setDate_cmd1();
        this.setDate_livraison1(date_liv);
    }
}