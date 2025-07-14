import java.sql.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/quizdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    /**
     * Establishes and returns a connection to the database using the provided URL, user, and password.
     *
     * @return A Connection object representing the established connection to the database.
     * @throws SQLException If a database access error occurs or the URL, user, or password are incorrect.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC Driver (important for Java 9+)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }

        // Return connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Closes the database connection, statement, and result set gracefully.
     */
    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Failed to close ResultSet: " + e.getMessage());
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Failed to close PreparedStatement: " + e.getMessage());
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failed to close Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Logs exceptions to the t_error_logger table for auditing/debugging.
     */
    public void logError(Exception e, String methodName, String createdBy) {
        String query = "INSERT INTO t_error_logger (Error, Error_Desc, Module, Record_Created_By) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setString(1, e.getClass().getName());
            preparedStatement.setString(2, e.getMessage());
            preparedStatement.setString(3, methodName);
            preparedStatement.setString(4, createdBy);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Error logged successfully.");
            }
        } catch (SQLException sqlEx) {
            System.out.println("Failed to log error: " + sqlEx.getMessage());
        }
    }
}
