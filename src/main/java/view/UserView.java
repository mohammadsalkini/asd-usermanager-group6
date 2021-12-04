package view;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class UserView {

    private static final String ANMELDEN = "a";
    private static final String REGISTRIEREN = "r";
    private static final String PROGRAMM_BEENDEN = "b";
    private static final String PASSWORD_AENDERN = "p";
    private static final String ABMELDEN = "a";
    private static final String ACCOUNT_LOESCHEN = "l";
    static Scanner scanner = new Scanner(System.in);


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
                    while (true) {
                        // TODO: show the login page -> this method should return a User object
                        // IF the returned user object is null then break, otherwise show the pager after login
                        break;
                    }
                    break;
                case REGISTRIEREN:
                    // TODO: show the registration page -> this method should return a User Object
                    // IF the returned user object is null then break, otherwise show the pager after login
                    break;
                case PROGRAMM_BEENDEN:
                    System.exit(0);
                default:
                    System.out.println("Falsche Eingabe!");
            }
        }
    }
}
