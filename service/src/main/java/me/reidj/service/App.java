package me.reidj.service;

import lombok.val;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.LoginUserPackage;
import me.reidj.service.util.DbUtil;

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

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received authUser: " + request);

            val loginPackage = Nats.getGson().fromJson(request, LoginUserPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(SELECT_USER_BY_EMAIL_AND_PASSWORD);

                statement.setString(1, loginPackage.getEmail());
                statement.setString(2, loginPackage.getPassword());

                val resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    loginPackage.setName(resultSet.getString("name"));
                    loginPackage.setSurname(resultSet.getString("surname"));
                    loginPackage.setPatronymic(resultSet.getString("patronymic"));
                }

                Nats.publish(message.getReplyTo(), loginPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "authUser");
    }
}
