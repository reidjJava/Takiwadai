package me.reidj.application;

import javafx.application.Application;
import javafx.stage.Stage;
import me.reidj.application.scene.PrimaryStage;
import me.reidj.application.scene.login.LoginScene;

public class App extends Application {

    private PrimaryStage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = new PrimaryStage(stage);
        LoginScene loginScene = new LoginScene();

        primaryStage.setScene(loginScene.getScene());
    }

    public static void main(String[] args) {
        launch();
    }

    public PrimaryStage getPrimaryStage() {
        return primaryStage;
    }
}