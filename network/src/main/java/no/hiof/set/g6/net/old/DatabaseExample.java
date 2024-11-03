package no.hiof.set.g6.net.old;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseExample {
    public static void main(String[] args) {
        // Database connection parameters
        String url = "jdbc:mysql://itstud.hiof.no:3306/tek2024_g6"; // URL til databasen
        String username = "mahmadg"; // Brukernavn
        String password = "G7m$2ZpQ"; // Passord

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Koble til databasen
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Koblet til databasen.");

            // SQL-spørring
            String query = "SELECT * FROM test";
            statement = connection.prepareStatement(query);

            // Utfør spørringen
            resultSet = statement.executeQuery();

            // Hent og skriv ut resultatene
            while (resultSet.next()) {
                int testId = resultSet.getInt("test_id");
                String testValue = resultSet.getString("test");

                System.out.println("Test ID: " + testId + ", Test: " + testValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Lukk ressurser
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
