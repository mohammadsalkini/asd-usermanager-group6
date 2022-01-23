package service;

import model.User;
import java.util.Optional;

public interface UserService {

    Optional<User> createNewUser(String username, String firstName, String lastName, String password);
    boolean deleteAccount(String username, String password);
    boolean updatePassword(User user, String newPassword);
    Optional<User> getUserByUserName(String userName);
    Optional<User> getUserByUsernameAndPassword(String username, String password);

}
