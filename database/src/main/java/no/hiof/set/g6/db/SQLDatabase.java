package no.hiof.set.g6.db;

import java.sql.*;
import no.hiof.set.g6.ny.DatatypeArray;
import no.hiof.set.g6.ny.LocalUser;
import no.hiof.set.g6.ny.Locks;
import no.hiof.set.g6.ny.UserAccount;
import no.hiof.set.g6.ny.HomeAddress;


public class SQLDatabase implements PrototypeDB {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Teknologiprosjekt"; // Database URL
    private static final String USERNAME = "root"; // Database username
    private static final String PASSWORD = "HeiHei123"; // Database password



    @Override
    public LocalUser.Role getUserRole(LocalUser user) {
        return null;
    }

    @Override
    public DatatypeArray<UserAccount> searchForAccount(UserAccount account) throws Exception {
        return null;
    }

    @Override
    public DatatypeArray<LocalUser> allStoredLocalUsers() throws Exception {
        String query = """
            SELECT LocalUser.user_name, UserAccount.first_name, UserAccount.last_name, UserAccount.email,
                           HomeAddress.country, HomeAddress.state, HomeAddress.city, HomeAddress.street_address, HomeAddress.postal_code,
                           LocalUser.role, UserAccount.phone_numbers
                    FROM LocalUser  
                    JOIN UserAccount ON LocalUser.account_id = UserAccount.account_id 
                    JOIN HomeAddress ON UserAccount.address_id = HomeAddress.address_id
                    WHERE LocalUser.hub_id = 1;
        """;

        DatatypeArray<LocalUser> localUsers = new DatatypeArray<>(LocalUser.class); // Initialize DatatypeArray

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            //Execute SQL query
            ResultSet resultSet = statement.executeQuery();

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
                UserAccount account = new UserAccount(firstName, lastName, email, phoneNumbers);
                account.getAddress().set(address);

                // Create LocalUser object
                LocalUser localUser = new LocalUser(account, userName, role);

                // Add LocalUser object to the DatatypeArray
                localUsers.add(localUser);
            }
        }

        catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e); // Handle SQL exceptions
        }

        return localUsers;
    }

    @Override
    public boolean addLocalUser(LocalUser user) throws Exception {
        return false;
    }

    @Override
    public boolean removeLocalUser(LocalUser user) throws Exception {
        return false;
    }

    @Override
    public boolean editLocalUser(LocalUser user) throws Exception {
        return false;
    }

    @Override
    public DatatypeArray<Locks> allStoredLocks() throws Exception {
        return null;
    }

    @Override
    public boolean editLock(Locks lock) throws Exception {
        return false;
    }
}
