package me.reidj.service;

import me.reidj.client.network.Nats;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static me.reidj.client.network.Nats.connect;
import static me.reidj.service.util.DbUtil.*;

public class App {

    public static void main(String[] args) {
        connect();

        try (Connection connection = getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            Arrays.asList(
                    CREATE_TABLE_USERS,
                    CREATE_TABLE_LIST_CHANGES,
                    CREATE_TABLE_APPLICATIONS
            ).forEach(sql -> {
                try {
                    statement.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

        while (true) {
            Nats.registerHandler((message) -> {
                System.out.println("Received saveUser from: " + new String(message.getData()));
            }, "saveUser");
        }
    }
}
