package me.reidj.application.wordprocessor;

import javafx.stage.DirectoryChooser;
import lombok.val;
import me.reidj.application.App;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WordDocumentProcessor {

    private static final String FILE_NAME = "Отчёт от ";

    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public void createWordDocument() {
        val selectedDirectory = directoryChooser.showDialog(App.getApp().getPrimaryStage().stage());
        try {
            App.getApp().getFileManager().createFileByPath(
                    selectedDirectory.getAbsolutePath() + "\\" + FILE_NAME + dateTimeFormatter.format(LocalDateTime.now()) + ".doc"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onWrite(StringBuilder stringBuilder) {
        App.getApp().getFileManager().onWrite(stringBuilder.toString().getBytes());
    }
}
