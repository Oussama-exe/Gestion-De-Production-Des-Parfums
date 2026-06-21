package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
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
 * Controller for QuantityView.fxml.
 *
 * DESIGN NOTE: Commande has no "bottle size" / total-quantity field
 * anywhere in the model or the commande table — only individual
 * Ingredient.quantite exists. In the absence of a dedicated field,
 * this controller interprets "choose a quantity" as: split the
 * chosen total (in grams, using the ml value as a stand-in since
 * there's no density conversion anywhere in the model either) evenly
 * across whatever ingredients are already in the cart. This is an
 * assumption, not something derived from your schema — revisit if
 * you actually want quantity to mean something else (e.g. add a
 * real `volume_ml` column to `commande` instead).
 */
public class QuantityController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Text quantityValueText;

    @FXML
    private Slider quantitySlider;

    @FXML
    private Button preset30Button;

    @FXML
    private Button preset50Button;

    @FXML
    private Button preset100Button;

    @FXML
    private Button confirmButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantitySlider.valueProperty().addListener((obs, oldVal, newVal) ->
                updateDisplay(newVal.intValue()));

        preset30Button.setOnAction(e -> quantitySlider.setValue(30));
        preset50Button.setOnAction(e -> quantitySlider.setValue(50));
        preset100Button.setOnAction(e -> quantitySlider.setValue(100));

        confirmButton.setOnAction(e -> handleConfirm());

        updateDisplay((int) quantitySlider.getValue());
    }

    private void updateDisplay(int ml) {
        quantityValueText.setText(ml + " ml");

        for (Button b : new Button[]{preset30Button, preset50Button, preset100Button}) {
            b.getStyleClass().setAll("btn-pill-select");
        }
        if (ml == 30) preset30Button.getStyleClass().setAll("btn-pill-select-active");
        if (ml == 50) preset50Button.getStyleClass().setAll("btn-pill-select-active");
        if (ml == 100) preset100Button.getStyleClass().setAll("btn-pill-select-active");
    }

    private void handleConfirm() {
        if (Session.getCurrentCommande() == null) {
            SceneManager.switchTo("/view/fxml/CatalogView.fxml");
            return;
        }

        var ingredients = Session.getCurrentCommande().getElementsCmd();
        if (!ingredients.isEmpty()) {
            double totalMl = quantitySlider.getValue();
            double perIngredient = totalMl / ingredients.size();
            for (Ingredient ingredient : ingredients) {
                ingredient.setQuantite(perIngredient);
            }
        }

        SceneManager.switchTo("/view/fxml/CompositionSummaryView.fxml");
    }
}
