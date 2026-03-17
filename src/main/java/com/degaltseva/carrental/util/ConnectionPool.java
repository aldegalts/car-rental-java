package com.degaltseva.carrental.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static ConnectionPool instance;
    private final BlockingQueue<Connection> pool;
    private final String url;
    private final String username;
    private final String password;

    private ConnectionPool(String url, String username, String password, int poolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.pool = new ArrayBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            pool.offer(createConnection());
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            Properties props = new Properties();
            try (InputStream is = ConnectionPool.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                if (is != null) {
                    props.load(is);
                }
            } catch (IOException e) {

            }

            String driver = env("DB_DRIVER", props.getProperty("db.driver", "org.postgresql.Driver"));
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("JDBC driver not found: " + driver, e);
            }

            String url = env("DB_URL", props.getProperty("db.url"));
            String username = env("DB_USERNAME", props.getProperty("db.username"));
            String password = env("DB_PASSWORD", props.getProperty("db.password"));
            int poolSize = Integer.parseInt(env("DB_POOL_SIZE", props.getProperty("db.pool.size", "10")));

            if (url == null || username == null || password == null) {
                throw new RuntimeException(
                        "Database not configured. Set env vars (DB_URL, DB_USERNAME, DB_PASSWORD) " +
                        "or create src/main/resources/db.properties");
            }

            instance = new ConnectionPool(url, username, password, poolSize);
        }
        return instance;
    }

    private static String env(String envKey, String fallback) {
        String value = System.getenv(envKey);
        return (value != null && !value.isBlank()) ? value : fallback;
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database connection", e);
        }
    }

    public Connection getConnection() {
        try {
            Connection connection = pool.take();
            if (connection.isClosed()) {
                connection = createConnection();
            }
            return connection;
        } catch (InterruptedException | SQLException e) {
            throw new RuntimeException("Failed to get connection from pool", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    pool.offer(connection);
                } else {
                    pool.offer(createConnection());
                }
            } catch (SQLException e) {
                pool.offer(createConnection());
            }
        }
    }

    public void shutdown() {
        for (Connection connection : pool) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore on shutdown
            }
        }
        pool.clear();
    }
}
