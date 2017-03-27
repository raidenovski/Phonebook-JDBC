/**
 * Created by raiden on 3/14/17.
 */

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PhonebookEngine {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/phonebook?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC";
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
            System.exit(0);
        }
        return connection;
    }

    protected static void insertContact(String nameParam, int numParam) throws SQLException {
        String sql = "insert into contacts" + " (name, tel)" + " values (?,?)";
        Connection connection = loadDriver();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nameParam);
            statement.setInt(2, numParam);
            try {
                statement.executeUpdate();
                System.out.println("Contact " + nameParam + " with number " + numParam + " saved to contact list");
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    protected static boolean removeContact(String nameParam, int numParam) throws SQLException {
        String sql = "DELETE FROM contacts " + "WHERE name=? " + "AND tel=?";
        boolean isDeleted;

        Connection connection = loadDriver();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nameParam);
            statement.setInt(2, numParam);
            try {
                statement.executeUpdate();
                isDeleted = true;
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }

        return isDeleted;
    }

    protected static List<Contact> getContacts(String queryParam) throws SQLException {
        List<Contact> contactList = new ArrayList<Contact>();
        String sql = "select * from contacts where name=?";

        Connection connection = loadDriver();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, queryParam);
            try {
                ResultSet results = statement.executeQuery();

                while(results.next()) {
                    Contact contact = new Contact(
                            results.getString("name"), results.getInt("tel"));
                    contactList.add(contact);
                }
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
        return contactList;
    }

    protected static List<Contact> getAllContacts() throws SQLException {
        List<Contact> contactList = new ArrayList<Contact>();
        String sql = "select * from contacts order by name";

        Connection connection = loadDriver();
        try {
            Statement statement = connection.prepareStatement(sql);
            try {
                ResultSet results = statement.executeQuery(sql);

                while(results.next()) {
                    Contact contact = new Contact(
                            results.getString("name"), results.getInt("tel"));
                    contactList.add(contact);
                }
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
        return contactList;
    }

    protected static Contact getContactByNumber(int queryParam) throws SQLException {
    String sql = "select * from contacts " + "where tel=?";
    Contact contact = null;

    Connection connection = loadDriver();
    try {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, queryParam);

        try {
            ResultSet results = statement.executeQuery();
            while(results.next()) {
                contact = new Contact(results.getString("name"), results.getInt("tel"));
            }
        } finally {
            statement.close();
        }
    } finally {
        connection.close();
    }
    return contact;
   }
} // End of class
