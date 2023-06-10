package me.reidj.application.scene;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.reidj.application.util.PathHelper;

import java.io.FileNotFoundException;

public record PrimaryStage(Stage stage) {

    public void setScene(Scene scene) {
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.sizeToScene();
        stage.setTitle("Takiwadai");
        stage.setResizable(false);
        try {
            Image icon = new Image(PathHelper.getResource("/images/compact_logo.png").getPath());
            stage.getIcons().add(icon);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        show();
    }

    public void showAlert(Alert.AlertType alertType, String contextText, String headerText) {
        Alert alert = new Alert(alertType, contextText);
        alert.setTitle(alertType == Alert.AlertType.ERROR ? "Ошибка!" : "Успешно!");
        alert.setHeaderText(headerText);
        alert.show();
    }

    public void show() {
        stage.show();
    }
}
