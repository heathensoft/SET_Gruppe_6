package no.hiof.set.g6.db;

import no.hiof.set.g6.dtdb.LocalUser;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyJDBC {

    // Metode for å hente ut alle brukere i ett spesifikt system
    public static List<LocalUser> getUsersByHubId(int hubId) {
        String url = "jdbc:mysql://127.0.0.1:3306/login_schema";
        String username = "root";
        String password = "HeiHei123";

        // Spørring som henter brukernavn og rolle for brukere basert på en bestemt hub_id
        String query = "SELECT user_name, role FROM LocalUser WHERE hub_id = ?";

        List<LocalUser> userList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Sett parameteren for hubId
            statement.setInt(1, hubId);

            // Kjøre spørringen
            ResultSet resultSet = statement.executeQuery();

            // Hent ut og lagre brukerinformasjon i LocalUser-objekter
            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String roleStr = resultSet.getString("role");
                LocalUser.Role role = LocalUser.Role.valueOf(roleStr.toUpperCase());

                // Opprett LocalUser-objekt
                LocalUser user = new LocalUser();
                user.setUserName(userName);
                user.setRole(role);

                // Legg til brukeren i listen
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;  // Returnerer listen med brukere
    }

    //Bare en test main metode, eksempel på hvordan metoden kan bli kalt.
    public static void main(String[] args) {
        // Henter brukere for en spesifikk hub_id
        List<LocalUser> users = getUsersByHubId(1);  // Endre til ønsket hub_id

        // Iterer gjennom listen og printer ut brukerinformasjon
        for (LocalUser user : users) {
            System.out.println("UserName: " + user.getUserName() +
                    ", Role: " + user.getRole());
        }
    }
}
