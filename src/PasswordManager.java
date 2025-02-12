import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class PasswordManager {
    private Map<String, String> passwordMap;
    private FileHandler fileHandler;

    public PasswordManager() {
        passwordMap = new HashMap<>();
        fileHandler = new FileHandler();
        loadPasswordsFromFile(); //Currently does nothing
    }

    public void addPassword(String userName, String plaintTextPassword) throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Password newPassword = new Password(userName, EncryptionUtil.encryptPassword(plaintTextPassword));
        passwordMap.put(newPassword.userName, newPassword.password);
        //For debugging
        System.out.println(newPassword.toString());
    }

    //Cant implement for now
    public String retrievePassword(String userName, String inputPassword) {
        return null;
    }

    public void listUsernames() {

    }

    private void loadPasswordsFromFile() {
        //Need to create loadPasswords method
        //passwordMap = fileHandler.loadPasswords();
    }
}
