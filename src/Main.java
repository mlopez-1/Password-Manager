import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        System.out.println("Welcome to Password Manager\n");
        System.out.println("Please enter your master password: ");
        Scanner sc = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {

            System.out.println("1. Create a new password\n");
            System.out.println("2. Retrieve password\n");
            System.out.println("3. Exit\n");

            System.out.print("Enter your choice: ");

            String keyString = sc.nextLine();

            switch (keyString) {
                case "1" -> { //Make password
                    System.out.print("Enter password name: ");
                    String accountName = sc.nextLine();
                    System.out.print("Enter your password: ");
                    String newPassword = sc.nextLine();

                    Password password = new Password(accountName, newPassword);

                    EncryptionUtil.encryptPassword(password.password);
                }
                case "2" -> { //Get password
                    System.out.print("Enter encrypted password: ");
                    String encryptedPassword = sc.nextLine();

                    EncryptionUtil.decryptPassword(encryptedPassword);
                }
                case "3" -> {
                    System.out.print("Exiting...");
                    System.exit(0);
                }
                default -> System.out.print("Invalid choice. Please try again\n\n");
            }
        }
    }
}
