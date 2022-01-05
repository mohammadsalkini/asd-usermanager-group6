package data;

import model.User;

import java.util.List;

public interface DBConnector {
    User selectUserByUsernameAndPassword(String userName, String password);
    User addUser(String username, String firstName, String lastName, String password);
    boolean deleteUser(String username, String password);
    boolean updatePassword (String username, String password);
    User selectUserByUserName(String userName);
}
