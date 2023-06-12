package me.reidj.application.scene.admin;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.misc.StatusType;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.data.ApplicationData;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.ApplicationDeletePackage;
import me.reidj.client.protocol.GetAllApplicationsIntervalPackage;
import me.reidj.client.protocol.GetAllApplicationsPackage;
import me.reidj.client.protocol.UpdateStatusApplicationPackage;

import java.util.Arrays;

public class AdminScene extends AbstractScene {

    @FXML
    private TableColumn<ApplicationData, String> categoryColumn;
    @FXML
    private TableColumn<ApplicationData, String> creatorColumn;
    @FXML
    private TableColumn<ApplicationData, String> dateCreationColumn;
    @FXML
    private TableColumn<ApplicationData, String> descriptionColumn;
    @FXML
    private DatePicker firstDatePicker;
    @FXML
    private TableView<ApplicationData> logsTableView;
    @FXML
    private JFXComboBox<String> printableStatusesComboBox;
    @FXML
    private TableColumn<ApplicationData, String> reasonColumn;
    @FXML
    private DatePicker secondDatePicker;
    @FXML
    private TableColumn<ApplicationData, String> statusColumn;
    @FXML
    private JFXComboBox<String> statusesComboBox;
    @FXML
    private Button applicationDeleteButton;
    @FXML
    private Button updateStatusApplicationButton;
    @FXML
    private AnchorPane reasonPane;
    @FXML
    private Button updateStatusWithReason;
    @FXML
    private TextField reasonField;
    @FXML
    private AnchorPane printingInWordPane;

    public AdminScene() {
        super("admin/adminScene.fxml");
    }

    @FXML
    private void initialize() {
        creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        statusesComboBox.setValue(StatusType.IN_WORK.getTitle());
        statusesComboBox.getItems().addAll(Arrays.stream(StatusType.values()).map(StatusType::getTitle).toList());

        printableStatusesComboBox.setValue(StatusType.IN_WORK.getTitle());
        printableStatusesComboBox.getItems().addAll(Arrays.stream(StatusType.values()).map(StatusType::getTitle).toList());

        fillTableWithApplications();
        selectionColumn();
    }

    private void fillTableWithApplications() {
        logsTableView.getItems().clear();
        val response = (GetAllApplicationsPackage) Nats.publishAndWaitResponse(new GetAllApplicationsPackage(), "getAllApplications");
        logsTableView.getItems().addAll(response.getApplicationDataSet());
    }

    private void selectionColumn() {
        logsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            val id = newValue.getId();
            removeApplication(id);
            updateStatusApplication(id, newValue.getCreatorId());
        });
    }

    private void removeApplication(int id) {
        applicationDeleteButton.setOnMouseClicked(event -> {
            Nats.publish("applicationDelete", new ApplicationDeletePackage(id));
            App.getApp().getPrimaryStage().showAlert(
                    Alert.AlertType.INFORMATION,
                    "Заявка удалена успешно!", ""
            );
            fillTableWithApplications();
            App.getApp().getChangelogScene().saveChange("Заявка с кодом: " + id + " была удалена.");
        });
    }

    private void updateStatusApplication(int id, int creatorId) {
        updateStatusApplicationButton.setOnMouseClicked(event -> {
            val status = statusesComboBox.getValue();
            val has = status.equalsIgnoreCase(StatusType.DENIED.getTitle());
            if (has) {
                reasonPane.setVisible(true);
                updateStatusWithReason.setOnMouseClicked(clicked -> sendingStatusUpdatePackage(id, creatorId));
                return;
            }
            sendingStatusUpdatePackage(id, creatorId);
        });
    }

    private void sendingStatusUpdatePackage(int id, int creatorId) {
        val status = statusesComboBox.getValue();
        val has = status.equalsIgnoreCase(StatusType.DENIED.getTitle());
        val reason = reasonField.getText();
        val response = (UpdateStatusApplicationPackage) Nats.publishAndWaitResponse(
                new UpdateStatusApplicationPackage(id, creatorId, status, has ? reason : null),
                "updateStatusApplication"
        );
        val user = response.getUser();

        App.getApp().getMailSender().send(
                user.email(),
                "Статус заявки был обновлён",
                "Здравствуйте " + user.name() + "." +
                        "\n\n" +
                        "Статус вашей заявки в приложении «Takiwadai» был " +
                        "изменён: " + status
        );

        App.getApp().getPrimaryStage().showAlert(
                Alert.AlertType.INFORMATION,
                "Статус заявки успешно обновлён!",
                ""
        );

        App.getApp().getChangelogScene().saveChange(
                "Заявка с кодом " + id + " .Обновлён статус - " + status
        );

        fillTableWithApplications();
    }

    @FXML
    private void closeReasonPane() {
        reasonPane.setVisible(false);
    }

    @FXML
    private void openChangelogOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getChangelogScene().getScene());
    }

    @FXML
    private void openPrintMenu() {
        printingInWordPane.setVisible(true);
    }

    @FXML
    private void cancelPrintWord() {
        printingInWordPane.setVisible(false);
    }

    @FXML
    private void goPrintData() {
        App.getApp().getWordDocumentProcessor().createWordDocument();

        val stringBuilder = new StringBuilder("Отчёт по присланным заявкам о технических неисправностях.");
        val response = (GetAllApplicationsIntervalPackage) Nats.publishAndWaitResponse(
                new GetAllApplicationsIntervalPackage(
                        firstDatePicker.getValue().toString(),
                        secondDatePicker.getValue().toString(),
                        printableStatusesComboBox.getValue()
                ),
                "getAllApplicationsInInterval"
        );

        response.getApplicationDataSet().forEach(applicationData -> stringBuilder
                .append("\n")
                .append("-------------------------------------------------------------------------")
                .append("\n")
                .append("Код заявки: ")
                .append(applicationData.getId())
                .append("\n")
                .append("Создал: ")
                .append(applicationData.getCreator())
                .append("\n")
                .append("Дата создания: ")
                .append(applicationData.getDateCreation())
                .append("\n")
                .append("Категория: ")
                .append(applicationData.getCategory())
                .append("\n")
                .append("Описание: ")
                .append(applicationData.getDescription())
                .append("\n")
                .append("Статус: ")
                .append(applicationData.getStatus())
                .append("\n"));

        App.getApp().getWordDocumentProcessor().onWrite(stringBuilder);
        App.getApp().getPrimaryStage().showAlert(
                Alert.AlertType.INFORMATION,
                "Документ успешно создан.",
                ""
        );
    }
}
