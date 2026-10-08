package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.SQLException;

import static org.example.Database.getConnection;

public class Brainstorage {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String serilize(NeuralNetwork brain) throws Exception{
        return objectMapper.writeValueAsString(brain);
    }

    public NeuralNetwork deserialize(String json) throws Exception{
        return objectMapper.readValue(json, NeuralNetwork.class);
    }

    public void save(NeuralNetwork brain) throws Exception {
        String json = serilize(brain);

        String sql = """
                    MERGE INTO best_brain (id, brain)
                    KEY (id)
                    VALUES (?, ?)
                """;

        try (Connection connection = getConnection();
            var statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, 1);
            statement.setString(2, json);

            statement.executeUpdate();
            System.out.println("Brain salvo com sucesso!");
        }
    }

    public void discard() throws SQLException {

        String sql = """
            DELETE FROM best_brain
            WHERE id = ?
            """;

        try (Connection connection = Database.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setLong(1, 1);

            statement.executeUpdate();
        }
    }

    public NeuralNetwork load() throws Exception {
        String sql = """
                    SELECT brain
                    FROM best_brain
                    WHERE id = ?
                """;

        try(Connection connection = Database.getConnection();
            var statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1,1);

            try (var result = statement.executeQuery()) {
                if(result.next()) {
                    String json = result.getString("brain");

                    return deserialize(json);
                }
            }
        }
        return null;
    }

    public NeuralNetwork copy(NeuralNetwork brain) throws Exception {
        String json = objectMapper.writeValueAsString(brain);

        return objectMapper.readValue(json, NeuralNetwork.class);
    }

    public static void testBrain() throws SQLException {

        String sql = """
            SELECT id, brain
            FROM best_brain
            """;

        try (Connection connection = getConnection();
             var statement = connection.prepareStatement(sql);
             var result = statement.executeQuery()) {

            boolean found = false;

            while (result.next()) {

                found = true;

                System.out.println("ID: " + result.getLong("id"));
                System.out.println("Brain: " + result.getString("brain"));
            }

            if(found) {
                System.out.println("No brain");
            }
        }
    }
}
