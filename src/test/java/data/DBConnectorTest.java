package data;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectorTest {

    private DBConnector dbConnector;

    @BeforeEach
    void setup() {
        dbConnector = new DBConnector();
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = dbConnector.selectAllUsers();
        assertAll(
                () -> assertFalse(users.isEmpty()),
                () -> assertEquals("drubinivitz0", users.get(0).getUsername()),
                () -> assertEquals("rcarvil2", users.get(2).getUsername())
        );
    }

    @Test
    void shouldGetUserByUsername() {
        User user = dbConnector.selectUserByUsername("hkrook1");
        assertAll(
                () -> assertEquals("hkrook1", user.getUsername()),
                () -> assertEquals("Hortense", user.getFirstName()),
                () -> assertEquals("Krook", user.getLastName()),
                () -> assertEquals("RVlBBbtijfU", user.getPassword())
        );
    }

    @Test
    void shouldReturnNullWhenUsernameDoesNotExist() {
        User user = dbConnector.selectUserByUsername("hkrook132");
        assertNull(user);
    }

    @Test
    void shouldAddNewUserToDB() {
        User user = dbConnector.addUser("TestUsername", "TestFirstName",
                "TestLastName", "TestPassword");

        assertAll(
                () -> assertEquals("TestUsername", user.getUsername()),
                () -> assertEquals("TestFirstName", user.getFirstName()),
                () -> assertEquals("TestLastName", user.getLastName()),
                () -> assertEquals("TestPassword", user.getPassword())
        );
    }

    @Test
    void shouldDeleteUserFromDB() {
        User user = dbConnector.addUser("TestUsername2", "TestFirstName2",
                "TestLastName2", "TestPassword2");

        assertAll(
                () -> assertEquals("TestUsername2", user.getUsername()),
                () -> assertEquals("TestFirstName2", user.getFirstName()),
                () -> assertEquals("TestLastName2", user.getLastName()),
                () -> assertEquals("TestPassword2", user.getPassword())
        );

        boolean isUserDeleted = dbConnector.deleteUser("TestUsername2");
        assertTrue(isUserDeleted);

        User deletedUser = dbConnector.selectUserByUsername("TestUsername2");
        assertNull(deletedUser);
    }
}