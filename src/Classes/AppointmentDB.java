package Classes;


import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import static Utils.Alerts.informationAlert;

/**
 * This class is used to house the SELECT statements for any appointment queries.
 */
public class AppointmentDB{
    /** Creates an array list to store all the appointments in. */
    public static ObservableList<Appointment> appointmentsList =  FXCollections.observableArrayList();
    /**creates an array list to store all the designated customer appointments in*/
    public static ObservableList<Appointment> customersAppointmentList = FXCollections.observableArrayList();
    /**
     * Creates an array list to store appointment types in.
     */
    public static ObservableList<String> appointmentTypeList = FXCollections.observableArrayList();
    /** Gets the resource bundle to bring up appropriate display messages. */
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String aListFailure = rb.getString("appointmentListFailure");
    private static String er = rb.getString("error");
    private static String oLap = rb.getString("overlap");
    private static String sameTimes = rb.getString("sameTime");
    private static String sComesBefore = rb.getString("startComesBefore");
    private static String previousDay = rb.getString("past");
    private static String mustBeInBusinessHours = rb.getString("outsideBusinessHours");

    /**
     * This static method takes a customer id. It then puts all of that customerID's appointments
     * in the customer appointments array list. This array list is used to check against overlapping
     * appointments for the customer.
     * @param customerID the integer handed into the method to get associated appointments for that customer
     * @return an observable array list is returned
     */
    public static ObservableList<Appointment> getCustomersAppointmentList(int customerID) {
        try {
            customersAppointmentList.clear();
            String getAllAppointments = "SELECT * FROM appointments WHERE Customer_ID = " + customerID;
            DBQuery.makeQuery(getAllAppointments);
            ResultSet allAppointmentsRS = DBQuery.getResult();
            while (allAppointmentsRS.next()) {
                int appointmentID = allAppointmentsRS.getInt("Appointment_ID");
                String title = allAppointmentsRS.getString("Title");
                String description = allAppointmentsRS.getString("Description");
                String location = allAppointmentsRS.getString("Location");
                String type = allAppointmentsRS.getString("Type");
                Timestamp start = allAppointmentsRS.getTimestamp("Start");
                Timestamp end = allAppointmentsRS.getTimestamp("End");
                Timestamp createDate = allAppointmentsRS.getTimestamp("Create_Date");
                String createdBy = allAppointmentsRS.getString("Created_By");
                Timestamp lastUpdate = allAppointmentsRS.getTimestamp("Last_Update");
                String lastUpdatedBy = allAppointmentsRS.getString("Last_Updated_By");
                customerID = allAppointmentsRS.getInt("Customer_ID");
                int userID = allAppointmentsRS.getInt("User_ID");
                int contactID = allAppointmentsRS.getInt("Contact_ID");
                Appointment appointmentToAdd = new Appointment(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);
                customersAppointmentList.add(appointmentToAdd);
            }

        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(aListFailure);
            informationAlert.showAndWait();
        }
        return customersAppointmentList;
    }
    /** This static method takes no parameters. It pulls all the appointments and puts them in
     * the all appointments array list. This will be used to fill a tableview for displaying.
     * @return an observable array list is returned
     * */
    public static ObservableList<Appointment> getEveryAppointmentList() {
        try {
            String getAllAppointments = "SELECT * FROM appointments";
            DBQuery.makeQuery(getAllAppointments);
            ResultSet allAppointmentsRS = DBQuery.getResult();
            appointmentsList.clear();
            while (allAppointmentsRS.next()) {
                int appointmentID = allAppointmentsRS.getInt("Appointment_ID");
                String title = allAppointmentsRS.getString("Title");
                String description = allAppointmentsRS.getString("Description");
                String location = allAppointmentsRS.getString("Location");
                String type = allAppointmentsRS.getString("Type");
                Timestamp start = allAppointmentsRS.getTimestamp("Start");
                Timestamp end = allAppointmentsRS.getTimestamp("End");
                Timestamp createDate = allAppointmentsRS.getTimestamp("Create_Date");
                String createdBy = allAppointmentsRS.getString("Created_By");
                Timestamp lastUpdate = allAppointmentsRS.getTimestamp("Last_Update");
                String lastUpdatedBy = allAppointmentsRS.getString("Last_Updated_By");
                int customerID = allAppointmentsRS.getInt("Customer_ID");
                int userID = allAppointmentsRS.getInt("User_ID");
                int contactID = allAppointmentsRS.getInt("Contact_ID");
                Appointment appointmentToAdd = new Appointment(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);
                appointmentsList.add(appointmentToAdd);
            }
        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(aListFailure);
            informationAlert.showAndWait();
        }
        return appointmentsList;
    }
    /** This static method takes no parameters. It pulls distinct type names
     * from all the appointments and puts them in one list.
     * @return an observable array list is returned
     * */
    public static ObservableList<String> getAppointmentType(){
        try {
            String getDistinctType = "SELECT DISTINCT TYPE FROM appointments";
            DBQuery.makeQuery(getDistinctType);
            ResultSet rs = DBQuery.getResult();
            appointmentTypeList.clear();
            while (rs.next()) {
                String type = rs.getString("Type");
                appointmentTypeList.add(type);
            }
        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(aListFailure);
            informationAlert.showAndWait();
        }
        return appointmentTypeList;
    }

