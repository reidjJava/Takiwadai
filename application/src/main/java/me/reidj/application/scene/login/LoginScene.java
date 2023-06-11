package me.reidj.application.scene.login;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.exception.Errors;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.LoginUserPackage;

public class LoginScene extends AbstractScene {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Pane root;
    @FXML
    private JFXCheckBox saveDataCheckbox;

    public LoginScene() {
        super("login/loginScene.fxml");
    }

    @FXML
    private void initialize() {
        val data = Nats.getGson().fromJson(new String(App.getApp().getFileManager().onRead()), String.class);
        if (data != null) {
            val pair = data.split(":");
            emailField.setText(pair[0]);
            passwordField.setText(pair[1]);
            saveDataCheckbox.fire();
        }
    }

    @FXML
    private void goAuth() {
        val email = emailField.getText();
        val password = passwordField.getText();

        if (Errors.FIELD_EMPTY.check(email, password))
            return;
        if (Errors.EMAIL.check(email))
            return;
        if (Errors.PASSWORD_IS_SHORT.check(password))
            return;

        val response = (LoginUserPackage) Nats.publishAndWaitResponse(new LoginUserPackage(email, password), "authUser");

        if (response.getName() == null || response.getSurname() == null || response.getPatronymic() == null) {
            App.getApp().getPrimaryStage().showAlert(Alert.AlertType.ERROR, "Неверный логин или пароль", "");
        } else {
            //showOverlay(root, App.getApp().getRegistrationScene().getScene().getRoot());
        }

        checkboxPressed();
    }

    @FXML
    private void openRegistrationOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getRegistrationScene().getScene());
    }

    private void checkboxPressed() {
        if (saveDataCheckbox.isSelected()) {
            val email = emailField.getText();
            val password = passwordField.getText();

            if (Errors.FIELD_EMPTY.check(email, password))
                return;

            App.getApp().getFileManager().onWrite(Nats.getGson().toJson(email + ":" + password).getBytes());
        }
    }
}
