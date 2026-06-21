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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for SignUpStepOneView.fxml.
 *
 * Validates the email isn't already taken and that both password
 * fields match, then stashes them in SignUpDraft and moves to step 2
 * (nom/prénom) before the Client is actually created.
 */
public class SignUpStepOneController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField pwdField;

    @FXML
    private PasswordField confirmPwdField;

    @FXML
    private Text errorText;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label loginLink;

    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAccountButton.setOnAction(e -> handleNext());
        loginLink.setOnMouseClicked(e -> SceneManager.switchTo("/view/fxml/LoginView.fxml"));
    }

    private void handleNext() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pwd = pwdField.getText() == null ? "" : pwdField.getText();
        String confirmPwd = confirmPwdField.getText() == null ? "" : confirmPwdField.getText();

        if (email.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
            showError("Merci de remplir tous les champs.");
            return;
        }

        if (pwd.length() < 8) {
            showError("Le mot de passe doit contenir au moins 8 caractères.");
            return;
        }

        if (!pwd.equals(confirmPwd)) {
            showError("Les mots de passe ne correspondent pas.");
            return;
        }

        if (clientDAO.findByEmail(email) != null) {
            showError("Un compte existe déjà avec cet email.");
            return;
        }

        hideError();
        SignUpDraft.setCredentials(email, pwd);
        SceneManager.switchTo("/view/fxml/SignUpStepTwoView.fxml");
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
