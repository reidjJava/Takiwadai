package me.reidj.application.scene.admin;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.misc.StatusType;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.data.LogData;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.ApplicationDeletePackage;
import me.reidj.client.protocol.GetAllApplicationsPackage;

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
        val response = (GetAllApplicationsPackage) Nats.publishAndWaitResponse(new GetAllApplicationsPackage(), "getAllApplications");
        logsTableView.getItems().clear();
        logsTableView.getItems().addAll(response.getLogDataSet());
    }

    private void selectionColumn() {
        logsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            val id = newValue.getId();
            removeApplication(id);
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
}
