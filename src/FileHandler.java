import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {
    private static final String FILE_NAME = "passwords.txt";

    // Check if the password file exists
    public boolean fileExists() {
        File file = new File(FILE_NAME);
        return file.exists();
    }

    // Create and update the password file with the given content
    public void updateFile(Map<String, String> passwordMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String firstLine = reader.readLine(); //Read and preserve salt and token

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                // Rewrite salt and token to file
                writer.write(firstLine);
                writer.newLine();

                // Write passwords to file
                for (Map.Entry<String, String> entry : passwordMap.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();  // Ensure each entry is on a new line
                }
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
            }
        }
        catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // Read the file and return a map of usernames to encrypted passwords
    public Map<String, String> readFile() {
        Map<String, String> passwordMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            reader.readLine();  // Skips salt and token

            // Reads passwords
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Remove leading/trailing whitespace
                if (!line.isEmpty()) {  // Skip empty lines
                    String[] parts = line.split(":", 2);  // Limit split to 2 parts (in case password contains ':')
                    if (parts.length == 2) {
                        passwordMap.put(parts[0], parts[1]);
                    } else {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return passwordMap;
    }
    
}
