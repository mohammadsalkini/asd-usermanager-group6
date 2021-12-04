package view;

import controller.UserController;
import data.DBConnectorImpl;
import model.User;
import service.UserServiceImpl;
import java.util.Scanner;


public class UserView {

    private static final String ANMELDEN = "a";
    private static final String REGISTRIEREN = "r";
    private static final String PROGRAMM_BEENDEN = "b";
    private static final String PASSWORD_AENDERN = "p";
    private static final String ABMELDEN = "a";
    private static final String ACCOUNT_LOESCHEN = "l";

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserController userController = new UserController(new UserServiceImpl(new DBConnectorImpl()));


    public static String mainPage() {
        System.out.println("Willkommen im AccountManager");
        System.out.println("__________________________________\n");
        System.out.println("'a': Anmelden");
        System.out.println("'r': Registrieren");
        System.out.println("'b': Beenden");
        return scanner.next();
    }

    public static void renderPage() {
        while (true) {
            String userChoice = mainPage();

            switch (userChoice) {
                case ANMELDEN:
                    while (true) {
                        // TODO: show the login page -> this method should return a User object
                        // IF the returned user object is null then break, otherwise show the pager after login
                        break;
                    }
                    break;
                case REGISTRIEREN:
                    User user = registrationPage();
                    if (user != null) {
                        //TODO: show the page after login
                    }
                    break;
                case PROGRAMM_BEENDEN:
                    System.exit(0);
                default:
                    System.out.println("Falsche Eingabe!");
            }
        }
    }

    private static User registrationPage() {
        System.out.println("Geben Sie den Usernamen ein: ");
        String username = scanner.next();
        if (!userController.isUserExisting(username)) {
            System.out.println("Geben Sie das Passwort ein: ");
            String password = scanner.next();
            System.out.println("Geben Sie den Vornamen ein: ");
            String firstName = scanner.next();
            System.out.println("Geben Sie den Nachnamen ein: ");
            String lastName = scanner.next();
            return userController.createAccount(username, password, firstName, lastName);
        }
        System.out.println("Der Benutzer existiert bereits, bitte erneut versuchen.\n\n");
        return null;
    }

    private static boolean pageAfterLogin(User user) {
        while (true) {
            String userChoice = userControlPanel(user.getUsername());
            switch (userChoice) {
                case PASSWORD_AENDERN:
                    changePasswordPage(user);
                    break;
                case ACCOUNT_LOESCHEN:
                    break;
                case ABMELDEN:
                    break;
                default:
                    System.out.println("Falsche Eingabe");
                    break;
            }
        }
    }

    private static String userControlPanel(String username) {
        System.out.println("\nWillkommen " + username);
        System.out.println("__________________________________\n");
        System.out.println("'p': Passwort ändern.");
        System.out.println("'a': Abmelden.");
        System.out.println("'l': Account löschen.");
        return scanner.next();
    }

    private static boolean changePasswordPage(User user) {
        System.out.println("Neues Passwort: ");
        String newPassword = scanner.next();
        System.out.println("Neues Passwort nochmal eingeben: ");
        String newPassword2 = scanner.next();
        if (newPassword.equals(newPassword2)) {
            if (userController.updatePassword(user.getUsername(), user.getPassword(), newPassword)) {
                System.out.println("Passwort geändert.\n");
                return true;
            } else {
                System.out.println("Fehler.\n");
            }
        } else {
            System.out.println("Passwörter stimmen nicht überein.\n");
        }
        return false;
    }
}