package controller;

import dao.ClientDAO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import model.Client;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for LoginView.fxml.
 *
 * Looks up the client by email via ClientDAO, then compares the typed
 * password against the stored one as plain text (no hashing — fine
 * for this project's current scope; revisit before any real
 * deployment).
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField emailField;

    @FXML
    private Label forgotPasswordLink;

    @FXML
    private PasswordField pwdField;

    @FXML
    private Text errorText;

    @FXML
    private Button loginButton;

    @FXML
    private Label createAccountLink;

    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(e -> handleLogin());
        createAccountLink.setOnMouseClicked(e -> SceneManager.switchTo("/view/fxml/SignUpStepOneView.fxml"));

        // Forgot-password flow isn't built yet — no screen for it in the
        // architecture diagram. Leaving it inert rather than guessing
        // at a destination; revisit once that screen exists.
    }

    private void handleLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pwd = pwdField.getText() == null ? "" : pwdField.getText();

        if (email.isEmpty() || pwd.isEmpty()) {
            showError("Merci de renseigner votre email et votre mot de passe.");
            return;
        }

        Client client = clientDAO.findByEmail(email);

        if (client == null || !pwd.equals(client.getPwd())) {
            showError("Email ou mot de passe incorrect.");
            return;
        }

        hideError();
        Session.setCurrentClient(client);
        SceneManager.switchTo("/view/fxml/HomeView.fxml");
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
        errorText.setManaged(true);
    }

    private void hideError() {
        errorText.setVisible(false);
        errorText.setManaged(false);
    }
}
