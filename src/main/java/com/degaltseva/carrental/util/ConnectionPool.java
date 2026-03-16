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
                props.load(is);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load db.properties", e);
            }

            String driver = props.getProperty("db.driver");
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("JDBC driver not found: " + driver, e);
            }

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            int poolSize = Integer.parseInt(props.getProperty("db.pool.size", "10"));

            instance = new ConnectionPool(url, username, password, poolSize);
        }
        return instance;
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
