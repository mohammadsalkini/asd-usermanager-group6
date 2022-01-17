package controller;
import utils.PasswordEncryptor;
import model.User;
import service.UserService;
import java.util.Optional;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> login(String username, String password) {
        String encryptedPassword = PasswordEncryptor.hashPassword(password);
        return userService.getUserByUsernameAndPassword(username, encryptedPassword);
    }

    public Optional<User> createAccount(String username, String password, String firstName, String lastName) {
        String encryptedPassword = PasswordEncryptor.hashPassword(password);
        return userService.createNewUser(username, firstName, lastName, encryptedPassword);
    }

    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        String encryptedNewPassword = PasswordEncryptor.hashPassword(newPassword);
        Optional<User> userByUsername = userService.getUserByUsernameAndPassword(username, oldPassword);
        return userByUsername.filter(user -> userService.updatePassword(user, encryptedNewPassword)).isPresent();
    }

    public boolean deleteAccount(String username, String password) {
        return userService.deleteAccount(username, password);
    }

    public boolean isUserExisting(String userName) {
        return userService.getUserByUserName(userName).isPresent();
    }
}
