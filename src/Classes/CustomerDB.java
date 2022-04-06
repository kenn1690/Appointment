package Classes;
import Utils.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import static Utils.Alerts.informationAlert;

/**
 * This class is for most of the Customer SQL statements/queries.
 * It allows us to get all customers, delete and get all customer ids.
 */

public class CustomerDB {
    /**
     * An array list for all customers.
     */
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    /**
     * An array list for all customers ids. This list will be used to populate a combo box
     * in appointments view.
     */
    public static ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
    /**gets the resource bundle to bring up appropriate display messages. */
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String er = rb.getString("error");
    private static String getAllCustomerFail = rb.getString("fetchAllCustomerFail");
    private static String cantDeleteCustomer = rb.getString("canNotDeleteCustomer");

    /**
     * This will populate the customer array list and return in. This item is returned
     * on each call since items can be added and removed after application is launched.
     * It clears the list each time as well to not show the user duplicates
     * @return gets every customer that has been created and returns them
     */
    public static ObservableList<Customer> getEveryCustomer() {
        customerList.clear();
        try {
            String getAllCustomers = "SELECT * FROM customers";
            DBQuery.makeQuery(getAllCustomers);
            ResultSet allCustomersRS = DBQuery.getResult();
            while (allCustomersRS.next()) {
                int id = allCustomersRS.getInt("Customer_ID");
                String name = allCustomersRS.getString("Customer_Name");
                String address = allCustomersRS.getString("Address");
                String postalCode = allCustomersRS.getString("Postal_Code");
                String phone = allCustomersRS.getString("Phone");
                String createdBy = allCustomersRS.getString("Created_By");
                String lastUpdatedBy = allCustomersRS.getString("Last_Updated_By");
                int divisionID = allCustomersRS.getInt("Division_ID");
                Customer customerToAdd = new Customer(id, name, address, postalCode, phone, createdBy, lastUpdatedBy, divisionID);
                customerList.add(customerToAdd);
            }
        }
        catch(SQLException | RuntimeException e){
            System.out.println("SQL error: " + e);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(getAllCustomerFail);
            informationAlert.showAndWait();
        }
        return customerList;
    }

    /**
     * This gets all customers ID's and puts them into an array list as an integer.
     * This integer is populated into a combo box in the add and modify appointment
     * controllers. This is part of the requirement. The user must be able to select
     * a customer for an appointment.
     * @return gets every customer ID and returns them in an observable array list
     */
    public static ObservableList<Integer> getEveryCustomerID() {
        customerIDList.clear();
        try {
            String getAllCustomers = "SELECT Customer_ID FROM customers";
            DBQuery.makeQuery(getAllCustomers);
            ResultSet allCustomersRS = DBQuery.getResult();
            while (allCustomersRS.next()) {
                int id = allCustomersRS.getInt("Customer_ID");
                customerIDList.add(id);
            }
        }
        catch(SQLException | RuntimeException e){
            System.out.println("SQL error: " + e);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(getAllCustomerFail);
            informationAlert.showAndWait();
        }
        return customerIDList;
    }

    /**
     * This method checks to make sure a customer can not be deleted
     * before deleting all the related appointments. If a customer
     * still has an appointment, they can not be deleted.
     * @param customerToDelete customer that is selected from customer view and passed to this method
     */
    public static void deleteCustomer(Customer customerToDelete)  {
        try {
        int goingToDelete = customerToDelete.getCustomerID();
        String check = "SELECT Customer_ID FROM appointments WHERE Customer_ID = " + goingToDelete;
        DBQuery.makeQuery(check);
        ResultSet rs = DBQuery.getResult();

            if(rs.next()){
                System.out.println("Can not delete");
                informationAlert.setHeaderText(er);
                informationAlert.setContentText(cantDeleteCustomer);
                informationAlert.showAndWait();
            }
            else{
                String delete = "DELETE FROM customers WHERE Customer_ID = " + goingToDelete;
                DBQuery.makeQuery(delete);
            }
        } catch (SQLException se) {
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(cantDeleteCustomer);
            informationAlert.showAndWait();
        }
    }
}
