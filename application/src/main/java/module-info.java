module me.reidj.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires me.reidj.client;
    requires com.google.gson;
    requires java.mail;
    requires static lombok;
    requires static activation;

    opens me.reidj.application to javafx.fxml;
    opens me.reidj.application.scene.login to javafx.fxml;
    opens me.reidj.application.scene.registration to javafx.fxml;
    opens me.reidj.application.scene.profile to javafx.fxml;
    opens me.reidj.application.scene.forgotten_password to javafx.fxml;
    opens me.reidj.application.scene.application to javafx.fxml;
    opens me.reidj.application.scene.admin to javafx.fxml;
    opens me.reidj.application.scene.log to javafx.fxml;
    opens me.reidj.application.scene.changelog to javafx.fxml;
    opens me.reidj.application.scene to javafx.fxml;
    exports me.reidj.application.scene.login;
    exports me.reidj.application.scene.profile;
    exports me.reidj.application.scene.registration;
    exports me.reidj.application.scene.forgotten_password;
    exports me.reidj.application.scene.application;
    exports me.reidj.application.scene.changelog;
    exports me.reidj.application.scene.log;
    exports me.reidj.application.scene.admin;
    exports me.reidj.application.scene;
    exports me.reidj.application;
}