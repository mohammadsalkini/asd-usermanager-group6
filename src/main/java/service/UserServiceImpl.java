package service;

import data.DBConnector;
import model.User;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final DBConnector connect;

    public UserServiceImpl(DBConnector connect) {
        this.connect = connect;
    }

    @Override
    public Optional<User> createNewUser(String username, String firstName, String lastName, String password) {
        Optional<User> user = connect.selectUserByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            return Optional.of(connect.addUser(username, firstName, lastName, password));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteAccount(String username, String password) {
        return connect.deleteUser(username, password);
    }

    @Override
    public boolean updatePassword(User user, String newPassword) {
        return connect.updatePassword(user.getUsername(), newPassword);
    }

    public Optional<User> getUserByUserName(String userName) {
        return connect.selectUserByUserName(userName);
    }

    @Override
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        return connect.selectUserByUsernameAndPassword(username, password);
    }
}