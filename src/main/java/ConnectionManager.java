/**
 * Created by raiden on 3/15/17.
 */
import javax.xml.transform.Result;
import java.sql.*;

public class ConnectionManager {

    // should I make them static or no? Makes sense to make them static to me...
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/conacts";
    private static final String USER = "java";
    private static final String PASS = "test123";
    private static Connection connection;

    private static Connection loadDriver() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("DB Driver loaded...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static ResultSet getResults(String query) throws SQLException {
        Connection connection = loadDriver();
        ResultSet results;
        Statement statement = connection.createStatement();
        results = statement.executeQuery(query);

        // write an if statement to check if good results are returned and close connections
        return results;
    }

    public static ResultSet updateDb(String query) throws SQLException {
        Connection connection = loadDriver();
        Result
    }

}
