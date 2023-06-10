package me.reidj.client.client;

import io.nats.client.Connection;
import io.nats.client.Nats;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@UtilityClass
public class NatsClient {

    private static final String NATS_URL = "nats://demo.nats.io:4222";

    private Connection connection;

    public void connect() {
        try {
            connection = Nats.connect(NATS_URL);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publish(String subject, String data) {
        connection.publish(subject, data.getBytes());
    }

    public String publishAndWaitResponse(String subject, String data) {
        try {
            return new String(connection.request(subject, data.getBytes()).get().getData());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }
}
