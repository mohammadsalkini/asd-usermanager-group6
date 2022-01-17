package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordEncryptorTest {
    private static final String Input = "salam";
    private static final String Output = "0582bd2c13fff71d7f40ef5586e3f4da05a3a61fe5ba9f0b4d06e99905ab83ea";

    @Test
    void ShouldDigestMessageToSha256() {
        String res = PasswordEncryptor.hashPassword(Input);
        assertEquals(Output.toLowerCase(), res.toLowerCase(), "PasswordEncryptor not working right");

    }
}