package Controllers;

import Classes.*;
import Utils.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import static Classes.AppointmentDB.*;
import static Main.Main.hours;
import static Main.Main.minutes;
import static Utils.Alerts.informationAlert;

/**
 * This class controls the Add Appointment view. There is a save and
 * return to home screen buttons actions. Also, there are some
 * on click functions to help filter combo box list data
 */
public class AddAppointmentsController implements Initializable {
    @FXML
    private Button addBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label titleLbl;
    @FXML
    private Label descriptionLbl;
    @FXML
    private Label locationLbl;
    @FXML
    private Label contactLbl;
    @FXML
    private Label typeLbl;
    @FXML
    private Label startDtLbl;
    @FXML
    private Label customerIDLbl;
    @FXML
    private Label userIDLbl;
    @FXML
    private Label startTimeLbl;
    @FXML
    private Label endTimeLbl;
    @FXML
    private ComboBox endTimeHoursCombo;
    @FXML
    private ComboBox endTimeMinutesCombo;
    @FXML
    private ComboBox startTimeHoursCombo;
    @FXML
    private ComboBox startTimeMinutesCombo;
    @FXML
    private TextField titleTxtFld;
    @FXML
    private TextField descriptionTxtFld;
    @FXML
    private TextField locationTxtFld;
    @FXML
    private TextField typeTxtFld;
    @FXML
    private DatePicker startDateDtPckr;
    @FXML
    private ComboBox contactCombo;
    @FXML
    private ComboBox customerIDCombo;
    @FXML
    private ComboBox userIDCombo;

