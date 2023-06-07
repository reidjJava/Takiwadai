import io.nats.client.Nats;
import lombok.val;

public class Service {

    private static final String URL = "nats://demo.nats.io:4222";

    public static void main(String[] args) {
        while (true) {
            try (val connection = Nats.connect(URL)) {
                val dispatcher = connection.createDispatcher((message) -> {
                    val receivedMessage = new String(message.getData());
                    System.out.println("Received message:" + receivedMessage);
                });
                dispatcher.subscribe("takiwadai.saveUser");
                System.out.println("Listening for messages on takiwadai.saveUser");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
