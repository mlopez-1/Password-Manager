import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        PasswordManager passwordManager = new PasswordManager();

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Password Manager\n");

        while (true) {
            System.out.print("Please enter your master password: ");
            String masterPassword = sc.nextLine();

            if (masterPassword.equals("master")) {
                while (true) {
                    System.out.println("1. Create a new password\n");
                    System.out.println("2. Retrieve password\n");
                    System.out.println("3. Exit\n");

                    System.out.print("Enter your choice: ");

                    String keyString = sc.nextLine();

                    switch (keyString) {
                        case "1" -> { //Make password
                            System.out.print("Enter name: ");
                            String userName = sc.nextLine();
                            System.out.print("Enter your password: ");
                            String newPassword = sc.nextLine();

                            passwordManager.addPassword(userName, newPassword);

                            System.out.println("Password added successfully!");
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
            } else if (masterPassword.equals("exit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid master password. Please try again\n");
            }
        }

    }
}
