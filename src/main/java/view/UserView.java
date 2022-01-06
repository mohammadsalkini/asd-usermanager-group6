package view;

import controller.UserController;
import data.DBConnectorImpl;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserServiceImpl;
import utils.SessionTimer;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Scanner;

public class UserView {

    private static Logger logger = LoggerFactory.getLogger(UserView.class);

    private static final String ANMELDEN = "a";
    private static final String REGISTRIEREN = "r";
    private static final String PROGRAMM_BEENDEN = "b";
    private static final String PASSWORD_AENDERN = "p";
    private static final String ABMELDEN = "a";
    private static final String ACCOUNT_LOESCHEN = "l";

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserController userController = new UserController(new UserServiceImpl(new DBConnectorImpl()));

    private static final int TIMER_INTERVALL_IN_SECONDS = 60;
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
        logger.debug("In renderPage method.");
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
                    Optional<User> optionalRegisterUser = registrationPage();
                    if (optionalRegisterUser.isPresent()) {
                        pageAfterLogin(optionalRegisterUser.get());
                    }
                    break;
                case PROGRAMM_BEENDEN:
                    System.exit(0);
                default:
                    logger.error("Falsche Eingabe!");
            }
        }
    }

    private static User loginPage() throws NoSuchAlgorithmException {
        logger.debug("In loginPage method.");
        int count = 1;
        while (count <= 3) {
            System.out.println("Username: ");
            String username = scanner.next();
            System.out.println("Passwort: ");
            String password = scanner.next();
            Optional<User> optionalUser = userController.login(username, password);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                System.out.println("Username oder Passwort zum " + count + ". mal nicht korrekt eingegeben.\n");
                count++;
            }
        }
        logger.debug("End of loginPage method.");
        return null;
    }

    private static Optional<User> registrationPage() throws NoSuchAlgorithmException {
        logger.debug("In registrationPage method.");
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
        logger.debug("End of registrationPage method.");
        return Optional.empty();
    }

    private static void pageAfterLogin(User user) throws NoSuchAlgorithmException {
        logger.debug("In pageAfterLogin method.");
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
                    System.out.println("Falsche Eingabe!");
                    break;
            }
        }
    }

    private static boolean deleteAccountPage(User user) {
        logger.debug("In deleteAccountPage method.");
        sessionTimer.resetTimer(TIMER_INTERVALL_IN_SECONDS);
        System.out.println("Möchten Sie den Account wirklich löschen? (y oder n)");
        String input2 = scanner.next();
        if (!sessionTimer.isSessionValid){
            System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS + " Sekunden.\n\n");
            logger.debug("End of deleteAccountPage method.");
            logger.info("Logout wegen Inaktivität von mehr als " + TIMER_INTERVALL_IN_SECONDS +" Sekunden.\n\n");
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
        logger.debug("In changePasswordPage method.");
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