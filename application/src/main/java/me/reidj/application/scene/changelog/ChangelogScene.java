package me.reidj.application.scene.changelog;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.val;
import me.reidj.application.App;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.data.LogData;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.AddToChangelogPackage;
import me.reidj.client.protocol.GetAllListChangesPackage;

public class ChangelogScene extends AbstractScene {

    @FXML
    private TableColumn<LogData, String> changesColumn;
    @FXML
    private TableColumn<LogData, String> dateChangeColumn;
    @FXML
    private TableColumn<LogData, String> descriptionColumn;
    @FXML
    private TableView<LogData> logsTable;

    public ChangelogScene() {
        super("changelog/changelogScene.fxml");
    }

    @FXML
    private void initialize() {
        changesColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
        dateChangeColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        val response = (GetAllListChangesPackage) Nats.publishAndWaitResponse(
                new GetAllListChangesPackage(),
                "getAllListChanges"
        );

        logsTable.getItems().addAll(response.getLogDataSet());
    }

    @FXML
    private void backAdminOverlay() {
        App.getApp().getPrimaryStage().showScene(App.getApp().getAdminScene().getScene());
    }


    public void saveChange(String description) {
        Nats.publish("createChangelog", new AddToChangelogPackage(
                System.currentTimeMillis(),
                App.getApp().getUser().id(),
                description
        ));
    }
}
