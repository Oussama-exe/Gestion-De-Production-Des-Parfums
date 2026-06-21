package controller;

import dao.ClientDAO;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import model.Client;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for SignUpStepTwoView.fxml.
 *
 * Combines nom/prénom from this screen with the email/pwd stashed in
 * SignUpDraft (set by SignUpStepOneController), creates the Client
 * via ClientDAO, logs the new client in immediately, and goes to
 * HomeView (per the confirmed flow: signup -> auto-login -> home).
 */
public class SignUpStepTwoController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nextButton.setOnAction(e -> handleCreateAccount());
        backButton.setOnAction(e -> SceneManager.switchTo("/view/fxml/SignUpStepOneView.fxml"));
    }

    private void handleCreateAccount() {
        String nom = nomField.getText() == null ? "" : nomField.getText().trim();
        String prenom = prenomField.getText() == null ? "" : prenomField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty()) {
            // No error Text node on this screen in the current FXML —
            // simplest safe behavior is to just not proceed. Add an
            // errorText node here (same pattern as SignUpStepOne) if
            // you want an inline message instead.
            return;
        }

        String email = SignUpDraft.getEmail();
        String pwd = SignUpDraft.getPwd();

        if (email == null || pwd == null) {
            // SignUpDraft is empty — the user landed on this screen
            // without going through step 1 (e.g. browser-style back
            // button misuse). Send them back to start over cleanly.
            SceneManager.switchTo("/view/fxml/SignUpStepOneView.fxml");
            return;
        }

        Client client = new Client(nom, prenom, email, pwd);
        int generatedId = clientDAO.insert(client);
        client.setID(generatedId);

        SignUpDraft.clear();
        Session.setCurrentClient(client);
        SceneManager.switchTo("/view/fxml/HomeView.fxml");
    }
}
