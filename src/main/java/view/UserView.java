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

    private UserView () {
    }
    private static Logger logger = LoggerFactory.getLogger(UserView.class);

    private static final String ANMELDEN = "a";
    private static final String REGISTRIEREN = "r";
    private static final String PROGRAMM_BEENDEN = "b";
    private static final String PASSWORD_AENDERN = "p";
    private static final String ABMELDEN = "a";
    private static final String ACCOUNT_LOESCHEN = "l";
    private static final int TIMER_INTERVAL_IN_SECONDS = 60;

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserController userController = new UserController(new UserServiceImpl(new DBConnectorImpl()));
    private static final SessionTimer sessionTimer = new SessionTimer(TIMER_INTERVAL_IN_SECONDS);


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
        int counter = 0;
        boolean status = true;
        while (status) {
            counter ++;
            status = counter < Integer.MAX_VALUE;
            String userChoice = mainPage();
            switch (userChoice) {
                case ANMELDEN:
                    Optional<User> optionalLoginUser = loginPage();
                    if (optionalLoginUser.isPresent()) {
                        pageAfterLogin(optionalLoginUser.get());
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
                    break;
                default:
                    System.out.println("Falsche Eingabe!");
            }
        }
    }

    private static Optional<User> loginPage() throws NoSuchAlgorithmException {
        logger.debug("In loginPage method.");
        int loginAttempts = 1;
        while (loginAttempts <= 3) {
            System.out.println("Username: ");
            String userName = scanner.next();
            System.out.println("Passwort: ");
            String password = scanner.next();
            Optional<User> optionalUser = userController.login(userName, password);
            if (optionalUser.isPresent()) {
                return optionalUser;
            } else {
                System.out.println("Username oder Passwort zum " + loginAttempts + ". mal nicht korrekt eingegeben.\n");
                loginAttempts ++;
            }
        }
        logger.debug("End of loginPage method.");
        return Optional.empty();
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
            sessionTimer.resetTimer(TIMER_INTERVAL_IN_SECONDS);
            String userChoice = userControlPanel(user.getUsername());
            switch (userChoice) {
                case PASSWORD_AENDERN:
                    if (!isSessionValid()) {return;}
                    if (!changePasswordPage(user)) {
                        System.out.println("Passwort konnte nicht geändert werden");
                    }
                    break;
                case ACCOUNT_LOESCHEN:
                    if (!isSessionValid()) {return;}
                    if (!deleteAccountPage(user)) {return;}
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
        sessionTimer.resetTimer(TIMER_INTERVAL_IN_SECONDS);
        System.out.println("Möchten Sie den Account wirklich löschen? (y oder n)");
        String input2 = scanner.next();
        if (!isSessionValid()) {return false;}
        else if (input2.equals("y")) {
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
        sessionTimer.resetTimer(TIMER_INTERVAL_IN_SECONDS);
        System.out.println("Neues Passwort: ");
        String newPassword = scanner.next();
        if (!isSessionValid()) {return false;}
        System.out.println("Neues Passwort nochmal eingeben: ");
        String newPassword2 = scanner.next();
        if (!isSessionValid()) {return false;}
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

    private static boolean isSessionValid () {
        boolean status = true;
        if (!sessionTimer.isSessionValid){
            System.out.println("Logout wegen Inaktivität von mehr als " + TIMER_INTERVAL_IN_SECONDS + " Sekunden.\n\n");
            logger.info("Logout wegen Inaktivität von mehr als " + TIMER_INTERVAL_IN_SECONDS +" Sekunden.");
            status = false;
        }
        sessionTimer.resetTimer(TIMER_INTERVAL_IN_SECONDS);
        return status;
    }
}