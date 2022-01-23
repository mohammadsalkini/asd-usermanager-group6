package data;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectorImplTest {

    private DBConnectorImpl dbConnectorImpl;

    @BeforeEach
    void setup() {
        dbConnectorImpl = new DBConnectorImpl();
    }

    @Test
    void shouldGetUserByUsername() {
        Optional<User> user = dbConnectorImpl.selectUserByUsernameAndPassword("hkrook1", "RVlBBbtijfU");
        assertAll(
                () -> assertEquals("hkrook1", user.get().getUsername()),
                () -> assertEquals("Hortense", user.get().getFirstName()),
                () -> assertEquals("Krook", user.get().getLastName()),
                () -> assertEquals("RVlBBbtijfU", user.get().getPassword())
        );
    }

    @Test
    void shouldReturnNullWhenUsernameDoesNotExist() {
        Optional<User> user = dbConnectorImpl.selectUserByUsernameAndPassword("hkrook132", "asdawer");
        assertEquals(Optional.empty(), user);
    }

    @Test
    void shouldAddNewUserToDB() {
        User user = dbConnectorImpl.addUser("TestUsername", "TestFirstName",
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
        User user = dbConnectorImpl.addUser("TestUsername2", "TestFirstName2",
                "TestLastName2", "TestPassword2");

        assertAll(
                () -> assertEquals("TestUsername2", user.getUsername()),
                () -> assertEquals("TestFirstName2", user.getFirstName()),
                () -> assertEquals("TestLastName2", user.getLastName()),
                () -> assertEquals("TestPassword2", user.getPassword())
        );

        boolean isUserDeleted = dbConnectorImpl.deleteUser("TestUsername2", "TestPassword2");
        assertTrue(isUserDeleted);

        Optional<User> deletedUser = dbConnectorImpl.selectUserByUsernameAndPassword("TestUsername2", "adasrae");
        assertEquals(Optional.empty(), deletedUser);
    }

    @Test
    void shouldUpdateUser() {
        dbConnectorImpl.updatePassword("rcarvil2", "newPassword");
        Optional<User> user = dbConnectorImpl.selectUserByUsernameAndPassword("rcarvil2", "newPassword");
        assertNotNull(user);
    }

    @Test
    void shouldGetUserByUserName() {
        Optional<User> user = dbConnectorImpl.selectUserByUserName("hkrook1");
        assertAll(
                () -> assertEquals("hkrook1", user.get().getUsername()),
                () -> assertEquals("Hortense", user.get().getFirstName()),
                () -> assertEquals("Krook", user.get().getLastName()),
                () -> assertEquals("RVlBBbtijfU", user.get().getPassword())
        );
    }
}