    /**
     * This static boolean performs checks for outside business hours and for customer overlapping appointments.
     * If it changes flips to false at any point, it will trigger a message to the user explaining why it
     * accept the start and end date-time
     * @param start the start time passed into the method
     * @param end the end time passed into the method
     * @param appID the app id passed into the method
     * @return a true or false value is returned
     */
    public static boolean checkForOverlappingAppointments(LocalDateTime start, LocalDateTime end, int appID){
        ZoneId  estZoneID = ZoneId.of("US/Eastern");
        ZoneId localZoneID = ZoneId.of(TimeZone.getDefault().getID());
        //create business start hour
        LocalDateTime startBusinessDay = LocalDateTime.of(2021, 3, 16, 8, 00);
        ZonedDateTime startBusinessDayZDT = ZonedDateTime.of(startBusinessDay, estZoneID);
        //create business end hour
        LocalDateTime endBusinessDay = LocalDateTime.of(2021, 3, 16, 22, 00);
        ZonedDateTime endBusinessDayZDT = ZonedDateTime.of(endBusinessDay, estZoneID);
        //convert start time received into EST
        ZonedDateTime startZDT = ZonedDateTime.of(start, estZoneID);
        Instant startToESTInstant = startZDT.toInstant();
        ZonedDateTime startToLocalZDT = startZDT.withZoneSameInstant(localZoneID);
        ZonedDateTime estToLocalZDT = startToESTInstant.atZone(localZoneID);
        //convert start time received into EST
        ZonedDateTime endZDT = ZonedDateTime.of(end, estZoneID);
        Instant endToESTInstant = endZDT.toInstant();
        ZonedDateTime endToLocalZDT = endZDT.withZoneSameInstant(localZoneID);
        ZonedDateTime estEndToLocalZDT = endToESTInstant.atZone(localZoneID);

        System.out.println("Start of business: " + startBusinessDayZDT.toLocalTime());
        System.out.println("End of Business: " + endBusinessDayZDT.toLocalTime());
        System.out.println("Converted time: " + startToLocalZDT.toLocalTime());
        System.out.println("Converted time: " + endToLocalZDT.toLocalTime());
        System.out.println(startToLocalZDT.toLocalDate().getDayOfWeek());
        informationAlert.setHeaderText(er);

        /**Checks to see if appointment is before 10pm est.
         * @return false
         * */
        if(startToLocalZDT.toLocalTime().isAfter(endBusinessDayZDT.toLocalTime())){
            informationAlert.setContentText(mustBeInBusinessHours);
            informationAlert.showAndWait();
            return false;
        }
        /**Checks to see if appointment is after 8am est.
         * @return false
         * */
        if(startToLocalZDT.toLocalTime().isBefore(startBusinessDayZDT.toLocalTime())){
            informationAlert.setContentText(mustBeInBusinessHours);
            informationAlert.showAndWait();
            return false;
        }
        /**Checks to see if appointment is before 10pm est.
         * @return false
         * */
        if(endToLocalZDT.toLocalTime().isAfter(endBusinessDayZDT.toLocalTime())){
            informationAlert.setContentText(mustBeInBusinessHours);
            informationAlert.showAndWait();
            return false;
        }
        /**Checks to see if appointment is after 8am est.
         * @return false
         * */
        if(endToLocalZDT.toLocalTime().isBefore(startBusinessDayZDT.toLocalTime())){
            informationAlert.setContentText(mustBeInBusinessHours);
            informationAlert.showAndWait();
            return false;
        }
        /**checks to see if the appointment is longer than a minute.
         * @return false
         * */
        if(start.equals(end)){
            informationAlert.setContentText(sameTimes);
            informationAlert.showAndWait();
            return false;
        }
        /**Confirms start time is after end time.
         * @return false
         * */
        if(start.isAfter(end)){
            informationAlert.setContentText(sComesBefore);
            informationAlert.showAndWait();
            return false;
        }
        LocalDateTime today = LocalDateTime.now();
        /**Checks to see if appointment is after the current date time
         * no appointment can be set in the past.
         * @return false
         * */
        if(start.isBefore(today)){
            informationAlert.setContentText(previousDay);
            informationAlert.showAndWait();
            return false;
        }
        /**Pulls all the appointments for a particular customer.
         * */
        for (Appointment appointment:customersAppointmentList) {
            LocalDateTime startLDT = appointment.getStart().toLocalDateTime();
            LocalDateTime endLDT = appointment.getEnd().toLocalDateTime();
            /**Confirms an appointment can not be put in the middle of another.
             * @return false
             * */
            if(start.isAfter(startLDT) && start.isBefore(endLDT)){
                informationAlert.setContentText(oLap);
                informationAlert.showAndWait();
                return false;
            }
            /**Confirms the end time is after the end
             * and before the beginning of appointments.
             * @return false
             * */
            if(end.isAfter(startLDT) && end.isBefore(endLDT)){

                informationAlert.setContentText(oLap);
                informationAlert.showAndWait();
                return false;
            }
            /**Confirms an appointment can not surround another
             * @return false
             * */
            if(start.isBefore(startLDT) && end.isAfter(endLDT)){

                informationAlert.setContentText(oLap);
                informationAlert.showAndWait();
                return false;
            }
            /**
             * This method is put last to confirm all other checks have been made.
             *If an appointment is being modified, this text allows the user to
             * keep the same appointment times
             */
            if(appID == appointment.getAppointmentID()){
                break;
            }
            /**This makes sure a newly added appointment can not have the
             * same start or end time as another appointment for the customer.
             * @return false
             * */
            if(start.equals(startLDT) || end.equals(endLDT)){
                informationAlert.setContentText(oLap);
                informationAlert.showAndWait();
            return false;
            }

        }
        return true;
    }
}
