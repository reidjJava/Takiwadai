package me.reidj.application;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import me.reidj.application.manager.FileManager;
import me.reidj.application.scene.PrimaryStage;
import me.reidj.application.scene.login.LoginScene;
import me.reidj.application.scene.registration.RegistrationScene;
import me.reidj.client.network.Nats;

import java.io.IOException;

@Getter
public class App extends Application {

    @Getter
    private static App app;

    private PrimaryStage primaryStage;
    private LoginScene loginScene;
    private RegistrationScene registrationScene;

    private FileManager fileManager;

    @Override
    public void start(Stage stage) throws IOException {
        app = this;

        primaryStage = new PrimaryStage(stage);
        loginScene = new LoginScene();
        registrationScene = new RegistrationScene();

        fileManager = new FileManager();
        fileManager.createFileInAppdataDir(FileManager.FILE_NAME);

        primaryStage.setScene(loginScene.getScene());

        Nats.connect();
    }

    public static void main(String[] args) {
        launch();
    }
}