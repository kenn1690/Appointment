package Classes;

import Utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
 * This class helps is to make country data accessible.
 * Mostly it fills a country list for the user to
 * select from when adding a customer.
 */
public class Country {
    private int countryID;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    public static ObservableList<Country> countryList = FXCollections.observableArrayList();
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String er = rb.getString("error");
    private static String countryFail = rb.getString("countryFailure");

    /**
     * Constructor class for Country.
     * @param countryID id for county
     * @param country country name
     * @param createDate date it was created
     * @param createdBy who it was created by
     * @param lastUpdate when last update was made
     * @param lastUpdatedBy who last updated it
     */
    public Country (int countryID, String country, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy){
        this.countryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * This method fills the country list from the mysql database. This
     * list will be used later to fill a combo box.
     */
    public static void fillCountryList() {
        try {
            String selectCountries = "SELECT * FROM countries";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(selectCountries);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                LocalDate date = rs.getDate("Create_Date").toLocalDate();
                LocalTime time = rs.getTime("Create_Date").toLocalTime();
                LocalDateTime createDate = LocalDateTime.of(date, time);
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                Country addedCountry = new Country(countryID, country, createDate, createdBy, lastUpdate, lastUpdatedBy);
                countryList.add(addedCountry);
            }
        }
        catch (SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(countryFail);
        }
    }

    /**
     * Returns the country array list.
     * @return an observable array list is returned
     */
    public static ObservableList<Country> getCountryList() {
        return countryList;
    }

    /**
     * Returns the country id This is used when helping to filter
     * the first level division list in the add and modify customer controller.
     * @return country id as int
     */
    public int getCountryID() {
        return countryID;
    }
    /**This overrides the array list so when presented to user in combo box
     * or other FX element, it prints out correctly and not with memory allocation designations.
     * @return country name
     * */
    @Override
    public String toString(){
        return country;
    }



}
