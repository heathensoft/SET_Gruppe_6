package no.hiof.set.g6.db;

import no.hiof.set.g6.dtdb.LocalUser;
import org.json.simple.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import no.hiof.set.g6.dt.G6JSON;
import no.hiof.set.g6.dt.HomeAddress;
import no.hiof.set.g6.dt.UserAccount;

public class MyJDBC {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Teknologiprosjekt"; // Database URL
    private static final String USERNAME = "root"; // Database username
    private static final String PASSWORD = "HeiHei123"; // Database password

    // Method to retrieve all user information for users with hub_id = 1
    public List<JSONObject> getAllUserInfo() {

        // SQL query
        String query = """
            SELECT LocalUser.user_name, UserAccount.first_name, UserAccount.last_name, UserAccount.email,
                           HomeAddress.country, HomeAddress.state, HomeAddress.city, HomeAddress.street_address, HomeAddress.postal_code,
                           LocalUser.role, UserAccount.phone_numbers
                    FROM LocalUser  
                    JOIN UserAccount ON LocalUser.account_id = UserAccount.account_id 
                    JOIN HomeAddress ON UserAccount.address_id = HomeAddress.address_id
                    WHERE LocalUser.hub_id = 1;
        """;

        List<JSONObject> userList = new ArrayList<>(); // List to store user information as JSON objects

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery();

            // Loop through the result set
            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                String phoneNumbers = resultSet.getString("phone_numbers");

                String state = resultSet.getString("state");
                String city = resultSet.getString("city");
                String streetAddress = resultSet.getString("street_address");
                int postalCode = resultSet.getInt("postal_code");
                String roleStr = resultSet.getString("role");
                LocalUser.Role role = LocalUser.Role.valueOf(roleStr.toUpperCase());

                // Create address and user account objects
                HomeAddress address = new HomeAddress(country, state, city, streetAddress, postalCode);
                UserAccount account = new UserAccount(firstName, lastName, email);
                account.getAddress().set(address);
                account.getPhoneNumbers().add(phoneNumbers);

                // Convert the UserAccount object to a JSON object
                JSONObject jsonObject = G6JSON.userAccountToJSON(account);

                // Add the JSON object to the userList
                userList.add(jsonObject);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }

        return userList; // Return the list of JSON objects
    }

    // Main method for testing the retrieval of user information
    public static void main(String[] args) {
        MyJDBC myJDBC = new MyJDBC();


        List<JSONObject> userInfoList = myJDBC.getAllUserInfo();


        for (JSONObject userInfo : userInfoList) {
            System.out.println(userInfo.toJSONString());
        }
    }
}
