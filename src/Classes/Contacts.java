package Classes;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import static Utils.Alerts.informationAlert;
/**This class is for the contacts. The main purpose is to fill the contact ID list
 * for user to be able to choose the contact ID when adding an appointment. */
public class Contacts {
    private int contactID;
    private String contactName;
    private String contactEmail;

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * This is the constructor class for the contacts.
     * @param contactID integer to identify customer
     * @param contactName string to identify customer name
     * @param contactEmail contact for customer as a string
     */
    public Contacts(int contactID, String contactName, String contactEmail){
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }
    /**This overrides the array list so when presented to user in combo box
     * or other FX element, it prints out correctly and not with memory allocation designations.
     * @return contact id as a string
     * */
    @Override
    public String toString(){
        return contactID + " " + contactName;
    }


}
