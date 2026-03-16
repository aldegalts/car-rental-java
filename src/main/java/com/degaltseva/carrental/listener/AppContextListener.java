package com.degaltseva.carrental.listener;

import com.degaltseva.carrental.util.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool pool = ConnectionPool.getInstance();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("sql/init.sql")) {
            if (is == null) {
                throw new RuntimeException("init.sql not found in classpath");
            }

            String sql = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Connection conn = pool.getConnection();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            } finally {
                pool.releaseConnection(conn);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }

        sce.getServletContext().log("Car Rental application initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().shutdown();
        sce.getServletContext().log("Car Rental application shut down");
    }
}
