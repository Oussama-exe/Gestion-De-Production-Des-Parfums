package controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import com.example.osmar.SceneManager;
import com.example.osmar.Session;
import com.example.osmar.SignUpDraft;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for WelcomeView.fxml.
 * "Suivant" navigates to the Login screen.
 */
public class WelcomeController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Circle orbOne;

    @FXML
    private Circle orbTwo;

    @FXML
    private Text welcomeTitle;

    @FXML
    private Button nextButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playEntranceAnimation();

        nextButton.setOnAction(e -> {SceneManager.switchTo("/view/fxml/LoginView.fxml");
            System.out.println("btn clicked!");
        });
    }

    /**
     * Fades the title in while it rises slightly, and softly fades the
     * "Suivant" button in just after — a calm, deliberate entrance that
     * fits the brand's unhurried, elegant tone.
     */
    private void playEntranceAnimation() {
        welcomeTitle.setOpacity(0);
        welcomeTitle.setTranslateY(welcomeTitle.getTranslateY() + 18);
        nextButton.setOpacity(0);

        FadeTransition titleFade = new FadeTransition(Duration.millis(700), welcomeTitle);
        titleFade.setFromValue(0);
        titleFade.setToValue(1);

        TranslateTransition titleRise = new TranslateTransition(Duration.millis(700), welcomeTitle);
        titleRise.setToY(welcomeTitle.getTranslateY() - 18);
        titleRise.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition buttonFade = new FadeTransition(Duration.millis(500), nextButton);
        buttonFade.setFromValue(0);
        buttonFade.setToValue(1);
        buttonFade.setDelay(Duration.millis(450));

        new ParallelTransition(titleFade, titleRise, buttonFade).play();
    }
}