    /**
     * This void method helps set all the label text on initialization.
     * The label text is pulled from the language resource bundle.
     * Also, it fills certain combo boxes.
     * @param url unified resource locator
     * @param rb resource bundler to get resources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        titleLbl.setText(rb.getString("title"));
        descriptionLbl.setText(rb.getString("description"));
        locationLbl.setText(rb.getString("location"));
        contactLbl.setText(rb.getString("contact"));
        typeLbl.setText(rb.getString("type"));
        startDtLbl.setText(rb.getString("start"));
        startTimeLbl.setText(rb.getString("sTime"));
        endTimeLbl.setText(rb.getString("eTime"));
        customerIDLbl.setText(rb.getString("customerID"));
        userIDLbl.setText(rb.getString("userID"));
        addBtn.setText(rb.getString("add"));
        cancelBtn.setText(rb.getString("cancel"));

        startTimeHoursCombo.setItems(hours);
        //startTimeMinutesCombo.setItems(minutes);
        endTimeHoursCombo.setItems(hours);
        //endTimeMinutesCombo.setItems(minutes);
        contactCombo.setItems(ContactsDB.getContactsID());
        userIDCombo.setItems(UserDB.getUserIDList());
        customerIDCombo.setItems(CustomerDB.getEveryCustomerID());
    }

    /**
     * This action event attempts to save the input information
     * to the database as a new appointment.The if statements
     * checks the input is empty and flips a boolean to false.
     * The local date time is made from a combination of a
     * date picker and combo boxes with military time. Before being
     * written to the database, it performs a check for overlapping
     * appointments for a particular customer. So different customers
     * may have overlapping timed appointments.
     * @param actionEvent on button click
     */
    public void onActionSaveAppointment(ActionEvent actionEvent) {
        boolean isValidAppointment = true;
        try{

            int appointmentID = -1; //sets a generic appointment id to pass into the overlapping business hours check
            String insert = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" +
                    "Values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(insert);
            String title = titleTxtFld.getText();
            if(titleTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            String description = descriptionTxtFld.getText();
            if(descriptionTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            String location = locationTxtFld.getText();
            if(locationTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            String type = typeTxtFld.getText();
            if(typeTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            LocalDate start = startDateDtPckr.getValue();
            LocalTime startTime = LocalTime.parse(startTimeHoursCombo.getValue().toString() + ":" + startTimeMinutesCombo.getValue().toString());
            LocalDateTime startLDT = LocalDateTime.of(start, startTime);
            LocalDate end = startDateDtPckr.getValue();
            LocalTime endTime = LocalTime.parse(endTimeHoursCombo.getValue().toString() + ":" + endTimeMinutesCombo.getValue().toString());
            LocalDateTime endLDT = LocalDateTime.of(end, endTime);
            Contacts contact = (Contacts) contactCombo.getValue();
            String conID = String.valueOf(contact.getContactID());
            int customerID = (int) customerIDCombo.getValue();
            int userID = (int) userIDCombo.getValue();
            int contactID = Integer.parseInt(conID);
            boolean overlappingAppointments = checkForOverlappingAppointments(startLDT, endLDT, appointmentID);
            if (!overlappingAppointments) {
                System.out.println("Overlapping business hours");
            }
            else if(!isValidAppointment){
                informationAlert.setHeaderText("Null Value Error");
                informationAlert.setContentText("All fields must have a value \nPlease confirm your entries and try again.");
                informationAlert.showAndWait();
            }
            else{

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, Timestamp.valueOf(startLDT));
                ps.setTimestamp(6, Timestamp.valueOf(endLDT));
                ps.setInt(7, customerID);
                ps.setInt(8, userID);
                ps.setInt(9, contactID);
                ps.execute();

                Parent root = FXMLLoader.load(getClass().getResource("/Views/HomeView.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 507);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
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
            informationAlert.setContentText("Unable to write values to database. \nPlease confirm your entries and try again.");
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
    public void onActionGoHome(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/Views/HomeView.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 507);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            System.out.println(e);
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Unable to write values to database. \nPlease confirm your entries and try again.");
            informationAlert.showAndWait();
        }
    }

    /**
     * When the user chooses an hour from the star hour combo box
     * this method will set the start time minute combo box
     * with the appropriate values
     * @param actionEvent on combo box click
     */
    public void onActionSetHourToChangeMinutes(ActionEvent actionEvent) {
        startTimeMinutesCombo.setItems(minutes);
    }

    /**
     * When the user chooses an hour from the end hour combo box
     * this method will set the end time minute combo box
     * with the appropriate values. This also ensures no appointments
     * can be made after 11pm.
     * @param actionEvent on combo box click
     */
    public void onActionSetEndHourToChangeMinutes(ActionEvent actionEvent) {
        if(endTimeHoursCombo.getValue().equals("22")){
            endTimeMinutesCombo.setValue("00");
        }
        else{
            endTimeMinutesCombo.setItems(minutes);
        }
    }

    /**
     * When the user clicks on the start hour combo box; without having
     * to select any items from the combo box, it will filter
     * out the 11pm hour.
     * <p>
     *  <b>
     * This lambda is used to help confine the user
     * to putting items in with inside business hours.
     * </b>
     * </p>
     * @param mouseEvent This mouse event starts when the user clicks to bring up the combo box list items.
     * @throws IOException throws in case mouse event does not happen
     *
     */
    public void onClickRemoveHour(MouseEvent mouseEvent) throws IOException{

        ObservableList<String> removeHour = hours.filtered(f ->{
            if(!(f.equals("22"))){
                return true;
            }
            return false;
        });
        startTimeHoursCombo.setItems(removeHour);
    }
    /**
     * This action event is after the user selects a customer
     * from the combo box. After the selection is made, it passes
     * it into the get customer appointment method which makes an
     * SQL query to find all the associated appointments for the
     * selected customer id.
     * @param actionEvent on combo box click
     */
    public void onActionGetCustomerAppointmentList(ActionEvent actionEvent) {
        try {
            AppointmentDB.getCustomersAppointmentList((Integer) customerIDCombo.getValue());
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
        catch(RuntimeException e){
            System.out.println(e);
        }
    }
}
