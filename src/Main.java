import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String masterPassword = null;
        System.out.println("Welcome to Password Manager\n");
        Scanner sc = new Scanner(System.in);
        PasswordManager passwordManager = new PasswordManager();
        boolean isFirstRun = passwordManager.isFirstRun();


        if (!isFirstRun) {
            boolean authenticated = false;

            while (!authenticated) {
                System.out.print("Please enter your master password: ");
                masterPassword = sc.nextLine();

                if (passwordManager.verifyMasterPassword(masterPassword)) {
                    authenticated = true;
                } else if (masterPassword.equals("exit")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                } else {
                    System.out.println("Invalid master password");
                }
            }
        }

        byte[] salt = passwordManager.getStoredSalt();

        while (true) {
            System.out.println("1. Create a new password\n");
            System.out.println("2. Retrieve password\n");
            System.out.println("3. List all passwords\n");
            System.out.println("4. Exit\n");

            System.out.print("Enter your choice: ");

            String keyString = sc.nextLine();

            switch (keyString) {
                case "1" -> {  // Make password
                    System.out.print("Enter name: ");
                    String username = sc.nextLine();
                    System.out.print("Enter your password: ");
                    String newPassword = sc.nextLine();

                    passwordManager.addPassword(username, newPassword, masterPassword, salt);
                }
                case "2" -> {  // Get password
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();

                    System.out.println(passwordManager.retrievePassword(username, masterPassword, salt));
                }
                case "3" -> {  // Print list of passwords
                    passwordManager.listUsernames();
                }
                case "4" -> {
                    System.out.print("Exiting...");
                    System.exit(0);
                }
                default -> System.out.print("Invalid choice. Please try again\n\n");
            }
        }

    }
}
