import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManager {
    private Map<String, String> passwordMap;
    private final FileHandler fileHandler;
    private boolean isFirstRun = false;
    private String masterPassword;

    public PasswordManager(){
        passwordMap = new HashMap<>();
        fileHandler = new FileHandler();
        if (!fileHandler.fileExists()) {
            masterPassword = setMasterPassword();
            isFirstRun = true;
        }
        else {
            try {
                loadPasswordsFromFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public String setMasterPassword() {
        Scanner sc = new Scanner(System.in);
        System.out.print("No password file found. Set an initial master password: ");
        String masterPassword = sc.nextLine();

        try {
            // Create salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Create key
            SecretKey key = EncryptionUtil.deriveKey(masterPassword, salt);

            // Create token
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getEncoded(), "HmacSHA256"));
            byte[] token = mac.doFinal("verification".getBytes());

            // Encode salt and token
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedToken = Base64.getEncoder().encodeToString(token);

            // Write salt and token to file
            BufferedWriter writer = new BufferedWriter(new FileWriter("passwords.txt"));
            writer.write(encodedSalt + ":" + encodedToken);
            writer.newLine();
            writer.close();

            return masterPassword;
        }
        catch (Exception e) {
            System.out.println("Error while master password: " + e.getMessage());
            return null;
        }
    }

    public boolean verifyMasterPassword(String masterPassword) {
        try {
            // Read the stored salt and token from the file
            BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
            String metadataLine = reader.readLine();  // Reads first line
            reader.close();

            // Get salt and token
            String[] parts = metadataLine.split(":");
            if (parts.length != 2) {
                System.out.println("Invalid password file format.");
                return false;
            }

            byte[] decodedSalt = Base64.getDecoder().decode(parts[0]);
            byte[] decodedToken = Base64.getDecoder().decode(parts[1]);

            // Derive the key from the entered master password + stored salt
            SecretKey key = EncryptionUtil.deriveKey(masterPassword, decodedSalt);

            // Generate a new verification token using the derived key
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getEncoded(), "HmacSHA256"));
            byte[] newToken = mac.doFinal("verification".getBytes());

            // Compare the stored token with the new one
            if (MessageDigest.isEqual(decodedToken, newToken)) {
                System.out.println("Master password verified successfully!");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error verifying master password: " + e.getMessage());
            return false;
        }
        return false;
    }

    public void addPassword(String username, String plaintTextPassword, String masterPassword, byte[] salt) {
        try {
            Password newPassword = new Password(username, EncryptionUtil.encryptPassword(plaintTextPassword, masterPassword, salt));
            passwordMap.put(newPassword.username, newPassword.password);
            savePasswordsToFile();
            System.out.println("Password added successfully!");
        }
        catch (Exception e) {
            System.out.println("Error adding password: " + e.getMessage());
        }
    }

    //Cant implement for now
    public String retrievePassword(String username, String masterPassword, byte[] salt) {
        if (!passwordMap.containsKey(username)) {
            return "No password found for username: " + username;
        }
        try {
            String encryptedPassword = passwordMap.get(username);
            return EncryptionUtil.decryptPassword(encryptedPassword, masterPassword, salt);
        }
        catch (Exception e) {
            System.out.println("Error retrieving password: " + e.getMessage());
            return null;
        }
    }

    public void listUsernames() {
        for (String username : passwordMap.keySet()) {
            System.out.println(username);
        }
    }

    public void loadPasswordsFromFile(){
            passwordMap = fileHandler.readFile();  // Read passwords from the file into the map
    }

    private void savePasswordsToFile() {
        fileHandler.updateFile(passwordMap);
    }

    public byte[] getStoredSalt() {
        try (BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"))) {
            String line = reader.readLine();  // Read salt and token

            String encodedSalt = line.split(":")[0]; // Isolate salt

            return Base64.getDecoder().decode(encodedSalt); // Decode and return
        }
        catch (Exception e) {
            System.out.println("Error getting salt." + e.getMessage());
            return null;
        }
    }
}
