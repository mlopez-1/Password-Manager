import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte [] salt = new byte[16];
        random.nextBytes(salt);
        String saltString = Base64.getEncoder().encodeToString(salt);
        System.out.println(saltString);
    }
}
