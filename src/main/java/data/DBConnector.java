package data;

import model.User;

import java.util.List;

public interface DBConnector {
    List<User> selectAllUsers();
    User selectUserByUsernameAndPassword(String userName, String password);
    User addUser(String username, String firstName, String lastName, String password);
    boolean deleteUser(String username, String password);
    boolean updatePassword (String username, String password);
    User selectUserByUserName(String userName);
}
