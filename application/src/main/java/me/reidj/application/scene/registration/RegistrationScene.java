package me.reidj.application.scene.registration;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.exception.Errors;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.RegistrationUserPackage;

import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer;

    public RegistrationScene() {
        super("registration/registrationScene.fxml");
    }

    @FXML
    void backToLoginScreen() {

    }

    @FXML
    void goRegistration() {
        val name = nameField.getText();
        val surname = surnameField.getText();
        val patronymic = patronymicField.getText();
        val email = emailField.getText();
        val password = passwordField.getText();
        val confirmPassword = confirmPasswordField.getText();

        if (Errors.EMAIL.check(email))
            return;
        if (Errors.FIELD_EMPTY.check(name, surname, patronymic, email, password, confirmPassword))
            return;
        if (Errors.PASSWORD_IS_SHORT.check(password))
            return;
        if (Errors.INCORRECT_SYMBOL.check(name, surname, patronymic))
            return;
        if (Errors.PASSWORD.check(password, confirmPassword))
            return;

        val response = (RegistrationUserPackage) Nats.publishAndWaitResponse(
                new RegistrationUserPackage(
                        email,
                        password,
                        name,
                        surname,
                        patronymic
                ), "registrationUser");

        if (response.exception != null) {
            App.getApp().getPrimaryStage().showAlert(
                    Alert.AlertType.ERROR,
                    "Пользователь с таким электронным адресом уже существует!",
                    ""
            );
            return;
        }

        App.getApp().getPrimaryStage().showAlert(
                Alert.AlertType.INFORMATION,
                "Через 3 секунды Вы будете автоматически перенаправлены на страницу авторизации.",
                "Регистрация прошла успешно!"
        );

        timer = new Timer();
        timer.scheduleAtFixedRate(new RedirectTask(), 3000, 3000);
    }

    private class RedirectTask extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(() -> {

            });
            timer.cancel();
        }
    }
}
