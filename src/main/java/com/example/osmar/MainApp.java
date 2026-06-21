package com.example.osmar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Application entry point for Essence — Maison de Parfum.
 *
 * Loads the Welcome screen first. Use {@link SceneManager} from any
 * controller to navigate between the FXML views listed in
 * {@code /fxml/*.fxml}. All views currently render static content;
 * wire up real event handlers / data in their respective Controllers.
 */
public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        stage.setTitle("Essence — Maison de Parfum");
        stage.setMinWidth(960);
        stage.setMinHeight(640);

        Parent root = loadFxml("/view/fxml/WelcomeView.fxml");
        Scene scene = new Scene(root, 1080, 720);
        scene.getStylesheets().add(
                Objects.requireNonNull(MainApp.class.getResource("/view/css/theme.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads an FXML file from the classpath (e.g. "/fxml/LoginView.fxml")
     * and returns its root node, ready to be placed into a Scene.
     */
    static Parent loadFxml(String fxmlClasspathLocation) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlClasspathLocation));
        return loader.load();
    }

    static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
