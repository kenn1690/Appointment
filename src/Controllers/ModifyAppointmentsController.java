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

public class ModifyAppointmentsController implements Initializable {
    @FXML
    private Button modifyBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label appointmentIDLbl;
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
    private ComboBox modifyStartHourCombo;
    @FXML
    private ComboBox modifyStartMinuteCombo;
    @FXML
    private ComboBox modifyEndHourCombo;
    @FXML
    private ComboBox modifyEndMinuteCombo;
    @FXML
    private ComboBox modifyCustomerIDCombo;
    @FXML
    private ComboBox modifyUserIDCombo;
    @FXML
    private ComboBox modifyContactCombo;
    @FXML
    private DatePicker modifyStartDtPckr;
    @FXML
    private TextField modifyAppointmentIDTxtFld;
    @FXML
    private TextField modifyTitleTxtFld;
    @FXML
    private TextField modifyDescriptionTxtFld;
    @FXML
    private TextField modifyLocationTxtFld;
    @FXML
    private TextField modifyTypeTxtFld;
    private static String startHour;
    private static String endHour;
    /**
     * This class controls the Modify Appointment view. There is a save and
     * return to home screen buttons actions. Also, there are some
     * on click functions to help filter combo box list data
     */

    /**
     * This method receives the selected appointment information
     * from the home view controller. After the user has an appointment
     * highlighted on the home view, they can then select modify on the
     * home view to populate all the appointment information.
     * @param selectedAppointment selected appointment from the home view
     */
    public void receiveAppointmentInfo(Appointment selectedAppointment) {
            int cID = selectedAppointment.getContactID();
            Contacts contact = ContactsDB.getContactBasedOnID(cID);
            modifyAppointmentIDTxtFld.setText(Integer.toString(selectedAppointment.getAppointmentID()));
            modifyTitleTxtFld.setText(selectedAppointment.getTitle());
            modifyDescriptionTxtFld.setText(selectedAppointment.getDescription());
            modifyLocationTxtFld.setText(selectedAppointment.getLocation());
            modifyContactCombo.setValue(contact);
            modifyTypeTxtFld.setText(selectedAppointment.getType());
            modifyStartDtPckr.setValue(selectedAppointment.getStart().toLocalDateTime().toLocalDate());
            modifyStartHourCombo.setValue(String.format("%02d", selectedAppointment.getStart().toLocalDateTime().toLocalTime().getHour()));
            startHour = String.format("%02d", selectedAppointment.getStart().toLocalDateTime().toLocalTime().getHour());
            modifyStartMinuteCombo.setValue(String.format("%02d", selectedAppointment.getStart().toLocalDateTime().toLocalTime().getMinute()));
            modifyEndHourCombo.setValue(String.format("%02d", selectedAppointment.getEnd().toLocalDateTime().toLocalTime().getHour()));
            endHour = String.format("%02d", selectedAppointment.getEnd().toLocalDateTime().toLocalTime().getHour());
            modifyEndMinuteCombo.setValue(String.format("%02d", selectedAppointment.getEnd().toLocalDateTime().toLocalTime().getMinute()));
            modifyCustomerIDCombo.setValue(selectedAppointment.getCustomerID());
            modifyUserIDCombo.setValue(selectedAppointment.getUserID());
            AppointmentDB.getCustomersAppointmentList((Integer) modifyCustomerIDCombo.getValue());

    }
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
        appointmentIDLbl.setText(rb.getString("appointmentID"));
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
        modifyBtn.setText(rb.getString("add"));
        cancelBtn.setText(rb.getString("cancel"));
        modifyStartHourCombo.setItems(hours);
        //modifyStartMinuteCombo.setItems(minutes);
        modifyEndHourCombo.setItems(hours);
        //modifyEndMinuteCombo.setItems(minutes);
        modifyContactCombo.setItems(ContactsDB.getContactsID());
        modifyUserIDCombo.setItems(UserDB.getUserIDList());
        modifyCustomerIDCombo.setItems(CustomerDB.getEveryCustomerID());

    }
    /**
     * This action event attempts to save the input information
     * to the database as the modified appointment.The if statements
     * checks the input is empty and flips a boolean to false.
     * The local date time is made from a combination of a
     * date picker and combo boxes with military time. Before being
     * written to the database, it performs a check for overlapping
     * appointments for a particular customer. So different customers
     * may have overlapping timed appointments. With this overlapping
     * appointment check, it allows the appointment to have the same
     * start and end time without flipping the overlapping appointment
     * boolean to false.
     * @param actionEvent on button click
     */
    public void onActionModifyAppointment(ActionEvent actionEvent) {
        try{
            boolean isValidAppointment = true;
            String insert = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(insert);
            int appointmentID = Integer.parseInt(modifyAppointmentIDTxtFld.getText());
            String title = modifyTitleTxtFld.getText();
            if(modifyTitleTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            String description = modifyDescriptionTxtFld.getText();
            if(modifyDescriptionTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            String location = modifyLocationTxtFld.getText();
            if(modifyLocationTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            String type = modifyTypeTxtFld.getText();
            if(modifyTypeTxtFld.getText().isEmpty()){
                isValidAppointment = false;
            }
            LocalDate start = modifyStartDtPckr.getValue();
            LocalTime startTime = LocalTime.parse(modifyStartHourCombo.getValue().toString() + ":" + modifyStartMinuteCombo.getValue().toString());
            LocalDateTime startLDT = LocalDateTime.of(start, startTime);
            LocalDate end = modifyStartDtPckr.getValue();
            LocalTime endTime = LocalTime.parse(modifyEndHourCombo.getValue().toString() + ":" + modifyEndMinuteCombo.getValue().toString());
            LocalDateTime endLDT = LocalDateTime.of(end, endTime);
            Contacts contact = (Contacts) modifyContactCombo.getValue();
            String conID = String.valueOf(contact.getContactID());
            int customerID = (int) modifyCustomerIDCombo.getValue();
            int userID = (int) modifyUserIDCombo.getValue();
            int contactID = Integer.parseInt(conID);


            boolean overlappingAppointments = checkForOverlappingAppointments(startLDT, endLDT, appointmentID);

            if(!overlappingAppointments){
                System.out.println("Appointments are overlapping");
            }
            else if(!isValidAppointment){
                informationAlert.setHeaderText("Null Value Error");
                informationAlert.setContentText("All fields must have a value \nPlease confirm your entries and try again.");
                informationAlert.showAndWait();
            }
            else {
                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, Timestamp.valueOf(startLDT));
                ps.setTimestamp(6, Timestamp.valueOf(endLDT));
                ps.setInt(7, customerID);
                ps.setInt(8, userID);
                ps.setInt(9, contactID);
                ps.setInt(10, appointmentID);
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
            informationAlert.setContentText("Unable to bring up view, please try again.");
            informationAlert.showAndWait();
        }
        catch(NullPointerException ne){
            System.out.println("NullPoint Exception thrown: " + ne);
            informationAlert.setHeaderText("Null Value Error");
            informationAlert.setContentText("All fields must have a value \nPlease confirm your entries and try again.");
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
            informationAlert.setContentText("Unable to bring up view, please try again.");
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
        modifyStartMinuteCombo.setItems(minutes);
    }
    /**
     * When the user chooses an hour from the end hour combo box
     * this method will set the end time minute combo box
     * with the appropriate values. This also ensures no appointments
     * can be made after 11pm
     * @param actionEvent on combo box click
     */
    public void onActionSetEndHourToChangeMinutes(ActionEvent actionEvent) {
        if(modifyEndHourCombo.getValue().equals("22")){
            modifyEndMinuteCombo.setValue("00");
        }
        else{
            modifyEndMinuteCombo.setItems(minutes);
        }
    }
    /**
     * When the user clicks on the start hour combo box; without having
     * to select any items from the combo box, it will filter
     * out the 11pm hour.
     * <p><b>
     * This lambda is used to help confine the user
     * to putting items in with inside business hours.
     * </b></p>
     * @param mouseEvent This mouse event starts when the user clicks to bring up the combo box list items.
     * @throws IOException in case the combo box is not clicked
     *
     */
    public void onClickRemoveHour(MouseEvent mouseEvent) throws IOException{
        ObservableList<String> removeHour = hours.filtered(f ->{
            if(!(f.equals("22"))){
                return true;
            }
            return false;
        });
        modifyStartHourCombo.setItems(removeHour);
    }
    /**
     * This action event is after the user selects a customer
     * from the combo box. After the selection is made, it passes
     * it into the get customer appointment method which makes an
     * SQL query to find all the associated appointments for the
     * selected customer id.
     * @param actionEvent on combo box click
     */
    public void onActionGetCustomerAppointments(ActionEvent actionEvent) {
        try {
            AppointmentDB.getCustomersAppointmentList((Integer) modifyCustomerIDCombo.getValue());
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
        catch(RuntimeException e){
            System.out.println(e);
        }
    }
}
