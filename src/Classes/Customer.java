package Classes;

import Utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static Utils.Alerts.informationAlert;

/**
 * This customer class allows us to create customers in other classes as well as
 * get the division name for the customer.
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private String createdBy;
    private String lastUpdatedBy;
    private int divisionID;
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String er = rb.getString("error");
    private static String customerCountryFail = rb.getString("custCountryFail");
    private static String customerFirstLevelDivisionFail = rb.getString("customerFLDFail");

    public Customer() {}

    /**
     * Constructor for Customer class.
     * @param customerID customers ID
     * @param customerName customers name
     * @param address customers address
     * @param postalCode customers zip code
     * @param phone customers phone number
     * @param createdBy who the customer was created by
     * @param lastUpdatedBy who last updated the customer
     * @param divisionID the state or division id for customer
     */
    public Customer(int customerID, String customerName, String address, String postalCode, String phone, String createdBy, String lastUpdatedBy, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }

    /**
     * Returns customer ID. Is used when deleting customers
     * @return customer id as int is returned
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Gets the customers name. This helps populate the selected customers' data into modify view
     * @return customer name as string
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets the customers address. This helps populate the selected customers' data into modify view
     * @return address for customer is returned
     */
    public String getAddress() {
        return address;
    }
    /**
     * Gets the customers zip code. This helps populate the selected customers' data into modify view
     * @return zip code is returned for customer
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     * Gets the customers address. This helps populate the selected customers' data into modify view
     * @return phone is returned for customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the customers Division ID. This helps get the division ID as this value is passed into the method
     * getDivisionName.
     * @return division ID as in is returned
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * This is needed for when a customer is passed into modify. This helps set the combo box
     * value to whatever the first level divsion data is from the selected customer from the Customer controller
     * view.
     * @return gets the division name the customer belongs to
     */
    public FirstLevelDivision getDivisionName() {
        int divisionID = getDivisionID();
        Integer.toString(divisionID);
        try {
            String select = "SELECT * FROM first_level_divisions WHERE Division_ID = " + divisionID;
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String division = rs.getString("Division");
                LocalDate date = rs.getDate("Create_Date").toLocalDate();
                LocalTime time = rs.getTime("Create_Date").toLocalTime();
                LocalDateTime createDate = LocalDateTime.of(date, time);
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("COUNTRY_ID");
                FirstLevelDivision returnedDivision = new FirstLevelDivision(divisionID, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryID);
                return returnedDivision;
            }
        }
        catch (SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(customerFirstLevelDivisionFail);
            informationAlert.showAndWait();
        }
        return null;
    }

    /**
     *This is needed for when a customer is passed into modify. This helps set the combo box
     *value to whatever the Country data is from the selected customer from the Customer controller
     * view.
     * @return country name is returned based on division ID
     */
    public Country getCountryName() {
        int divisionID = getDivisionID();
        int countryID = 0;
        Country addedCountry = null;
        try {
            String getCountryID = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID = " + divisionID;
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(getCountryID);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                countryID = rs.getInt("COUNTRY_ID");
            }
            String getCountry = "SELECT * FROM countries WHERE Country_ID = " + countryID;
            ps.execute(getCountry);
            rs = ps.getResultSet();
            while (rs.next()) {
                String country = rs.getString("Country");
                LocalDate date = rs.getDate("Create_Date").toLocalDate();
                LocalTime time = rs.getTime("Create_Date").toLocalTime();
                LocalDateTime createDate = LocalDateTime.of(date, time);
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                //Display above
                System.out.println(countryID + " | " + country + " | " + date + " " + time + " | " + createdBy + " | " + lastUpdatedBy);
                addedCountry = new Country(countryID, country, createDate, createdBy, lastUpdate, lastUpdatedBy);

            }
        }
        catch (SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(customerCountryFail);
            informationAlert.showAndWait();
        }
        return addedCountry;

    }


    /**This makes sure a newly added appointment can not have the
     * same start or end time as another appointment for the customer
     * @return customer id as string
     * */
         
    public String toString(){
        return Integer.toString(customerID);
    }
}
