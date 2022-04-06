package Utils;

import java.io.*;
import java.time.ZonedDateTime;

import static Utils.Alerts.informationAlert;

/**
 * This class creates a log everytime the user
 * logs into the application. It writes it to
 * a file called login_activity.txt.
 */

public class Logger {
    private static String filename = "login_activity.txt";
    public static void log (String username, boolean success) {
        try (FileWriter fw = new FileWriter(filename, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(ZonedDateTime.now() + " " + username + (success ? " Success" : " Failure"));
        } catch (IOException e) {
            System.out.println("Logger Error: " + e.getMessage());
            informationAlert.setHeaderText("IO writing error");
            informationAlert.setContentText("Unable to connect to write to file");
            informationAlert.showAndWait();
        }
    }
}
