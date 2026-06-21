package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Commande {
    private int id;
    private int id_client;
    private Set<Ingredient> elements_cmd;
    private double montant_total;
    private LocalDate date_cmd;
    private LocalDate date_livraison;

    // Constructeurs
    public Commande() {
        this.elements_cmd = new HashSet<>();
    }

    public Commande(int id_client, int yyyy, int mm, int dd) {
        this.id_client = id_client;
        this.elements_cmd = new HashSet<>();
        this.date_cmd = LocalDate.now();
        this.date_livraison = LocalDate.of(yyyy, mm, dd);
    }

    public Commande(Commande cmd) {
        this.id_client = cmd.id_client;
        this.elements_cmd = new HashSet<>();
        this.elements_cmd.addAll(cmd.elements_cmd);
        this.date_cmd = cmd.date_cmd;
        this.date_livraison = cmd.date_livraison;
    }

    // Getters
    public int getID() { return id; }
    public int getIdClient() { return id_client; }
    public Set<Ingredient> getElementsCmd() { return elements_cmd; }
    public double getMontantTotal() { return montant_total; }
    public LocalDate getDateCmd() { return date_cmd; }
    public LocalDate getDateLivraison() { return date_livraison; }

    // Setters
    public void setID(int id) { this.id = id; }
    public void setIdClient(int id_client) { this.id_client = id_client; }
    public void setElementsCmd(Set<Ingredient> elemCmd) { this.elements_cmd.addAll(elemCmd); }
    public void setMontantTotal(double montant_tt) { this.montant_total = montant_tt; }
    public void setDateCmd(LocalDate date_cmd) { this.date_cmd = date_cmd; }
    public void setDateLivraison(LocalDate date_liv) { this.date_livraison = date_liv; }

    // Methodes
    public void addIngredient(Ingredient ing) {
        this.elements_cmd.add(ing);
    }

    public double calcul_montant_total() {
        double mtt = 0.0;
        for (Ingredient i : this.elements_cmd) {
            mtt += i.calculer_prix();
        }
        this.montant_total = mtt;
        return this.montant_total;
    }
}
