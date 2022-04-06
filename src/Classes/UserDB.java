package Classes;

import Utils.DBConnection;
import Utils.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import static Utils.Alerts.informationAlert;

/**
 * This class is responsible for performing some SELECT queries to get user information from the database.
 */
public class UserDB {
    /**
     * Creates an array list for users to be stored in.
     */
    public static ObservableList<User> userList = FXCollections.observableArrayList();
    /**
     * Creates an array list for users id's to be stored in.
     */
    public static ObservableList<Integer> userIDList = FXCollections.observableArrayList();
    /** Gets the resource bundle to bring up appropriate display messages. */
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String er = rb.getString("error");
    private static String canNotPullAppointmentsForUser = rb.getString("canNotPullAppForUser");
    private static String canNotGetLoginInformation = rb.getString("canNotGetLoginInfo");
    private static String canNotFillUserList = rb.getString("cantFillUserList");
    private static int userID = 0;

    /**
     * This action takes in a username and password. It then checks it against the database.
     * If there is a match, it returns true and the user can log in and see home view. If it is
     * false, the user will get an error message in the appropriate, currently set, system language.
     * Also, there is a log file that this method writes to everytime a user clicks submit.
     * @param userName username as a string
     * @param password password as a string
     * @return gets a true or false value
     */
    public static boolean loginCheck(String userName, String password){
        try {
            String checkLogin = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(checkLogin);
            ps.setString(1, userName);
            ps.setString(2, password);
            //DBQuery.queryDB(checkLogin);
            ResultSet userRS = ps.executeQuery();
            //ResultSet userRS = DBQuery.getResultSet();
            while (userRS.next()) {
                if (userRS.getString("User_Name").equals(userName) && userRS.getString("Password").equals(password)) {
                    userName = userRS.getString("User_Name");
                    Logger.log(userName, true);
                    userID = userRS.getInt("User_ID");
                    User currentUser = new User(userID, userName);
                    currentUser.setUserID(userID);
                    currentUser.setUserName(userName);
                    return true;
                }
            }
        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(canNotGetLoginInformation);
            informationAlert.showAndWait();
        }
        Logger.log(userName, false);
        return false;
    }

    /**
     * This method fills the user array list with all users in the DB.
     */
    public static void fillUserList(){
        try {
            String getUsers = "SELECT * FROM users";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(getUsers);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                User userToAdd = new User(userId, userName);
                userList.add(userToAdd);
                System.out.println(userId + " " + userName);
            }
        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(canNotFillUserList);
            informationAlert.showAndWait();
        }
    }
    /**
     * This method fills the user array list with all users ID's in the DB.
     * These Id's will be populated into a combo box the user can select to associate
     * a user with an appointment.
     */
    public static void fillUserIDList(){
        try {
            String getUsers = "SELECT User_ID FROM users";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(getUsers);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int id = rs.getInt("User_ID");
                userIDList.add(id);
            }
        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(canNotFillUserList);
            informationAlert.showAndWait();
        }
    }

    /**
     * Returns the userID. This list populates a combo box in the add and
     * modify appointments views.
     * @return gets user id as an int
     */
    public static ObservableList<Integer> getUserIDList(){return userIDList;}

    /**
     * This method gets the local time a user logs in and checks to see if they
     * have any appointments with in the next 15 minutes. Upon login the user
     * will receive a prompt whether they have an appointment or not. It will give
     * them the id, date and time.
     */
    public static void getAppointmentAtLogin() {
        try {
            int appointmentID = 0;
            LocalDateTime ldt = null;
            String checkForAppointments = "SELECT Appointment_ID, Start FROM appointments WHERE User_ID = ?";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(checkForAppointments);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            boolean isAppointment = false;
            while (rs.next()) {
                appointmentID = rs.getInt("Appointment_ID");
                Timestamp ts = rs.getTimestamp("Start");
                ldt = ts.toLocalDateTime();
                if(ldt.isAfter(LocalDateTime.now()) && ldt.isBefore(LocalDateTime.now().plusMinutes(15))) {
                    isAppointment = true;
                }
            }
            if(isAppointment) {
                System.out.println("you have an appointment with in 15 minutes");
                informationAlert.setContentText("You have an appointment: " + appointmentID + " with in 15 minutes. " +
                        "\nStart time: " + ldt);
                informationAlert.showAndWait();
            }
            else{
                System.out.println("no appointments right away");
                informationAlert.setContentText("You have no appointments right away :)");
                informationAlert.showAndWait();
            }
            //put call block for text outside based on boolean
        }
        catch (SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(canNotPullAppointmentsForUser);
            informationAlert.showAndWait();
        }
    }

    /**
     * Per requirements, this is displaying the default/local Zone Id for the system
     * the user is logging in form.
     * @return string value of where the system default of the PC
     */
    public static String showZoneID() {
        // create ZoneId object
        ZoneId zoneID = ZoneId.systemDefault();
        // printresult
        System.out.println("ZoneId: " + zoneID);
        return String.valueOf(zoneID);
    }

}
