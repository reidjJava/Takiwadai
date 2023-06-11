package me.reidj.application.scene.log;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.data.LogData;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.GetUserApplicationPackage;

public class LogScene extends AbstractScene {

    @FXML
    private TableColumn<LogData, String> categoryColumn;
    @FXML
    private TableColumn<LogData, String> dateCreationColumn;
    @FXML
    private TableColumn<LogData, String> descriptionColumn;
    @FXML
    private TableView<LogData> logsTable;
    @FXML
    private TableColumn<LogData, String> reasonColumn;
    @FXML
    private TableColumn<LogData, String> statusColumn;

    public LogScene() {
        super("log/logScene.fxml");
    }

    @FXML
    private void initialize() {
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        val reason = (GetUserApplicationPackage) Nats.publishAndWaitResponse(
                new GetUserApplicationPackage(App.getApp().getUser().id()),
                "getUserApplication"
        );
        logsTable.getItems().addAll(reason.getLogDataSet());
    }


    @FXML
    void backApplicationOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getApplicationScene().getScene());
    }
}
