package me.reidj.application;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import me.reidj.application.scene.PrimaryStage;
import me.reidj.application.scene.login.LoginScene;
import me.reidj.application.scene.registration.RegistrationScene;
import me.reidj.client.network.Nats;

@Getter
public class App extends Application {

    @Getter
    private static App app;

    private PrimaryStage primaryStage;
    private LoginScene loginScene;
    private RegistrationScene registrationScene;

    @Override
    public void start(Stage stage) {
        app = this;

        primaryStage = new PrimaryStage(stage);
        loginScene = new LoginScene();
        registrationScene = new RegistrationScene();

        primaryStage.setScene(loginScene.getScene());

        Nats.connect();
    }

    public static void main(String[] args) {
        launch();
    }
}