package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Console;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordEncryptorTest {
    private static final String Input = "salam";
    private static final String Output = "582BD2C13FFF71D7F40EF5586E3F4DA05A3A61FE5BA9F0B4D06E99905AB83EA";

    @Test
    void ShouldDigestMessageToSha256() throws NoSuchAlgorithmException {
        byte[] resByte = PasswordEncryptor.getSHA(Input);
        String res = PasswordEncryptor.toHexString(resByte);
        assertEquals(Output.toLowerCase(), res.toLowerCase(), "PasswordEncryptor not working right");

    }
}