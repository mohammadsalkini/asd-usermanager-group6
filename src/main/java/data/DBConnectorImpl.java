package data;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.Optional;

public class DBConnectorImpl implements DBConnector {

    private static final Logger logger = LoggerFactory.getLogger(DBConnectorImpl.class);

    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String PASSWORD = "password";
    private static final String USER_MANAGEMENT = "userManagment";

    private Connection connect() {
        String url = "jdbc:sqlite:sqllite/identifier.sqlite";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error("Unable to connect to DB - {}", e.getMessage());
        }
        return connection;
    }

    public Optional<User> selectUserByUsernameAndPassword(String userName, String password) {
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s=? AND %s=?", USERNAME, FIRSTNAME, LASTNAME, PASSWORD, USER_MANAGEMENT, USERNAME, PASSWORD);
        return prepareSqlExecuteStatement(sql, new String[]{userName, password});
    }

    public User addUser(String username, String firstName, String lastName, String password) {
        String sql = String.format("INSERT INTO %s(%s, %s, %s, %s) VALUES(?,?,?,?)", USER_MANAGEMENT, USERNAME, FIRSTNAME, LASTNAME, PASSWORD);
        executeUpdateStatement(sql, new String[]{username, firstName, lastName, password});
        return new User(username, firstName, lastName, password);
    }

    public boolean deleteUser(String username, String password) {
        String sql = String.format("DELETE FROM %s WHERE %s = ? AND %s =?", USER_MANAGEMENT, USERNAME, PASSWORD);
        return executeUpdateStatement(sql, new String[]{username, password});
    }

    public boolean updatePassword(String username, String password) {
        String sql = String.format("UPDATE %s SET %s =? WHERE %s =?", USER_MANAGEMENT, PASSWORD, USERNAME);
        return executeUpdateStatement(sql, new String[]{password, username});
    }

    @Override
    public Optional<User> selectUserByUserName(String userName) {
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s=?", USERNAME, FIRSTNAME, LASTNAME, PASSWORD, USER_MANAGEMENT, USERNAME);
        return prepareSqlExecuteStatement(sql, new String[]{userName});
    }

    private Optional<User> prepareSqlExecuteStatement(String sql, String[] parameters) {
        logger.debug("DBConnectorImpl - prepareSqlExecuteStatement method starts");
        Optional<User> user = Optional.empty();
        try {
            try (Connection connection = this.connect();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int index = 0; index < parameters.length; index++) {
                    preparedStatement.setString(index + 1, parameters[index]);
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = Optional.of(new User());
                    user.get().setUsername(resultSet.getString(USERNAME));
                    user.get().setFirstName(resultSet.getString(FIRSTNAME));
                    user.get().setLastName(resultSet.getString(LASTNAME));
                    user.get().setPassword(resultSet.getString(PASSWORD));
                }
            }
        } catch (SQLException e) {
            logger.error("Unable to prepare Sql execute statement - {}", e.getMessage());
        }
        logger.debug("DBConnectorImpl - prepareSqlExecuteStatement method ends");
        return user;
    }

    private boolean executeUpdateStatement(String sql, String[] parameters) {
        logger.debug("DBConnectorImpl - executeUpdateStatement method starts");
        try (Connection connection = this.connect(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int index = 0; index < parameters.length; index++) {
                preparedStatement.setString(index + 1, parameters[index]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Unable to execute update statement - {}", e.getMessage());
            return false;
        }
        logger.debug("DBConnectorImpl - executeUpdateStatement method ends");
        return true;
    }
}
