package myfitnesspal.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class PasswordUtil {
    private static final SecureRandom RNG = new SecureRandom();

    private PasswordUtil() {
    }

    public static String newSalt() {
        byte[] salt = new byte[16];
        RNG.nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String hash(String salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((salt + password).getBytes(StandardCharsets.UTF_8));
            return bytesToHex(md.digest());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static String bytesToHex(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

