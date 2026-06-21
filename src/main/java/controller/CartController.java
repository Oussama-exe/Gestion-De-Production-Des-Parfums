package controller;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
 * Controller for CartView.fxml.
 *
 * Renders Session.getCurrentCommande().getElementsCmd() as rows,
 * lets the user remove items, keeps the subtotal/count in sync, and
 * "Confirmer ma commande" moves on to Quantity selection.
 */
public class CartController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button closeButton;

    @FXML
    private VBox cartRowsContainer;

    @FXML
    private Text subtotalText;

    @FXML
    private Text articleCountText;

    @FXML
    private Button confirmButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderCart();

        closeButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/CatalogView.fxml"));

        confirmButton.setOnAction(e -> {
            if (Session.getCurrentCommande() == null || Session.getCurrentCommande().getElementsCmd().isEmpty()) {
                return;
            }
            SceneManager.switchTo("/view/fxml/QuantityView.fxml");
        });
    }

    private void renderCart() {
        cartRowsContainer.getChildren().clear();

        if (Session.getCurrentCommande() == null) {
            updateSummary(0, 0.0);
            return;
        }

        for (Ingredient ingredient : Session.getCurrentCommande().getElementsCmd()) {
            HBox row = IngredientCardFactory.buildCartRow(ingredient, this::handleRemove);
            cartRowsContainer.getChildren().add(row);
        }

        updateSummary(
                Session.getCurrentCommande().getElementsCmd().size(),
                Session.getCurrentCommande().calcul_montant_total()
        );
    }

    private void handleRemove(Ingredient ingredient) {
        if (Session.getCurrentCommande() != null) {
            Session.getCurrentCommande().getElementsCmd().remove(ingredient);
        }
        renderCart();
    }

    private void updateSummary(int count, double total) {
        subtotalText.setText(String.format("%.2f€", total));
        articleCountText.setText(count + (count == 1 ? " essence" : " essences"));
    }
}
