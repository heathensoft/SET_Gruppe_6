package no.hiof.set.g6.db;
import java.sql.*;
public class MyJDBC {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/login_schema",
                    "root",
                    "HeiHei123"
            );

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

            while(resultSet.next()){
                System.out.println(resultSet.getString("first_name"));
                System.out.println(resultSet.getString("last_name"));
                System.out.println(resultSet.getString("email"));
                System.out.println(resultSet.getString("phone_number"));
                System.out.println(resultSet.getString("address"));
                System.out.println(resultSet.getString("role"));
                System.out.println(resultSet.getString("date_registered"));
                System.out.println();

            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}

