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

public class ContactsDB {
    /**This is the array list that holds the contact ID's. */
    public static ObservableList<Contacts> contactsIDList = FXCollections.observableArrayList();
    /**This imports the resource bundle to get strings to fill labels/errors. */
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String er = rb.getString("error");
    private static String failedPullingContacts = rb.getString("cantPullContacts");

    /**
     * returns the contact ID array list.
     * @return int is returned for id
     */
    public static ObservableList<Contacts> getContactsID() {return contactsIDList;}

    public static void fillContactsIDList() {
        try {
            String getContacts = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(getContacts);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contacts contact = new Contacts(contactID, contactName, contactEmail);
                contactsIDList.add(contact);
            }
        }
        catch (SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(failedPullingContacts);
            informationAlert.showAndWait();
        }
    }

    public static Contacts getContactBasedOnID(int contactID){
        try {
            String getContacts = "SELECT * FROM contacts WHERE Contact_ID = " + contactID;
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(getContacts);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contacts contact = new Contacts(contactID, contactName, contactEmail);
                return contact;
            }
        }
        catch (SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(failedPullingContacts);
            informationAlert.showAndWait();
        }
        return null;
    }

}
