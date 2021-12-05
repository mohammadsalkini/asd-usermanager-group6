package view;

import controller.UserController;
import data.DBConnectorImpl;
import model.User;
import service.UserServiceImpl;
import utils.SessionTimer;

import java.security.NoSuchAlgorithmException;
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

    private static final int TIMER_INTERVALL_IN_SECONDS = 5;
    private static final SessionTimer sessionTimer = new SessionTimer(TIMER_INTERVALL_IN_SECONDS);

    public static String mainPage() {
        System.out.println("Willkommen im AccountManager");
        System.out.println("__________________________________\n");
        System.out.println("'a': Anmelden");
        System.out.println("'r': Registrieren");
        System.out.println("'b': Beenden");
        return scanner.next();
    }

    public static void renderPage() throws NoSuchAlgorithmException {
        while (true) {
            String userChoice = mainPage();

            switch (userChoice) {
                case ANMELDEN:
                    User loginUser = loginPage();
                    if (loginUser != null) {
                        pageAfterLogin(loginUser);
                    }
                    break;
                case REGISTRIEREN:
                    User registerUser = registrationPage();
                    if (registerUser != null) {
                        pageAfterLogin(registerUser);
                    }
                    break;
                case PROGRAMM_BEENDEN:
                    System.exit(0);
                default:
                    System.out.println("Falsche Eingabe!");
            }
        }
    }

    private static User loginPage() throws NoSuchAlgorithmException {
        int count = 1;
        while (count <= 3) {
            System.out.println("Username: ");
            String username = scanner.next();
            System.out.println("Passwort: ");
            String password = scanner.next();
            User user = userController.login(username, password);
            if (user != null) {
                return user;
            } else {
                System.out.println("Username oder Passwort zum " + count + ". mal nicht korrekt eingegeben.\n");
                count++;
            }
        }
        return null;
    }

    private static User registrationPage() throws NoSuchAlgorithmException {
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

    private static void pageAfterLogin(User user) throws NoSuchAlgorithmException {
        while (true) {
            sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
            String userChoice = userControlPanel(user.getUsername());
            switch (userChoice) {
                case PASSWORD_AENDERN:
                    if (!sessionTimer.isSessionValid){
                        System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS + " Sekunden.");
                        return;
                    }
                    sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
                    if (!changePasswordPage(user)) {
                        return;
                    }
                    break;
                case ACCOUNT_LOESCHEN:
                    if (!sessionTimer.isSessionValid){
                        System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS + " Sekunden.");
                        return;
                    }
                    sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
                    if (!deleteAccountPage(user)) {
                        return;
                    }
                    break;
                case ABMELDEN:
                    return;
                default:
                    System.out.println("Falsche Eingabe");
                    break;
            }
        }
    }

    private static boolean deleteAccountPage(User user) {
        sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
        System.out.println("Möchten Sie den Account wirklich löschen? (y oder n)");
        String input2 = scanner.next();
        if (!sessionTimer.isSessionValid){
            System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS + " Sekunden.\n\n");
            return false;
        } else if (input2.equals("y")) {
            userController.deleteAccount(user.getUsername(), user.getPassword());
            System.out.println("Ihr Account wurde gelöscht!\n\n");
            return false;
        } else {
            System.out.println("Ihr Account konnte nicht gelöscht werden!");
            return true;
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

    private static boolean changePasswordPage(User user) throws NoSuchAlgorithmException {
        sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
        System.out.println("Neues Passwort: ");
        String newPassword = scanner.next();
        if (!sessionTimer.isSessionValid){
            System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS + " Sekunden.");
            return false;
        }
        sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
        System.out.println("Neues Passwort nochmal eingeben: ");
        String newPassword2 = scanner.next();
        if (!sessionTimer.isSessionValid){
            System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS + " Sekunden.");
            return false;
        }
        sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
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