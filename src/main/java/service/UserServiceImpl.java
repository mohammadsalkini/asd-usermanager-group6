package service;

import data.DBConnector;
import model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final DBConnector connect;

    public UserServiceImpl(DBConnector connect) {
        this.connect = connect;
    }

    @Override
    public User createNewUser(String username, String firstName, String lastName, String password) {
        User user = connect.selectUserByUsernameAndPassword(username, password);
        if (user == null) {
            return connect.addUser(username, firstName, lastName, password);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public boolean deleteAccount(String username) {
        return false;
    }

    @Override
    public boolean updatePassword(User user, String newPassword) {
        return connect.updatePassword(user.getUsername(), newPassword);
    }
    public User getUserByUserName(String userName) {
        return connect.selectUserByUserName(userName);
    }
}