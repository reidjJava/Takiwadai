import me.reidj.client.client.Nats;

public class App {

    public static void main(String[] args) {
        Nats.connect();
        while (true) {
            Nats.registerHandler((message) -> {
                System.out.println("Received saveUser from: " + new String(message.getData()));
            }, "saveUser");
        }
    }
}
