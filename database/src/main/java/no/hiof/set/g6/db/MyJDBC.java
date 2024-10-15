package no.hiof.set.g6.db;

import no.hiof.set.g6.dtdb.LocalUser;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MyJDBC {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Teknologiprosjekt";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "HeiHei123";

    // Metode for å hente all informasjon om brukere med hub_id = 1
    public static List<String> getAllUserInfo() {
        String query = """
            SELECT LocalUser.user_name, UserAccount.first_name, UserAccount.last_name, UserAccount.email,
                           HomeAddress.country, HomeAddress.state, HomeAddress.city, HomeAddress.street_address, HomeAddress.postal_code,
                           LocalUser.role
                    FROM LocalUser \s
                    JOIN UserAccount ON LocalUser.account_id = UserAccount.account_id\s
                    JOIN HomeAddress ON UserAccount.address_id = HomeAddress.address_id
                    WHERE LocalUser.hub_id = 1;
                    
        """;

        List<String> userInfoList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Kjører spørringen
            ResultSet resultSet = statement.executeQuery();

            // Henter ut og lagrer informasjonen som JSON-strukturer i listen
            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                String state = resultSet.getString("state");
                String city = resultSet.getString("city");
                String streetAddress = resultSet.getString("street_address");
                int postalCode = resultSet.getInt("postal_code");
                String roleStr = resultSet.getString("role");
                LocalUser.Role role = LocalUser.Role.valueOf(roleStr.toUpperCase());

                // Bygger opp en streng med all informasjon
                String userInfo = String.format(
                        "{ \"userName\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"country\": \"%s\", \"state\": \"%s\", \"city\": \"%s\", \"streetAddress\": \"%s\", \"postalCode\": %d, \"role\": \"%s\" }",
                        userName, firstName, lastName, email, country, state, city, streetAddress, postalCode, role
                );

                // Legger til informasjonen i listen
                userInfoList.add(userInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userInfoList;  // Returnerer listen
    }

    // Testmetode for å demonstrere hvordan man henter ut all brukerinformasjon
    public static void main(String[] args) {
        // Henter all informasjon om brukere
        List<String> userInfoList = getAllUserInfo();

        // Printer ut all brukerinformasjon
        for (String userInfo : userInfoList) {
            System.out.println(userInfo);
        }
    }
}
