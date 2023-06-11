package me.reidj.application;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.reidj.application.manager.FileManager;
import me.reidj.application.scene.PrimaryStage;
import me.reidj.application.scene.admin.AdminScene;
import me.reidj.application.scene.application.ApplicationScene;
import me.reidj.application.scene.forgotten_password.ForgottenPasswordScene;
import me.reidj.application.scene.log.LogScene;
import me.reidj.application.scene.login.LoginScene;
import me.reidj.application.scene.profile.ProfileScene;
import me.reidj.application.scene.registration.RegistrationScene;
import me.reidj.application.service.MailSender;
import me.reidj.application.user.User;
import me.reidj.client.network.Nats;

import java.io.IOException;

@Getter
public class App extends Application {

    @Getter
    private static App app;

    private PrimaryStage primaryStage;
    private LoginScene loginScene;
    private RegistrationScene registrationScene;
    private ProfileScene profileScene;
    private ForgottenPasswordScene forgottenPasswordScene;
    private ApplicationScene applicationScene;
    private AdminScene adminScene;
    private LogScene logScene;

    private FileManager fileManager;
    private MailSender mailSender;
    @Setter
    private User user;

    @Override
    public void start(Stage stage) throws IOException {
        app = this;

        primaryStage = new PrimaryStage(stage);
        loginScene = new LoginScene();
        registrationScene = new RegistrationScene();
        profileScene = new ProfileScene();
        forgottenPasswordScene = new ForgottenPasswordScene();
        applicationScene = new ApplicationScene();
        adminScene = new AdminScene();
        logScene = new LogScene();

        mailSender = new MailSender();
        fileManager = new FileManager();
        fileManager.createFileInAppdataDir(FileManager.FILE_NAME);

        primaryStage.setScene(loginScene.getScene());

        Nats.connect();
    }

    public static void main(String[] args) {
        launch();
    }
}