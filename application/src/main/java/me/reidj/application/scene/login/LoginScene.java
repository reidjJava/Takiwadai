package me.reidj.application.scene.login;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.exception.Errors;
import me.reidj.application.scene.AbstractScene;
import me.reidj.application.user.User;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.LoginUserPackage;

public class LoginScene extends AbstractScene {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private JFXCheckBox saveDataCheckbox;
    @FXML
    private JFXCheckBox loginAsAdmin;

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
            App.getApp().setUser(new User(
                    response.getUserId(),
                    response.getName(),
                    response.getSurname(),
                    response.getPatronymic(),
                    response.getEmail(),
                    response.getPassword()
            ));
            if (loginAsAdmin.isSelected()) {
                App.getApp().getPrimaryStage().showScene(App.getApp().getAdminScene().getScene());
            } else {
                App.getApp().getPrimaryStage().showScene(App.getApp().getProfileScene().getScene());
            }
        }

        checkboxPressed();
    }

    @FXML
    private void openRegistrationOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getRegistrationScene().getScene());
    }

    @FXML
    void openPasswordRecoveryOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getForgottenPasswordScene().getScene());
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
