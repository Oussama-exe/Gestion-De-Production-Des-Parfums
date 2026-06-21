package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for QuantityView.fxml.
 *
 * Sets the chosen bottle size (ml) directly on Commande.quantite —
 * this is the order's overall volume, separate from each
 * Ingredient.quantite (grams of that ingredient in the recipe,
 * tracked individually in ingredients_cmd).
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
        if (Session.getCurrentCommande() != null && Session.getCurrentCommande().getQuantite() > 0) {
            quantitySlider.setValue(Session.getCurrentCommande().getQuantite());
        }

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

        Session.getCurrentCommande().setQuantite(quantitySlider.getValue());

        SceneManager.switchTo("/view/fxml/CompositionSummaryView.fxml");
    }
}
