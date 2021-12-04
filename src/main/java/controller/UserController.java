package controller;

import model.User;
import service.UserService;

import java.security.NoSuchAlgorithmException;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User createAccount (String username, String password, String firstName, String lastName) {
        return userService.createNewUser(username, firstName, lastName, password);
    }

    public boolean updatePassword (String username, String oldPassword, String newPassword) throws NoSuchAlgorithmException {
        User userByUsername = userService.getUserByUsernameAndPassword(username, oldPassword);
        return userService.updatePassword(userByUsername, newPassword);
    }

    public boolean isUserExisting (String userName) {
        return userService.getUserByUserName(userName) != null;
    }
}
