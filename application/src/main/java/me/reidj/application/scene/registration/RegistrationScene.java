package me.reidj.application.scene.registration;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.reidj.application.scene.AbstractScene;

public class RegistrationScene extends AbstractScene {

    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField surnameField;

    public RegistrationScene() {
        super("registration/registrationScene.fxml");
    }

    @FXML
    void backToLoginScreen() {

    }
}
