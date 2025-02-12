import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

public class PasswordManager {
    private HashMap<String, Password> passwords;
    private FileHandler fileHandler;

    public PasswordManager() {

    }

    public void addPassword(String userName, String plaintTextPassword) throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Password password = new Password(userName, EncryptionUtil.encryptPassword(plaintTextPassword));

        //For debugging
        System.out.println(password.toString());
    }

    public String retrievePassword(String userName, String inputPassword) {
        return null;
    }

    public void listUsernames() {

    }

    private void loadPasswords() {

    }
}
