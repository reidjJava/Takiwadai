module me.reidj.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.nats.jnats;

    opens me.reidj.application to javafx.fxml;
    exports me.reidj.application;
}