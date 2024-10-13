package no.hiof.set.g6.db;
import java.sql.*;
import java.sql.*;

public class MyJDBC {

    //Metode for å hente ut alle brukere i ett spesifikt system
    public static void getUsersByHubId(int hubId) {
        String url = "jdbc:mysql://127.0.0.1:3306/login_schema";
        String username = "root";
        String password = "HeiHei123";

        String query = "SELECT LocalUser.user_name, UserAccount.first_name, UserAccount.last_name, LocalUser.role " +
                "FROM LocalUser " +
                "JOIN UserAccount ON LocalUser.account_id = UserAccount.account_id " +
                "JOIN Hubs ON LocalUser.hub_id = Hubs.hub_id " +
                "WHERE Hubs.hub_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Sett parameteren for hubId
            statement.setInt(1, hubId);

            // Kjøre spørringen
            ResultSet resultSet = statement.executeQuery();

            // Hent ut og skriv ut data
            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String role = resultSet.getString("role");

                // Skriv ut brukerinformasjon
                System.out.println("UserName: " + userName + ", First Name: " + firstName + ", Last Name: " + lastName + ", Role: " + role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Henter brukere for en spesifikk hub_id
        getUsersByHubId(1);  // Endre til det ønskede hub_id
    }
}


