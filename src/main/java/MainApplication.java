import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.UserView;
import java.security.NoSuchAlgorithmException;

public class MainApplication {

    private static Logger logger = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        try {
            UserView.renderPage();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error detected: " + e.getMessage());
        }
    }
}
