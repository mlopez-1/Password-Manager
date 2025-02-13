import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionUtil {

    public static SecretKey deriveKey(String masterPassword, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 1024, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String encryptPassword(String password, String masterPassword, byte[] salt) throws Exception {
        // Get key
        SecretKey key = deriveKey(masterPassword, salt);

        // Prep AES cipher to encrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the password
        byte [] encryptedData = cipher.doFinal(password.getBytes());

        return new String(Base64.getEncoder().encode(encryptedData));
    }

    public static String decryptPassword(String encryptedPassword, String masterPassword, byte[] salt) throws Exception{
        // Get key
        SecretKey key = deriveKey(masterPassword, salt);

        // Prep AES cipher to decrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decrypt password
        byte [] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedData);
    }
}
