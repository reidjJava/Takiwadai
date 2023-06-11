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
import me.reidj.client.data.LogData;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.ApplicationDeletePackage;
import me.reidj.client.protocol.GetAllApplicationsPackage;
import me.reidj.client.protocol.UpdateStatusApplicationPackage;

import java.util.Arrays;

public class AdminScene extends AbstractScene {

    @FXML
    private TableColumn<LogData, String> categoryColumn;
    @FXML
    private TableColumn<LogData, String> creatorColumn;
    @FXML
    private TableColumn<LogData, String> dateCreationColumn;
    @FXML
    private TableColumn<LogData, String> descriptionColumn;
    @FXML
    private DatePicker firstDatePicker;
    @FXML
    private TableView<LogData> logsTableView;
    @FXML
    private JFXComboBox<LogData> printableStatusesComboBox;
    @FXML
    private TableColumn<LogData, String> reasonColumn;
    @FXML
    private DatePicker secondDatePicker;
    @FXML
    private TableColumn<LogData, String> statusColumn;
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

        fillTableWithApplications();
        selectionColumn();
    }

    private void fillTableWithApplications() {
        logsTableView.getItems().clear();
        val response = (GetAllApplicationsPackage) Nats.publishAndWaitResponse(new GetAllApplicationsPackage(), "getAllApplications");
        logsTableView.getItems().addAll(response.getLogDataSet());
    }

    private void selectionColumn() {
        logsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            val id = newValue.getId();
            removeApplication(id);
            updateStatusApplication(id, newValue.creatorId);
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
        val response = (UpdateStatusApplicationPackage) Nats.publishAndWaitResponse(
                new UpdateStatusApplicationPackage(id, creatorId, status, has ? reasonField.getText() : null),
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

        fillTableWithApplications();
    }

    @FXML
    void closeReasonPane() {
        reasonPane.setVisible(false);
    }
}
