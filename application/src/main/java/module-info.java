module me.reidj.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires me.reidj.client;
    requires static lombok;

    opens me.reidj.application to javafx.fxml;
    opens me.reidj.application.scene.login to javafx.fxml;
    opens me.reidj.application.scene to javafx.fxml;
    exports me.reidj.application.scene.login;
    exports me.reidj.application.scene;
    exports me.reidj.application;
}