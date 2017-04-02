/**
 * Created by raiden on 3/14/17.
 */

import javax.xml.transform.Result;
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

    protected static boolean updateContact(String whatToChange, String toChangeParam) throws SQLException {
        String sql = "update contacts " + "set name =? " + "where name =?";
        boolean isChanged;

        // TODO find a way to make the updates to DB permanent
        Connection connection = loadDriver();
        connection.setAutoCommit(false);
        try {
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, whatToChange);
            statement.setString(2, toChangeParam);
            try {
                statement.executeUpdate();
                isChanged = true;
                connection.commit();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
        return isChanged;
    }

    protected static List<Contact> getContacts(String queryParam) throws SQLException {
        List<Contact> contactList = new ArrayList<Contact>();
        String sql = "select * from contacts where name=?";
        Object[] queryParamsList = new Object[]{queryParam};

        contactList = findContactsByQuery(sql, queryParamsList);
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

                while (results.next()) {
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
        Object[] queryParamsList = new Object[]{queryParam};

        List<Contact> resultList = findContactsByQuery(sql, queryParamsList);
        return resultList.get(0);
    }

    // private methods
    private static List<Contact> findContactsByQuery(String queryString, Object[] queryParams) {
        PreparedStatement statement = null;

        try {
            try {
                Connection connection = loadDriver();
                statement = connection.prepareStatement(queryString);

                if (queryParams != null) {
                    for (int i = 0; i < queryParams.length; i++) {
                        statement.setObject(i + 1, queryParams[i]);
                    }
                }
                ResultSet results = statement.executeQuery();
                List<Contact> contactList = new ArrayList<Contact>();

                while (results.next()) {
                    contactList.add(new Contact(results.getString("name"), results.getInt("tel")));
                }
                return contactList;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
} // End of class
