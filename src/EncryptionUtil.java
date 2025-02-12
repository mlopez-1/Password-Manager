import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String saltString = "ykSI1lJLBZw73HaqkQ1cZg==";
    private static final byte [] decodedSalt = Base64.getDecoder().decode(saltString.getBytes());

    //Generates key for given password
    private static SecretKey deriveKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), decodedSalt, 1024, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static void encryptPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //Get key
        SecretKey key = deriveKey(password);

        //Prep AES cipher to encrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        //Encrypt the password
        byte [] encryptedData = cipher.doFinal(password.getBytes());
        String messageString = new String(Base64.getEncoder().encode(encryptedData));

        System.out.println("Encrypted message: " + messageString + "\n");
    }

    public static void decryptPassword(String encryptedPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        //Get key
        SecretKey key = deriveKey(encryptedPassword);

        //Prep AES cipher to decrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        //Decrypt password
        byte [] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        String decryptedPassword = new String(decryptedData);

        System.out.println("Password: " + decryptedPassword);
    }
}
