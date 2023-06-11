package me.reidj.application.scene.application;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.exception.Errors;
import me.reidj.application.misc.CategoryType;
import me.reidj.application.misc.StatusType;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.CreateApplicationPackage;

import java.util.Arrays;

public class ApplicationScene extends AbstractScene {

    @FXML
    private JFXComboBox<String> categoryComboBox;
    @FXML
    private TextField customCategoryField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button createApplicationButton;

    public ApplicationScene() {
        super("application/applicationScene.fxml");
    }

    @FXML
    private void initialize() {
        categoryComboBox.setValue(CategoryType.WEBSITE_PROBLEMS.getTitle());
        categoryComboBox.getItems().addAll(Arrays.stream(CategoryType.values()).map(CategoryType::getTitle).toList());
        categoryComboBox.setOnAction(event -> {
            val has = categoryComboBox.getValue().equalsIgnoreCase(CategoryType.OTHER.getTitle());
            customCategoryField.setVisible(has);
            descriptionTextArea.setLayoutY(has ? 300 : 256);
            createApplicationButton.setLayoutY(has ? 510 : 481);
        });
    }


    @FXML
    private void createApplication() {
        val customCategory = customCategoryField.getText();
        val description = descriptionTextArea.getText();
        val categoryValue = categoryComboBox.getValue();
        val category = categoryValue.equalsIgnoreCase(CategoryType.OTHER.getTitle()) ? customCategory : categoryValue;

        if (Errors.FIELD_EMPTY.check(description, category))
            return;

        Nats.publish("createApplication", new CreateApplicationPackage(
                App.getApp().getUser().id(),
                description,
                category,
                StatusType.SEND.getTitle()
        ));

        App.getApp().getPrimaryStage().showAlert(
                Alert.AlertType.INFORMATION,
                "Заявка успешно отправлена!",
                ""
        );
    }

    @FXML
    private void backToProfileOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getProfileScene().getScene());
    }
}
