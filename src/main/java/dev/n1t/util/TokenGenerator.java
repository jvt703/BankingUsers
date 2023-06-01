package dev.n1t.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

    private static final int TOKEN_LENGTH = 32;

    public static String generateResetToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
