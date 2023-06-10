module me.reidj.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.nats.jnats;
    requires com.jfoenix;
    requires static lombok;

    opens me.reidj.application to javafx.fxml;
    opens me.reidj.application.scene.login to javafx.fxml;
    exports me.reidj.application.scene.login;
    exports me.reidj.application.scene;
    exports me.reidj.application;
}