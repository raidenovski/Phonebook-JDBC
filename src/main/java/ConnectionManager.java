/**
 * Created by raiden on 3/15/17.
 */
import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.sql.*;

public class ConnectionManager {

    // should I make them static or no? Makes sense to make them static to me...
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/contacts";
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

        while (results.next()) {
            String name = results.getString("name");
            int tel = results.getInt("tel");
            // this needs to be put into an object
        }

        if (results != null) {
            System.out.println("Retrieved valid results from DB");
        } else {
            System.out.println("Invalid results");
        }
        return results;
    }

    public static ResultSet updateDb(String updateQuery) throws SQLException {
        Connection connection = loadDriver();
        ResultSet update;
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        update = statement.executeQuery(updateQuery);

        return update;
        // write this method differently?
    }

}
