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
 * This class helps construct and run the SQL statements for
 * first level division data.
 */
public class FirstLevelDivision {
    private int divisionID;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryID;
    public static ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();
    private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
    private static String er = rb.getString("error");
    private static String gettingFirstLevelDivisionFailed = rb.getString("gettingFLDFailed");

    /**
     * Constructor for First level divisions to be used throughout the program.
     * @param divisionID id for the state/division
     * @param division name of the state/division
     * @param createDate date of creation for state/division
     * @param createdBy who created the division
     * @param lastUpdate when the last updated to the division was
     * @param lastUpdatedBy who last updated division
     * @param countryID country id the state is associated with
     */
    public FirstLevelDivision (int divisionID, String division, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryID){
        this.divisionID = divisionID;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    /**
     * Returns the first level division ID after a user selects a first level division from a filtered combo
     * box in the add and modify customer controllers.
     * @return state id is returned as an int
     */
    public int getDivisionID() {
        return divisionID;
    }
    /**
     * Returns the Country ID after a user selects a Country from a combo
     * box in the add and modify customer controllers. The Country ID returned
     * is then used to filter the first level division list.
     * @return country id is returned as an int
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     *This fills the first level division list will all the divisions from the database.
     * This list is later filtered with lambda expressions.
     */
    public static void fillDivisionList() {
        try {
            String selectCountries = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(selectCountries);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                LocalDate date = rs.getDate("Create_Date").toLocalDate();
                LocalTime time = rs.getTime("Create_Date").toLocalTime();
                LocalDateTime createDate = LocalDateTime.of(date, time);
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("COUNTRY_ID");
                FirstLevelDivision addedFirstLevelDivision = new FirstLevelDivision(divisionID, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryID);
                divisionList.add(addedFirstLevelDivision);
            }
        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText(er);
            informationAlert.setContentText(gettingFirstLevelDivisionFailed);
            informationAlert.showAndWait();
        }
    }
    /**This makes sure a newly added appointment can not have the
     * same start or end time as another appointment for the customer.
     * @return gets division name
     * */
    @Override
    public String toString(){
        return division;
    }

}
