import view.UserView;
import java.security.NoSuchAlgorithmException;

public class MainApplication {

    public static void main(String[] args) {
        try {
            UserView.renderPage();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
