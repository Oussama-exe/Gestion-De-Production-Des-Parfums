package controller;

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
 * Controller for CompositionSummaryView.fxml.
 *
 * Recap of the current order's ingredients (after quantity has been
 * distributed by QuantityController). "Modifier" returns to the
 * Catalog to adjust the selection; "Suivant" proceeds to payment.
 */
public class CompositionSummaryController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Text summaryCaptionText;

    @FXML
    private VBox ingredientRowsContainer;

    @FXML
    private Button modifyButton;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderSummary();

        modifyButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/CatalogView.fxml"));

        nextButton.setOnAction(e -> {
            if (Session.getCurrentCommande() == null || Session.getCurrentCommande().getElementsCmd().isEmpty()) {
                return;
            }
            SceneManager.switchTo("/view/fxml/PaymentMethodView.fxml");
        });
    }

    private void renderSummary() {
        ingredientRowsContainer.getChildren().clear();

        if (Session.getCurrentCommande() == null) {
            summaryCaptionText.setText("0 ml · 0 essence sélectionnée");
            return;
        }

        var ingredients = Session.getCurrentCommande().getElementsCmd();
        double totalMl = ingredients.stream().mapToDouble(Ingredient::getQuantite).sum();

        summaryCaptionText.setText(String.format("%.0f ml · %d essence%s sélectionnée%s",
                totalMl, ingredients.size(),
                ingredients.size() == 1 ? "" : "s",
                ingredients.size() == 1 ? "" : "s"));

        for (Ingredient ingredient : ingredients) {
            HBox row = IngredientCardFactory.buildSummaryRow(ingredient, this::handleRemove);
            ingredientRowsContainer.getChildren().add(row);
        }
    }

    private void handleRemove(Ingredient ingredient) {
        if (Session.getCurrentCommande() != null) {
            Session.getCurrentCommande().getElementsCmd().remove(ingredient);
        }
        renderSummary();
    }
}
