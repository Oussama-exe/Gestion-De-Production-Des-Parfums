package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Commande;

import java.time.format.DateTimeFormatter;

/**
 * Flattens a Commande into simple String properties so it can be
 * displayed in OrderHistoryView's TableView via PropertyValueFactory.
 * Commande itself isn't a JavaFX bean (no XxxProperty getters), so
 * this thin wrapper exists purely for the table to bind against.
 */
public class CommandeRow {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final StringProperty orderId;
    private final StringProperty date;
    private final StringProperty parfum;
    private final StringProperty quantite;
    private final StringProperty total;
    private final StringProperty statut;

    public CommandeRow(Commande commande) {
        this.orderId = new SimpleStringProperty("CMD-" + commande.getID());
        this.date = new SimpleStringProperty(
                commande.getDateCmd() != null ? commande.getDateCmd().format(DATE_FORMAT) : "—");
        this.parfum = new SimpleStringProperty(
                commande.getElementsCmd().size() + " ingrédient(s)");
        this.quantite = new SimpleStringProperty(String.valueOf(commande.getElementsCmd().size()));
        this.total = new SimpleStringProperty(String.format("%.2f€", commande.getMontantTotal()));
        this.statut = new SimpleStringProperty(
                commande.getDateLivraison() != null && commande.getDateLivraison().isBefore(java.time.LocalDate.now())
                        ? "Livrée"
                        : "En cours");
    }

    public String getOrderId() { return orderId.get(); }
    public String getDate() { return date.get(); }
    public String getParfum() { return parfum.get(); }
    public String getQuantite() { return quantite.get(); }
    public String getTotal() { return total.get(); }
    public String getStatut() { return statut.get(); }

    public StringProperty orderIdProperty() { return orderId; }
    public StringProperty dateProperty() { return date; }
    public StringProperty parfumProperty() { return parfum; }
    public StringProperty quantiteProperty() { return quantite; }
    public StringProperty totalProperty() { return total; }
    public StringProperty statutProperty() { return statut; }
}
