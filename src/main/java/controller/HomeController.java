package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for HomeView.fxml.
 *
 * "Faire une Commande" starts a brand new Commande in Session and
 * goes to the Catalog. "Voir mes historiques" goes to OrderHistory.
 * greetingText shows "Bonjour, {prénom}" for the logged-in client,
 * replacing the static "Essence" wordmark.
 */
public class HomeController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Text greetingText;

    @FXML
    private Button profileButton;

    @FXML
    private Button orderButton;

    @FXML
    private Button historyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Session.getCurrentClient() != null) {
            greetingText.setText("Bonjour, " + Session.getCurrentClient().getPrenom());
        }

        orderButton.setOnAction(e -> {
            Session.startNewCommande();
            SceneManager.switchTo("/view/fxml/CatalogView.fxml");
        });

        historyButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/OrderHistoryView.fxml"));

        // No profile/account-settings screen exists in the architecture
        // diagram yet, so profileButton has nowhere to navigate to.
        // Leaving it inert rather than guessing a destination.
    }
}