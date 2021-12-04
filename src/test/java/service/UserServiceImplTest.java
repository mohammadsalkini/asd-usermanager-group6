package service;

import data.DBConnector;
import data.DBConnectorImpl;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String USERNAME = "Test";
    private static final String FIRST_NAME = "TestF";
    private static final String LAST_NAME = "TestL";
    private static final String PASSWORD = "TestP";

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    DBConnector connect;

    @Test
    void shouldCreateNewUserWhenUserIsNotExisting() {
        when(connect.selectUserByUsernameAndPassword(anyString(), anyString())).thenReturn(null);
        when(connect.addUser(anyString(), anyString(), anyString(), anyString())).thenReturn(new User());
        assertNotNull(userService.createNewUser(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD));
    }

    @Test
    void shouldReturnNullWhenUserIsExisting() {
        when(connect.selectUserByUsernameAndPassword(anyString(), anyString())).thenReturn(new User());
        assertNull(userService.createNewUser(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD));
    }


}