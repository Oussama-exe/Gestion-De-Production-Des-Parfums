package controller;

import dao.ClientDAO;
import dao.CommandeDAO;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for LocationView.fxml.
 *
 * Saves the typed address onto the client (Client.adresse — there's
 * no separate location/address table in the schema), then finalizes
 * the order: recomputes the total, saves the Commande + its
 * ingredients via CommandeDAO.insertWithIngredients, and moves on to
 * the confirmation screen.
 *
 * "Utiliser ma position" (GPS) has no real geolocation wired up —
 * there's no mapping/geocoding library in this project — so it's
 * left as a visual affordance only for now.
 */
public class LocationController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField addressField;

    @FXML
    private Button useGpsButton;

    @FXML
    private Button savedAddressButton;

    @FXML
    private Button nextButton;

    private final ClientDAO clientDAO = new ClientDAO();
    private final CommandeDAO commandeDAO = new CommandeDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Session.getCurrentClient() != null && Session.getCurrentClient().getAdresse() != null) {
            addressField.setText(Session.getCurrentClient().getAdresse());
        }

        if (Session.getCurrentClient() != null && Session.getCurrentClient().getAdresse() != null) {
            savedAddressButton.setOnAction(e -> addressField.setText(Session.getCurrentClient().getAdresse()));
        } else {
            savedAddressButton.setDisable(true);
        }

        nextButton.setOnAction(e -> handleConfirmOrder());
    }

    private void handleConfirmOrder() {
        String address = addressField.getText() == null ? "" : addressField.getText().trim();

        if (address.isEmpty()) {
            return;
        }

        if (Session.getCurrentClient() != null) {
            Session.getCurrentClient().setAdresse(address);
            clientDAO.update(Session.getCurrentClient());
        }

        if (Session.getCurrentCommande() != null) {
            Session.getCurrentCommande().calcul_montant_total();
            commandeDAO.insertWithIngredients(Session.getCurrentCommande());
        }

        SceneManager.switchTo("/view/fxml/OrderConfirmationView.fxml");
    }
}
