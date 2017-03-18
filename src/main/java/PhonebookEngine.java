/**
 * Created by raiden on 3/14/17.
 */

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PhonebookEngine {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/phonebook";
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
        }
        return connection;
    }

    public static List<Contact> getContact(String query) throws SQLException {
        List <Contact> contactList = new ArrayList<Contact>();
        ResultSet results = null;
        Statement statement = null;

        try {
            Connection connection = loadDriver();
            statement = connection.createStatement();
            results = statement.executeQuery(query);

            while (results.next()) {
                Contact contact = new Contact();
                contact.setName(results.getString("name"));
                contact.setNumber(results.getInt("tel"));
                contactList.add(contact);
            }
        }  finally {
            if (results != null) try { results.close(); } catch (SQLException ignore) {}
            if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
        }
        return contactList;
    }
} // End of class
