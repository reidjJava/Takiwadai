import io.nats.client.Nats;
import lombok.val;

import java.io.IOException;

public class Service {

    private static final String URL = "nats://demo.nats.io:4222";

    public static void main(String[] args) throws IOException, InterruptedException {
        val connection = Nats.connect(URL);
        while (true) {
            // TODO Вынести в метод
            val dispatcher = connection.createDispatcher((message) -> {
                System.out.println("Received takiwadai.saveUser from: " + new String(message.getData()));
                connection.publish(message.getReplyTo(), "HI BEACHHHH".getBytes());
            });
            dispatcher.subscribe("saveUser");
        }
    }
}
