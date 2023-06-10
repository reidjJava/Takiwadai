package me.reidj.application;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import me.reidj.application.scene.PrimaryStage;
import me.reidj.application.scene.login.LoginScene;
import me.reidj.client.network.Nats;

public class App extends Application {

    private static App app;

    @Getter
    private PrimaryStage primaryStage;

    @Override
    public void start(Stage stage) {
        app = this;

        primaryStage = new PrimaryStage(stage);
        LoginScene loginScene = new LoginScene();

        primaryStage.setScene(loginScene.getScene());

        Nats.connect();
    }

    public static void main(String[] args) {
        launch();
    }

    public static App getApp() {
        return app;
    }
}