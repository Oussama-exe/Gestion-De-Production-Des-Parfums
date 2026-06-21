package controller;

import dao.ClientDAO;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import model.Paiement;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for PaymentMethodView.fxml.
 *
 * DESIGN NOTE: Paiement is a field on Client, not on Commande — there
 * is no payment-method column on the commande table, and Commande.java
 * has no Paiement field. So "choosing a payment method" here updates
 * the logged-in client's stored preference (Client.paiement) via
 * ClientDAO.update, rather than something tied to this specific
 * order. If you want per-order payment method instead, that needs a
 * new column on `commande` plus a field on Commande.java.
 */
public class PaymentMethodController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private HBox cardOption;

    @FXML
    private RadioButton cardRadio;

    @FXML
    private ToggleGroup paymentGroup;

    @FXML
    private HBox paypalOption;

    @FXML
    private RadioButton paypalRadio;

    @FXML
    private HBox visaOption;

    @FXML
    private RadioButton visaRadio;

    @FXML
    private HBox cashOption;

    @FXML
    private RadioButton cashRadio;

    @FXML
    private Button confirmButton;

    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Clicking anywhere on the row selects its radio, and updates
        // the visual "selected" styling on the HBox.
        cardOption.setOnMouseClicked(e -> selectOption(cardRadio, cardOption));
        paypalOption.setOnMouseClicked(e -> selectOption(paypalRadio, paypalOption));
        visaOption.setOnMouseClicked(e -> selectOption(visaRadio, visaOption));
        cashOption.setOnMouseClicked(e -> selectOption(cashRadio, cashOption));

        confirmButton.setOnAction(e -> handleConfirm());
    }

    private void selectOption(RadioButton radio, HBox option) {
        radio.setSelected(true);
        for (HBox box : new HBox[]{cardOption, paypalOption, visaOption, cashOption}) {
            box.getStyleClass().setAll(box == option ? "payment-option-selected" : "payment-option");
        }
    }

    private void handleConfirm() {
        Paiement chosen;
        if (cardRadio.isSelected()) {
            chosen = Paiement.CARTE_BANQUAIRE;
        } else if (paypalRadio.isSelected()) {
            chosen = Paiement.PAYPAL;
        } else if (visaRadio.isSelected()) {
            chosen = Paiement.VISA;
        } else if (cashRadio.isSelected()) {
            chosen = Paiement.ESPECE;
        } else {
            return; // nothing selected — shouldn't happen since cardRadio defaults to selected
        }

        if (Session.getCurrentClient() != null) {
            Session.getCurrentClient().setPaiement(chosen);
            clientDAO.update(Session.getCurrentClient());
        }

        SceneManager.switchTo("/view/fxml/LocationView.fxml");
    }
}
