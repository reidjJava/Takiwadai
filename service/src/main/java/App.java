import static me.reidj.client.client.Nats.connect;
import static me.reidj.client.client.Nats.registerHandler;

public class App {

    public static void main(String[] args) {
        connect();
        while (true) {
            registerHandler((message) -> {
                System.out.println("Received saveUser from: " + new String(message.getData()));
            }, "saveUser");
        }
    }
}
