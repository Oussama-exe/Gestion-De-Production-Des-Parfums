package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import model.Ingredient;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for ProductDetailView.fxml.
 *
 * Reads the ingredient the user clicked on (set by CatalogController
 * via Session.setSelectedIngredient) and displays its real details.
 *
 * NOTE ON NAVIGATION: this view is a full-screen overlay in the FXML
 * (StackPane covering the whole window), not a true popup floating
 * over the Catalog — SceneManager.switchTo replaces the whole scene
 * root, so the Catalog is fully hidden underneath while this is
 * showing, not visible-but-dimmed behind it. "✕" and "Ajouter à la
 * commande" both navigate back to the Catalog rather than literally
 * closing a popup. If you want a true floating modal later, this
 * would need to become a separate Stage (or a popup library) instead
 * of a full FXML scene swap.
 */
public class ProductDetailController implements Initializable {

    @FXML
    private StackPane overlayRoot;

    @FXML
    private Text emojiText;

    @FXML
    private Text tagText;

    @FXML
    private Text nameText;

    @FXML
    private Text categoryText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text availabilityText;

    @FXML
    private Text priceText;

    @FXML
    private Button closeButton;

    @FXML
    private Button addToCartButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displaySelectedIngredient();

        closeButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/CatalogView.fxml"));

        addToCartButton.setOnAction(e -> handleAddToCart());
    }

    private void displaySelectedIngredient() {
        Ingredient ingredient = Session.getSelectedIngredient();
        if (ingredient == null) {
            // Defensive fallback — shouldn't happen in normal flow since
            // this screen is only reached by clicking a product card.
            nameText.setText("Ingrédient introuvable");
            descriptionText.setText("");
            addToCartButton.setDisable(true);
            return;
        }

        emojiText.setText(IngredientCardFactory.emojiFor(ingredient));
        tagText.setText(IngredientCardFactory.typeLabelFor(ingredient));
        nameText.setText(ingredient.getNom());
        categoryText.setText(IngredientCardFactory.familyLabelFor(ingredient));
        descriptionText.setText(
                "Ingrédient de la famille " + IngredientCardFactory.familyLabelFor(ingredient).toLowerCase()
                        + ", de type " + IngredientCardFactory.typeLabelFor(ingredient).toLowerCase() + ".");
        availabilityText.setText(ingredient.getDisponibilite() ? "En stock" : "Indisponible");
        priceText.setText(String.format("%.2f€ / g", pricePerGram(ingredient)));

        addToCartButton.setDisable(!ingredient.getDisponibilite());
    }

    /**
     * calculer_prix() needs a quantite to multiply against, but here
     * we just want the unit price/gram for display — temporarily set
     * quantite to 1 to read it off, since none of the 4 Ingredient
     * subclasses expose getPrixParGramme() directly (it lives on
     * their respective enum's getType()).
     */
    private double pricePerGram(Ingredient ingredient) {
        double originalQuantite = ingredient.getQuantite();
        ingredient.setQuantite(1.0);
        double pricePerGram = ingredient.calculer_prix();
        ingredient.setQuantite(originalQuantite);
        return pricePerGram;
    }

    private void handleAddToCart() {
        Ingredient ingredient = Session.getSelectedIngredient();
        if (ingredient == null || Session.getCurrentCommande() == null) {
            SceneManager.switchTo("/view/fxml/CatalogView.fxml");
            return;
        }

        // Default to 1 gram when adding from the catalog detail screen —
        // QuantityController lets the user adjust the overall order
        // quantity later in the flow.
        ingredient.setQuantite(1.0);
        Session.getCurrentCommande().addIngredient(ingredient);

        SceneManager.switchTo("/view/fxml/CatalogView.fxml");
    }
}
