package me.reidj.application;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import me.reidj.application.scene.PrimaryStage;
import me.reidj.application.scene.login.LoginScene;
import me.reidj.client.client.NatsClient;

public class App extends Application {

    @Getter
    private PrimaryStage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = new PrimaryStage(stage);
        LoginScene loginScene = new LoginScene();

        primaryStage.setScene(loginScene.getScene());

        NatsClient.connect();
    }

    public static void main(String[] args) {
        launch();
    }
}