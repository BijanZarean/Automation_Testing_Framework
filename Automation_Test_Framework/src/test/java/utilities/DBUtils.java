package utilities;

import java.sql.*;

public class DBUtils {
    public static boolean ownerExists(String firstName, String lastName, String address, String city, String telephone) {
        String sql = "SELECT * FROM owners WHERE first_name = ? AND last_name = ? AND address = ? AND city = ? AND telephone = ?";
        try (Connection connection = DriverManager.getConnection(DataReader.get("db.url"),
                DataReader.get("db.username"),
                DataReader.get("db.password"));
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, address);
            statement.setString(4, city);
            statement.setString(5, telephone);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    public static boolean deleteOwner(String firstName, String lastName, String address, String city, String telephone) {
        String sql = "DELETE FROM owners WHERE first_name = ? AND last_name = ? AND address = ? AND city = ? AND telephone = ?";
        try (Connection connection = DriverManager.getConnection(DataReader.get("db.url"),
                DataReader.get("db.username"),
                DataReader.get("db.password"));
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, address);
            statement.setString(4, city);
            statement.setString(5, telephone);

            //.executeUpdate() returns number of rows deleted.
            int rowsDeleted = statement.executeUpdate();
            //if at least one row is deleted, this will return true:
                return  rowsDeleted > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }
}
