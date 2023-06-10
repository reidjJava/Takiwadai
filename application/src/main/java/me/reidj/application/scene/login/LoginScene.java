package me.reidj.application.scene.login;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.client.NatsClient;

public class LoginScene extends AbstractScene {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    public LoginScene() {
        super("login/loginScene.fxml");
    }

    @FXML
    void goAuth() {
        System.out.println(NatsClient.publishAndWaitResponse("saveUser", "test"));
    }
}
