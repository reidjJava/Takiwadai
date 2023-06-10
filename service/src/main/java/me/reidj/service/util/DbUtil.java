package me.reidj.service.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.io.IOError;
import java.util.Properties;

import static me.reidj.service.util.Utils.RESOURCES;

@UtilityClass
public class DbUtil {

    public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT, name TEXT, surname TEXT, patronymic TEXT, email TEXT, password TEXT, PRIMARY KEY(id));";
    public static final String CREATE_TABLE_APPLICATIONS = "CREATE TABLE IF NOT EXISTS applications (id INT AUTO_INCREMENT, userId INT, name TEXT, surname TEXT, secondName TEXT, email TEXT, description TEXT, date DATE, time TIME, category TEXT, status TEXT, reason TEXT NULL, PRIMARY KEY (id), FOREIGN KEY(userId) REFERENCES users (id));";
    public static final String CREATE_TABLE_LIST_CHANGES = "CREATE TABLE IF NOT EXISTS list_changes (id INT AUTO_INCREMENT, dateChange TIMESTAMP, userId INT, description TEXT, PRIMARY KEY (id), FOREIGN KEY(userId) REFERENCES users (id))";

    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER_CLASS = "driver.class.name";

    @Getter
    private HikariDataSource dataSource;

    static {
        try {
            Properties properties = Utils.loadFile(RESOURCES + "database.properties");

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
