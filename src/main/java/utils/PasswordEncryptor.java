package utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordEncryptor {
    private PasswordEncryptor() {
    }


    public static String hashPassword(String originalPassword) {
        return Hashing.sha256()
                .hashString(originalPassword, StandardCharsets.UTF_8)
                .toString();
    }
}  