package data;

import model.User;
import java.util.Optional;

public interface DBConnector {
    Optional<User> selectUserByUsernameAndPassword(String userName, String password);
    User addUser(String username, String firstName, String lastName, String password);
    boolean deleteUser(String username, String password);
    boolean updatePassword (String username, String password);
    Optional<User> selectUserByUserName(String userName);
}
