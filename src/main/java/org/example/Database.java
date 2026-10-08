package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:h2:./data/autonomous_car";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTable() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS best_brain (
                        id BIGINT PRIMARY KEY,
                        brain CLOB NOT NULL
                    )
                """;

        try (Connection connection = getConnection();
            var statement = connection.createStatement()
        ) {
            statement.executeUpdate(sql);
        }
    }
}
