package data;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnectorImpl implements DBConnector{

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

    public List<User> selectAllUsers() {
        String sql = "SELECT username, firstName, lastName, password FROM userManagment";
        List<User> users = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getString("username"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("password")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return users;
    }

    public User selectUserByUsernameAndPassword(String userName, String password) {
        String sql = "SELECT username, firstName, lastName, password FROM userManagment WHERE username=? AND password=?";
        User user = null;
        try {
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setPassword(rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }

    public User addUser(String username, String firstName, String lastName, String password) {
        String sql = "INSERT INTO userManagment(username,firstName,lastName,password) VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return new User(username, firstName, lastName, password);
    }

    public boolean deleteUser(String username, String password) {
        String sql = "DELETE FROM userManagment WHERE username = ? AND password =?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean updatePassword (String username, String password) {
        String sql = "UPDATE userManagment SET password =? WHERE username =?";
        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, password);
            pstmt.setString(2, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public User selectUserByUserName(String userName) {
        String sql = "SELECT username, firstName, lastName, password FROM userManagment WHERE username=?";
        User user = null;
        try {
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setPassword(rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }
}
