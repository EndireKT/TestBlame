import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class ContributionChecker {
    private static Map<String, User> users = new HashMap<>();
    private static ProcessBuilder pBuilder = new ProcessBuilder();
    private static Scanner inputScanner = new Scanner(System.in);

    public static void setFileToCheck() {
        System.out.println("Enter path to file directory to check for contribution:");
        String path = inputScanner.nextLine().trim();

        System.out.println("Enter java file name in the directiory for checking contribution:");
        String fileName = inputScanner.nextLine().trim();

        String commandInput = "cd " + path + " && get blame " + fileName; 
        pBuilder.command("cmd.exe", "/c", commandInput);
    }

    public static void readAndParseLine(Process process) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

                
        boolean hasLine = true;  
        while (hasLine) {
            String line = reader.readLine();
            if (line == null) {
                hasLine = false;
                continue;
            }
            parseLine(line);
            // System.out.println(line);
        }
    }

    public static void getContributionReport() {
        if (users.isEmpty()) {
            return;
        }
        User mostLineUser = null;
        User mostCharUser = null;
        for (Map.Entry<String,User> entry : users.entrySet()) {
            User user = entry.getValue();
            if (mostLineUser == null) {
                mostLineUser = user;
                mostCharUser = user;
            }
            if (user.linesContributed.size() > mostLineUser.linesContributed.size()) {
                mostLineUser = user;
            }
            if (user.totalChar > mostCharUser.totalChar) {
                mostCharUser = user;
            }
            System.out.println("User " + user.id + " has contributed line numbers: " + 
            user.linesContributed + " and a total number of " + user.totalChar + 
            " characters to the file.");
        }
        System.out.println("The user who contributed the most lines is " + mostLineUser.id 
        + " with " + mostLineUser.linesContributed.size() + " lines contributed.");
        System.out.println("The user who contributed the most characters is " + mostCharUser.id 
        + " with " + mostCharUser.totalChar + " characters contributed.");
    }

    public static boolean isCommitted(String part) {
        try {
            if (Integer.parseInt(part) == 0){
                return false;
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return true;
    }

    public static boolean isEmptyLine(String line) {
        try {
            Integer.parseInt(Character.toString(line.charAt(0)));
        } catch (NumberFormatException e) {
            return false;
        }
        Character check = line.charAt(1);
        if (check.equals(')')) {
            return true;
        }
        return false;
    }

    public static void parseLine(String line) {
        String[] parts = line.split(" ");
        if (!isCommitted(parts[0])){
            return;
        }
        String id = parts[1].substring(1).trim();
        String lineNo;
        boolean isEmpty = isEmptyLine(parts[parts.length - 1]);
        int charCount = 0;
        if (!isEmpty) {
            lineNo = parts[parts.length - 2].substring(0, 1);
            charCount = parts[parts.length - 1].length();
        } else {
            lineNo = parts[parts.length - 1].substring(0, 1);            
        }
        addContribution(id, Integer.parseInt(lineNo), charCount);
        // System.out.println(id + " contributed line number: " + lineNo + 
        // " with " + charCount + " characters");
    }

    private static void addContribution(String id, int lineNo, int count) {
        boolean isNewUser = !users.containsKey(id);
        if (isNewUser) {
            User newUser = new User(id, count, lineNo);
            users.put(id, newUser);
        } else {
            User user = users.get(id);
            user.newContribution(count, lineNo);
        }
    }

    public static void run() {
        //setFileToCheck();
        pBuilder.command("cmd.exe", "/c", "git blame README.md");
        Process process = pBuilder.start();


        try {
            readAndParseLine(process);

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getContributionReport();

    }

}
