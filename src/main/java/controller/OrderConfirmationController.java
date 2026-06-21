package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for OrderConfirmationView.fxml.
 *
 * "Ajouter une autre commande" starts a brand new Commande and
 * returns to the Catalog. "Revenir à l'accueil" clears the
 * in-progress order and goes back to HomeView.
 */
public class OrderConfirmationController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button addOtherOrderButton;

    @FXML
    private Button backHomeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addOtherOrderButton.setOnAction(e -> {
            Session.startNewCommande();
            SceneManager.switchTo("/view/fxml/CatalogView.fxml");
        });

        backHomeButton.setOnAction(e -> {
            Session.setCurrentCommande(null);
            SceneManager.switchTo("/view/fxml/HomeView.fxml");
        });
    }
}
