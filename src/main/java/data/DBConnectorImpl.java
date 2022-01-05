package data;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnectorImpl implements DBConnector {

    private static Logger logger = LoggerFactory.getLogger(DBConnectorImpl.class);

    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String PASSWORD = "password";
    private static final String USERMANAGMENT = "userManagment";

    private Connection connect() {
        String url = "jdbc:sqlite:sqllite/identifier.sqlite";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }

    public User selectUserByUsernameAndPassword(String userName, String password) {
        //String sql = "SELECT username, firstName, lastName, password FROM userManagment WHERE username=? AND password=?";
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s=? AND %s=?", USERNAME, FIRSTNAME, LASTNAME, PASSWORD, USERMANAGMENT, USERNAME, PASSWORD);
        User user = null;
        try {
            try (Connection conn = this.connect();
                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = new User();
                    user.setUsername(resultSet.getString(USERNAME));
                    user.setFirstName(resultSet.getString(FIRSTNAME));
                    user.setLastName(resultSet.getString(LASTNAME));
                    user.setPassword(resultSet.getString(PASSWORD));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }

    public User addUser(String username, String firstName, String lastName, String password) {
        //String sql = "INSERT INTO userManagment(username,firstName,lastName,password) VALUES(?,?,?,?)";
        String sql = String.format("INSERT INTO %s(%s, %s, %s, %s) VALUES(?,?,?,?)", USERMANAGMENT, USERNAME, FIRSTNAME, LASTNAME, PASSWORD);
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return new User(username, firstName, lastName, password);
    }

    public boolean deleteUser(String username, String password) {
        //String sql = "DELETE FROM userManagment WHERE username = ? AND password =?";
        String sql = String.format( "DELETE FROM %s WHERE %s = ? AND %s =?", USERMANAGMENT, USERNAME, PASSWORD);
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updatePassword (String username, String password) {
        //String sql = "UPDATE userManagment SET password =? WHERE username =?";
        String sql = String.format("UPDATE %s SET %s =? WHERE username =?", USERMANAGMENT, PASSWORD, USERNAME);
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public User selectUserByUserName(String userName) {
        //String sql = "SELECT username, firstName, lastName, password FROM userManagment WHERE username=?";
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s=?", USERNAME, FIRSTNAME, LASTNAME, PASSWORD, USERMANAGMENT, USERNAME);
        User user = null;
        try {
            try (Connection connection = this.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = new User();
                    user.setUsername(resultSet.getString(USERNAME));
                    user.setFirstName(resultSet.getString(FIRSTNAME));
                    user.setLastName(resultSet.getString(LASTNAME));
                    user.setPassword(resultSet.getString(PASSWORD));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }
}
