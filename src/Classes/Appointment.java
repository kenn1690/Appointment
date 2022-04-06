package Classes;
import java.sql.Timestamp;
/**
 * This is a Class that helps construct an appointment and
 * only has the getters and setters used.
 */
public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * This is the constructor for the appointment class. It is used to make Appointment Objects in
     * other classes and controllers.
     * @param appointmentID appointment ID for appointment
     * @param title title for appointment
     * @param description description for appointment
     * @param location location for appointment
     * @param type location for appointment
     * @param start start date-time for appointment
     * @param end end date-time for appointment
     * @param createDate date of appointment creation
     * @param createdBy who the appointment was created by
     * @param lastUpdate when the appointment was last updated
     * @param lastUpdatedBy who the appointment was last updated by
     * @param customerID customer ID associated to the appointment
     * @param userID user ID associated to the appointment
     * @param contactID contact ID associated to the appointment
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       Timestamp start, Timestamp end, Timestamp createDate, String createdBy, Timestamp lastUpdate,
                        String lastUpdatedBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.lastUpdate = lastUpdate;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * This will return the start time of the appointment
     * as a timestamp.
     * @return start time gets returned as a timestamp
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * This will return the end time of the appointment
     * as a timestamp.
     * @return end time gets returned as a timestamp
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * This will return the appointment ID as an int.
     * @return appointment ID is returned as an int
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * This will return the title of the appointment
     * as a string.
     * @return title is returned as a string
     */
    public String getTitle() {
        return title;
    }

    /**
     * This will set the Title for the appointment.
     * @param title title is set as a string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This returns the description as a String.
     * @return discription is returned as a string
     */
    public String getDescription() {
        return description;
    }

    /**
     * This returns the location of the appointment as a String.
     * @return location is returned as string
     */
    public String getLocation() {
        return location;
    }

    /**
     * This return get the Type of appointment as a string.
     * @return type is reutned as string
     */
    public String getType() {
        return type;
    }

    /**
     * This will return the CustomerID belonging to the appointment.
     * @return customer ID returned as int
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * This will return the UserID belonging to the appointment.
     * @return user ID returned as int
     */

    public int getUserID() {
        return userID;
    }

    /**
     * This will return the ContactID belonging to the appointment.
     * @return contact ID returned as int
     */
    public int getContactID() {
        return contactID;
    }


}
