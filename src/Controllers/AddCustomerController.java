package Controllers;

import Classes.Country;
import Classes.FirstLevelDivision;
import Classes.User;
import Utils.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static Classes.FirstLevelDivision.divisionList;
import static Utils.Alerts.informationAlert;

/**
 * This class controls the Add Customers view. There is a save and
 * return to home screen buttons actions. Also, there are some
 * on click functions to help filter combo box list data
 */
public class AddCustomerController implements Initializable {
    @FXML
    private Label nameLbl;
    @FXML
    private Label addressLbl;
    @FXML
    private Label postalCodeLbl;
    @FXML
    private Label phoneLbl;
    @FXML
    private Label countryLbl;
    @FXML
    private Label stateLbl;
    @FXML
    private Button addBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField nameTxtFld;
    @FXML
    private TextField addresTxtFld;
    @FXML
    private TextField postalCodeTxtFld;
    @FXML
    private TextField phoneTxtFld;
    @FXML
    private ComboBox countryCombo;
    @FXML
    private ComboBox stateCombo;
    /**
     * This void method helps set all the label text on initialization.
     * The label text is pulled from the language resource bundle.
     * Also, it fills certain combo boxes
     * @param url unified resource locator
     * @param rb resource bundler to get resources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        nameLbl.setText(rb.getString("name"));
        addressLbl.setText(rb.getString("address"));
        postalCodeLbl.setText(rb.getString("postalCode"));
        phoneLbl.setText(rb.getString("phone"));
        countryLbl.setText(rb.getString("country"));
        stateLbl.setText(rb.getString("state"));
        addBtn.setText(rb.getString("add"));
        cancelBtn.setText(rb.getString("cancel"));
        countryCombo.setItems(Country.getCountryList());
    }

    /**
     * This action event attempts to save the input information
     * to the database as a new Customer. The if statements
     * checks to see if the input in the text field is empty
     * and flips a boolean to false if it is.
     * The local date time is formatted correctly to be written into the
     * database. Other information is pulled from combo boxes and the user id
     * is pulled from the current logged-in user.
     * @param actionEvent on button click
     */
    public void onActionAddCustomer(ActionEvent actionEvent) {
        try{
            boolean isValidCustomer = true;
            String insert = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(insert);
            String name = nameTxtFld.getText();
            if(nameTxtFld.getText().isEmpty()){
                isValidCustomer = false;
            }
            String address = addresTxtFld.getText();
            if(addresTxtFld.getText().isEmpty()){
                isValidCustomer = false;
            }
            String postalCode = postalCodeTxtFld.getText();
            if(postalCodeTxtFld.getText().isEmpty()){
                isValidCustomer = false;
            }
            String phone = phoneTxtFld.getText();
            if(phoneTxtFld.getText().isEmpty()){
                isValidCustomer = false;
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String today = dtf.format(now);
            FirstLevelDivision fld = (FirstLevelDivision) stateCombo.getValue();
            int divisionID = fld.getDivisionID();
            String createdBy = User.getUserName();
            if(isValidCustomer) {
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, postalCode);
                ps.setString(4, phone);
                ps.setString(5, today);
                ps.setString(6, createdBy);
                ps.setString(7, today);
                ps.setString(8, createdBy);
                ps.setInt(9, divisionID);
                ps.execute();

                Parent root = FXMLLoader.load(getClass().getResource("/Views/CustomerView.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 799, 499);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            }
            else{
                informationAlert.setHeaderText("Null Value Error");
                informationAlert.setContentText("All fields must have a value \nPlease confirm your entries and try again.");
                informationAlert.showAndWait();
            }
        }
        catch(SQLException se){
            System.out.println("SQL exception Exception thrown: " + se);
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Unable to write values to database. \nPlease confirm your entries and try again.");
            informationAlert.showAndWait();
        }
        catch(IOException e){
            System.out.println("IO Exception thrown: " + e);
            informationAlert.setHeaderText("Input or Output Error");
            informationAlert.setContentText("Unable to bring up view, please try again.");
            informationAlert.showAndWait();
        }
        catch(NullPointerException ne){
            System.out.println("NullPoint Exception thrown: " + ne);
            informationAlert.setHeaderText("Null Value Error");
            informationAlert.setContentText("All fields must have a value \nPlease confirm your entries and try again.");
            informationAlert.showAndWait();
        }
        catch(RuntimeException re){
            System.out.println("RunTime Exception thrown: " + re);
            informationAlert.setHeaderText("Runtime Error");
            informationAlert.setContentText("There is a problem loading the page \nPlease click Ok and try again.");
            informationAlert.showAndWait();
        }
    }
    /**
     * This action event takes the user back to the home screen.
     * @param actionEvent on button click
     */
    public void onActionGoToCustomerView(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/Views/CustomerView.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 799, 499);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            System.out.println(e);
            informationAlert.setHeaderText("Input or Output Error");
            informationAlert.setContentText("Unable to bring up view, please try again.");
            informationAlert.showAndWait();
        }
    }

    /**
     * This on action events helps filter all the correct
     * first level division information into the state combo box.
     * <p>
     * <b>
     * After selecting a country, it takes it and passes it to a
     * lambda expression that will filter the first level division
     * data based on country ID. This lambda helps meet business
     * requirements and ensures that the user will only put in
     * a state that belongs to that country.
     * </b>
     * </p>
     * @param actionEvent on combo box click
     */
    public void onActionChangeCountry(ActionEvent actionEvent) {
        Country country = (Country) countryCombo.getValue();
        System.out.println(countryCombo.getValue() + " " + country.getCountryID());

        //System.out.println(getDivisionList());
        ObservableList<FirstLevelDivision> filteredFLD = divisionList.filtered(f -> {
            if (f.getCountryID() == country.getCountryID()) {
                //System.out.println(f.getCountryID());
                return true;
            }
            return false;
        });
        stateCombo.setItems(filteredFLD);
        System.out.println(countryCombo.getValue());
    }

}
