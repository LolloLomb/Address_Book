import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class LoginManager {

    private static Connection connection;

    public static Connection createConnection(PropertiesReader propertiesReader) throws SQLException, IOException {

        String url = propertiesReader.getDbUrl();
        String username = propertiesReader.getDbUsername();
        String password = propertiesReader.getDbPassword();

        return DriverManager.getConnection(url, username, password);
    }

    public static boolean validate(String username, String password) {
        try {
            if (connection == null || connection.isClosed()) {

                throw new SQLException("Database connection is not established.");
            }

            String query = "SELECT password FROM user WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        return storedPassword.equals(password);
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setConnection(Connection connection){
        LoginManager.connection = connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
