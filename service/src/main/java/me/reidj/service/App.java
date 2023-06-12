package me.reidj.service;

import lombok.val;
import me.reidj.client.data.LogData;
import me.reidj.client.data.UserDto;
import me.reidj.client.exception.Exceptions;
import me.reidj.client.network.Nats;
import me.reidj.client.protocol.*;
import me.reidj.service.util.DbUtil;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

import static me.reidj.client.network.Nats.connect;
import static me.reidj.service.util.DbUtil.*;

public class App {

    public static void main(String[] args) {
        connect();

        try (Connection connection = getDataSource().getConnection()) {
            val statement = connection.createStatement();
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
                    loginPackage.setUserId(resultSet.getInt("id"));
                    loginPackage.setName(resultSet.getString("name"));
                    loginPackage.setSurname(resultSet.getString("surname"));
                    loginPackage.setPatronymic(resultSet.getString("patronymic"));
                }

                Nats.publish(message.getReplyTo(), loginPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "authUser");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received registrationUser: " + request);

            val registrationPackage = Nats.getGson().fromJson(request, RegistrationUserPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                var statement = connection.prepareStatement(SELECT_USER_BY_EMAIL);

                statement.setString(1, registrationPackage.getEmail());

                val resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    registrationPackage.setException(Exceptions.USER_ALREADY_EXISTS);
                } else {
                    statement = connection.prepareStatement(CREATE_USER);
                    statement.setString(1, registrationPackage.getName());
                    statement.setString(2, registrationPackage.getSurname());
                    statement.setString(3, registrationPackage.getPatronymic());
                    statement.setString(4, registrationPackage.getEmail());
                    statement.setString(5, registrationPackage.getPassword());
                    statement.execute();
                }
                Nats.publish(message.getReplyTo(), registrationPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "registrationUser");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received updateUserData: " + request);

            val updateUserDataPackage = Nats.getGson().fromJson(request, UpdateUserDataPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(UPDATE_USER_DATA);

                statement.setString(1, updateUserDataPackage.getName());
                statement.setString(2, updateUserDataPackage.getSurname());
                statement.setString(3, updateUserDataPackage.getPatronymic());
                statement.setString(4, updateUserDataPackage.getPassword());
                statement.setInt(5, updateUserDataPackage.getUserId());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "updateUserData");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received generateNewPassword: " + request);

            val generateNewPasswordPackage = Nats.getGson().fromJson(request, GenerateNewPasswordPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                var statement = connection.prepareStatement(SELECT_USER_BY_EMAIL);

                statement.setString(1, generateNewPasswordPackage.getEmail());

                val resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    val newPassword = passwordGenerator();
                    statement = connection.prepareStatement(UPDATE_USER_PASSWORD);
                    statement.setString(1, newPassword);
                    statement.setString(2, generateNewPasswordPackage.getEmail());
                    statement.executeUpdate();

                    generateNewPasswordPackage.setName(resultSet.getString("name"));
                    generateNewPasswordPackage.setNewPassword(newPassword);
                } else {
                    generateNewPasswordPackage.setException(Exceptions.EMAIL_NOT_FOUND);
                }
                Nats.publish(message.getReplyTo(), generateNewPasswordPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "generateNewPassword");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received createApplication: " + request);

            val createApplicationPackage = Nats.getGson().fromJson(request, CreateApplicationPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(CREATE_APPLICATION);

                statement.setInt(1, createApplicationPackage.getUserId());
                statement.setString(2, createApplicationPackage.getDescription());
                statement.setDate(3, new Date(System.currentTimeMillis()));
                statement.setTime(4, new Time(System.currentTimeMillis()));
                statement.setString(5, createApplicationPackage.getCategory());
                statement.setString(6, createApplicationPackage.getStatus());
                statement.setString(7, null);
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "createApplication");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received getAllApplications: " + request);

            val getAllApplicationsPackage = Nats.getGson().fromJson(request, GetAllApplicationsPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(SELECT_ALL_APPLICATIONS);
                val resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    getAllApplicationsPackage.getLogDataSet().add(
                            new LogData(
                                    resultSet.getInt("applicationId"),
                                    resultSet.getInt("id"),
                                    resultSet.getString("name") + " "
                                            + resultSet.getString("surname") + " "
                                            + resultSet.getString("patronymic"),
                                    resultSet.getString("date") + " "
                                            + resultSet.getString("time"),
                                    resultSet.getString("category"),
                                    resultSet.getString("description"),
                                    resultSet.getString("status"),
                                    resultSet.getString("reason")
                            )
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Nats.publish(message.getReplyTo(), getAllApplicationsPackage);
        }, "getAllApplications");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received applicationDelete: " + request);

            val applicationDeletePackage = Nats.getGson().fromJson(request, ApplicationDeletePackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(DELETE_APPLICATION);
                statement.setInt(1, applicationDeletePackage.getApplicationId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "applicationDelete");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received updateStatusApplication: " + request);

            val updateStatusApplicationPackage = Nats.getGson().fromJson(request, UpdateStatusApplicationPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                var statement = connection.prepareStatement(UPDATE_APPLICATION);
                statement.setString(1, updateStatusApplicationPackage.getStatus());
                statement.setString(2, updateStatusApplicationPackage.getReason());
                statement.setInt(3, updateStatusApplicationPackage.getApplicationId());
                statement.executeUpdate();

                statement = connection.prepareStatement(SELECT_APPLICATION_CREATOR_BY_ID);
                statement.setInt(1, updateStatusApplicationPackage.getCreatorId());

                val resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    updateStatusApplicationPackage.setUser(
                            new UserDto(
                                    resultSet.getString("name"),
                                    resultSet.getString("email")
                            )
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Nats.publish(message.getReplyTo(), updateStatusApplicationPackage);
        }, "updateStatusApplication");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received getUserApplication: " + request);

            val userApplicationPackage = Nats.getGson().fromJson(request, GetUserApplicationPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(SELECT_APPLICATION_CREATOR_BY_ID);

                statement.setInt(1, userApplicationPackage.getUserId());

                val resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    userApplicationPackage.getLogDataSet().add(
                            new LogData(
                                   resultSet.getString("date") + " " + resultSet.getString("time"),
                                   resultSet.getString("category"),
                                   resultSet.getString("description"),
                                   resultSet.getString("status"),
                                   resultSet.getString("reason")
                            )
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Nats.publish(message.getReplyTo(), userApplicationPackage);
        }, "getUserApplication");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received getAllListChanges: " + request);

            val getAllListChangesPackage = Nats.getGson().fromJson(request, GetAllListChangesPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(SELECT_ALL_LIST_CHANGES);
                val resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    getAllListChangesPackage.getLogDataSet().add(
                            new LogData(
                                    resultSet.getString("name") + " "
                                            + resultSet.getString("surname") + " "
                                            + resultSet.getString("patronymic"),
                                    resultSet.getString("dateChange"),
                                    resultSet.getString("description")
                            )
                    );
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Nats.publish(message.getReplyTo(), getAllListChangesPackage);
        }, "getAllListChanges");

        Nats.registerHandler((message) -> {
            val request = new String(message.getData());

            System.out.println("Received createChangelog: " + request);

            val addToChangelogPackage = Nats.getGson().fromJson(request, AddToChangelogPackage.class);

            try (val connection = DbUtil.getDataSource().getConnection()) {
                val statement = connection.prepareStatement(CREATE_CHANGELOG);

                statement.setTimestamp(1, new Timestamp(addToChangelogPackage.getDateChange()));
                statement.setInt(2, addToChangelogPackage.getUserId());
                statement.setString(3, addToChangelogPackage.getDescription());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "createChangelog");
    }

    private static String passwordGenerator() {
        return new Random()
                .ints(10, 33, 122)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
