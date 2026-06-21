package com.example.osmar;

import model.Client;
import model.Commande;
import model.Ingredient;

/**
 * Holds state that needs to survive across scene switches —
 * JavaFX/FXML gives no built-in way to pass data between
 * SceneManager.switchTo(...) calls, so every controller reads/writes
 * through this single shared instance instead.
 *
 * - currentClient: set by LoginController/SignUp once authenticated.
 * - currentCommande: the order currently being built (cart). Created
 *   fresh when the user starts a new order (HomeController ->
 *   "Faire une Commande"), persisted across Catalog -> Cart ->
 *   Quantity -> CompositionSummary -> Payment -> Location ->
 *   Confirmation, then cleared/reset for the next order.
 * - selectedIngredient: the single Ingredient the user just clicked
 *   on in the Catalog, read by ProductDetailController when the
 *   popup opens.
 */
public final class Session {

    private static Client currentClient;
    private static Commande currentCommande;
    private static Ingredient selectedIngredient;

    private Session() {
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Commande getCurrentCommande() {
        return currentCommande;
    }

    public static void setCurrentCommande(Commande commande) {
        currentCommande = commande;
    }

    /**
     * Starts a brand new order tied to the logged-in client, with
     * tomorrow's date as a placeholder delivery date (adjust later
     * once a real delivery-date picker exists).
     */
    public static Commande startNewCommande() {
        if (currentClient == null) {
            throw new IllegalStateException("Impossible de démarrer une commande sans client connecté.");
        }
        java.time.LocalDate inAWeek = java.time.LocalDate.now().plusDays(7);
        currentCommande = new Commande(
                currentClient.getID(),
                inAWeek.getYear(),
                inAWeek.getMonthValue(),
                inAWeek.getDayOfMonth()
        );
        return currentCommande;
    }

    public static Ingredient getSelectedIngredient() {
        return selectedIngredient;
    }

    public static void setSelectedIngredient(Ingredient ingredient) {
        selectedIngredient = ingredient;
    }

    /** Clears everything — call after logout or after a confirmed order. */
    public static void clear() {
        currentClient = null;
        currentCommande = null;
        selectedIngredient = null;
    }
}
