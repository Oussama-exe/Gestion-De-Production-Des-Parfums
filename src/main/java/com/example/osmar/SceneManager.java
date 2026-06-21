package com.example.osmar;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Tiny navigation helper so controllers can switch screens with one line,
 * e.g.:
 * <pre>
 *     SceneManager.switchTo("/fxml/LoginView.fxml");
 * </pre>
 *
 * This swaps the root of the primary Stage's Scene and re-applies the
 * shared theme stylesheet. Replace or extend this with your own
 * navigation/state-passing strategy once you wire up real events.
 */
public final class SceneManager {

    private SceneManager() {
    }

    public static void switchTo(String fxmlClasspathLocation) {
        try {
            Stage stage = MainApp.getPrimaryStage();
            Parent root = MainApp.loadFxml(fxmlClasspathLocation);

            Scene currentScene = stage.getScene();
            if (currentScene == null) {
                currentScene = new Scene(root, 1080, 720);
                stage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }

            currentScene.getStylesheets().setAll(
                    Objects.requireNonNull(SceneManager.class.getResource("/view/css/theme.css")).toExternalForm());

        } catch (IOException e) {
            throw new RuntimeException("Could not load view: " + fxmlClasspathLocation, e);
        }
    }
}
