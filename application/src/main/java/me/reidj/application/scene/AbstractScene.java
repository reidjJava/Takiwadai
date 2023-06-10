package me.reidj.application.scene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public abstract class AbstractScene {

    private Scene scene;
    private final FXMLLoader fxmlLoader;

    public AbstractScene(String path) {
        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + path));
    }

    public Scene getScene() {
        if (scene == null) {
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return scene;
    }

    @FXML
    void exit() {
        System.exit(0);
    }
}
