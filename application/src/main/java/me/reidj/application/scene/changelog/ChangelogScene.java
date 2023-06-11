package me.reidj.application.scene.changelog;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.reidj.application.scene.AbstractScene;
import me.reidj.client.data.LogData;

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
    }
}
