package me.reidj.application.scene.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.exception.Errors;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.UpdateUserDataPackage;

public class ProfileScene extends AbstractScene {

    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField surnameField;

    public ProfileScene() {
        super("profile/profileScene.fxml");
    }

    @FXML
    private void initialize() {
        val user = App.getApp().getUser();
        nameField.setText(user.name());
        surnameField.setText(user.surname());
        patronymicField.setText(user.patronymic());
        passwordField.setText(user.password());
        confirmPasswordField.setText(user.password());
    }

    @FXML
    private void updateUserData() {
        val name = nameField.getText();
        val surname = surnameField.getText();
        val patronymic = patronymicField.getText();
        val password = passwordField.getText();
        val confirmPassword = confirmPasswordField.getText();

        if (Errors.FIELD_EMPTY.check(name, surname, patronymic, patronymic, password, confirmPassword))
            return;

        Nats.publish("updateUserData", new UpdateUserDataPackage(
                App.getApp().getUser().id(),
                surname,
                name,
                patronymic,
                password
        ));

        App.getApp().getPrimaryStage().showAlert(Alert.AlertType.INFORMATION, "Данные успешно обновлены.", "");
    }
}
