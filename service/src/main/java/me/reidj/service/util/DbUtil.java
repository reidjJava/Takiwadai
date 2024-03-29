package me.reidj.service.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.reidj.client.util.Utils;

import java.io.IOError;
import java.util.Properties;

@UtilityClass
public class DbUtil {

    public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT, name TEXT, surname TEXT, patronymic TEXT, email TEXT, password TEXT, PRIMARY KEY(id));";
    public static final String CREATE_TABLE_APPLICATIONS = "CREATE TABLE IF NOT EXISTS applications (applicationId INT AUTO_INCREMENT, userId INT, description TEXT, date DATE, time TIME, category TEXT, status TEXT, reason TEXT NULL, PRIMARY KEY (applicationId), FOREIGN KEY(userId) REFERENCES users (id));";
    public static final String CREATE_TABLE_LIST_CHANGES = "CREATE TABLE IF NOT EXISTS list_changes (idChanges INT AUTO_INCREMENT, dateChange TIMESTAMP, userId INT, description TEXT, PRIMARY KEY (idChanges), FOREIGN KEY(userId) REFERENCES users (id))";
    public static final String SELECT_USER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String SELECT_ALL_APPLICATIONS = "SELECT * FROM users JOIN applications ON users.id = applications.userId";
    public static final String SELECT_APPLICATION_CREATOR_BY_ID = "SELECT * FROM users JOIN applications ON users.id = applications.userId WHERE id = ?";
    public static final String SELECT_ALL_LIST_CHANGES = "SELECT * FROM users JOIN list_changes ON users.id = list_changes.userId";
    public static final String SELECT_DATE_BETWEEN = "SELECT * FROM users JOIN applications ON users.id = applications.userId WHERE date BETWEEN ? AND ? AND status = ?";
    public static final String CREATE_USER = "INSERT INTO users (name, surname, patronymic, email, password) VALUES(?, ?, ?, ?, ?);";
    public static final String CREATE_CHANGELOG = "INSERT INTO list_changes (dateChange, userId, description) VALUES(?, ?, ?);";
    public static final String CREATE_APPLICATION = "INSERT INTO applications (userId, description, date, time, category, status, reason) VALUES(?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_USER_DATA = "UPDATE users SET name = ?, surname = ?, patronymic = ?, password = ? WHERE id = ?";
    public static final String UPDATE_USER_PASSWORD = "UPDATE users SET password = ? WHERE email = ?";
    public static final String UPDATE_APPLICATION = "UPDATE applications SET status = ?, reason = ? WHERE applicationId = ?";
    public static final String DELETE_APPLICATION = "DELETE FROM applications WHERE applicationId = ?";

    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER_CLASS = "driver.class.name";

    private static final String RESOURCES_PATH = "service/src/main/resources/";

    @Getter
    private HikariDataSource dataSource;

    static {
        try {
            Properties properties = Utils.loadFile(RESOURCES_PATH + "database.properties");

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(properties.getProperty(DB_DRIVER_CLASS));

            dataSource.setJdbcUrl(properties.getProperty(DB_URL));
            dataSource.setUsername(properties.getProperty(DB_USERNAME));
            dataSource.setPassword(properties.getProperty(DB_PASSWORD));

            dataSource.setIdleTimeout(300000);
            dataSource.setMaximumPoolSize(10);
            dataSource.setMinimumIdle(1);
        } catch (IOError e) {
            e.printStackTrace();
        }
    }
}
