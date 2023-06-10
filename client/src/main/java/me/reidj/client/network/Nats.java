package me.reidj.client.network;

import com.google.gson.Gson;
import io.nats.client.Connection;
import io.nats.client.Message;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.reidj.client.protocol.CorePackage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@UtilityClass
public class Nats {

    private static final String NATS_URL = "nats://demo.nats.io:4222";

    private Connection connection;

    private final Gson gson = new Gson();

    public void connect() {
        try {
            connection = io.nats.client.Nats.connect(NATS_URL);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publish(String subject, CorePackage data) {
        connection.publish(subject, gson.toJson(data).getBytes());
    }

    public CorePackage publishAndWaitResponse(CorePackage data, String subject) {
        String json = gson.toJson(data);
        String response = null;
        try {
            response = new String(connection.request(subject, json.getBytes()).get().getData());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return gson.fromJson(response, data.getClass());
    }

    public void registerHandler(Consumer<Message> consumer, String subject) {
        val dispatcher = connection.createDispatcher(consumer::accept);
        dispatcher.subscribe(subject);
    }
}
