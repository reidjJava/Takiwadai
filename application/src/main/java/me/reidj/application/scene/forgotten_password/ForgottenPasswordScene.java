package me.reidj.application.scene.forgotten_password;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.exception.Errors;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.GenerateNewPasswordPackage;

public class ForgottenPasswordScene extends AbstractScene {

    @FXML
    private TextField emailField;

    public ForgottenPasswordScene() {
        super("forgotten_password/forgottenPasswordScene.fxml");
    }

    @FXML
    void recoverData() {
        val email = emailField.getText();

        if (Errors.FIELD_EMPTY.check(email))
            return;
        if (Errors.EMAIL.check(email))
            return;

        val response = (GenerateNewPasswordPackage) Nats.publishAndWaitResponse(new GenerateNewPasswordPackage(email), "generateNewPassword");

        if (response.getException() != null) {
            App.getApp().getPrimaryStage().showAlert(
                    Alert.AlertType.ERROR,
                    "Введённый вами почтовый адрес не найден в нашей системе!",
                    ""
            );
            return;
        }

        App.getApp().getMailSender().send(
                email,
                "Восстановление пароля",
                "Здравствуйте " + response.getName() + "." +
                        "\n\n" +
                        "Для Вашей учетной записи в приложении «Takiwadai» было запрошено " +
                        "восстановление пароля. Новый пароль: " + response.getNewPassword()
        );

        App.getApp().getPrimaryStage().showAlert(
                Alert.AlertType.INFORMATION,
                "Письмо с новым паролем отправлено на адрес " + email,
                ""
        );
    }
}
