package Controllers;

import Classes.Appointment;
import Classes.AppointmentDB;
import Classes.Contacts;
import Classes.ContactsDB;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import static Utils.Alerts.choiceAlert;
import static Utils.Alerts.informationAlert;

/**
 * This class controls the home view. It displays
 * the appointment data and allows the user to access the
 * add, modify, delete appointment options. Also, it has the
 * option to go to
 */
public class HomeController implements Initializable {
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn contactCol;
    @FXML
    private TableColumn typeCol;
    @FXML
    private TableColumn startDateTimeCol;
    @FXML
    private TableColumn endDateCol;
    @FXML
    private ComboBox contactIDHomeComboBox;
    @FXML
    private RadioButton allAppointmentsViewRdBtn;
    @FXML
    private RadioButton monthlyAppointmentsViewRdBtn;
    @FXML
    private RadioButton weeklyAppointmentsViewRdBtn;
    @FXML
    private RadioButton dailyAppointmentsViewRdBtn;
    @FXML
    private Label totalNumberTxtLbl;
    @FXML
    private Label totalNumberLbl;
    @FXML
    private Button addBtn;
    @FXML
    private Button modifyBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button toCustomerViewBtn;
    @FXML
    private TableColumn appointmentIDCol;
    @FXML
    private TableColumn titleCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private TableColumn customerIDCol;
    @FXML
    private TableView appointmentsTableView;
    /**
     * This void method helps set all the label text on initialization.
     * The label text is pulled from the language resource bundle.
     * Also, it fills certain combo boxes. Lastly, it fills the appointment
     * table with appointment information.
     * @param url unified resource locator
     * @param rb resource bundler to get resources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        deleteBtn.setText(rb.getString("delete"));
        addBtn.setText(rb.getString("add"));
        modifyBtn.setText((rb.getString("modify")));
        toCustomerViewBtn.setText(rb.getString("customerView"));
        appointmentsTableView.setItems(AppointmentDB.getEveryAppointmentList());
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        contactIDHomeComboBox.setItems(ContactsDB.getContactsID());
    }

    /**
     * This action event takes the user to the view where they
     * can see add, delete and update customer information.
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
        catch(RuntimeException re){
            System.out.println("RunTime Exception thrown: " + re);
            informationAlert.setHeaderText("Runtime Error");
            informationAlert.setContentText("There is a problem loading the page \nPlease click Ok and try again.");
            informationAlert.showAndWait();
        }
    }
    /**
     * This action event takes the user to the add Appointment screen.
     * @param actionEvent on button click
     */
    public void onActionAddAppointment(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/Views/AddAppointmentsView.fxml"));
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
        catch(RuntimeException re){
            System.out.println("RunTime Exception thrown: " + re);
            informationAlert.setHeaderText("Runtime Error");
            informationAlert.setContentText("There is a problem loading the page \nPlease click Ok and try again.");
            informationAlert.showAndWait();
        }
    }
    /**
     * This action event takes the user to the modify Appointment screen.
     * @param actionEvent on button click
     */
    public void onActionModifyAppointment(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/ModifyAppointmentsView.fxml"));
            loader.load();
            ModifyAppointmentsController mac = loader.getController();
            mac.receiveAppointmentInfo((Appointment) appointmentsTableView.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Modify Product Screen");
            stage.setScene(new Scene(scene, 799, 499));
        }
        catch(IOException e){
            System.out.println(e);
            informationAlert.setHeaderText("Input or Output Error");
            informationAlert.setContentText("Unable to bring up view, please try again.");
            informationAlert.showAndWait();
        }
        catch(NullPointerException e){
            System.out.println("Must have a selection made");
            informationAlert.setHeaderText("Null Pointer Error");
            informationAlert.setContentText("You must make select the appointment you want to modify");
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
     * This allows the user to delete the selected appointment.
     * Before deleting it asks the user if they really want
     * to delete the appointment and presents them an option.
     * The appointment won't be deleted until the "Ok" option is made.
     * @param actionEvent on button click
     */
    public void onActionDeleteAppointment(ActionEvent actionEvent) {
        try {

            Appointment appointmentToBeDeleted = (Appointment) appointmentsTableView.getSelectionModel().getSelectedItem();
            int id = appointmentToBeDeleted.getAppointmentID();
            String type = appointmentToBeDeleted.getType();
            String select = "DELETE FROM appointments WHERE Appointment_ID = " + id;
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(select);
            choiceAlert.setHeaderText("Confirm Delete");
            choiceAlert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = choiceAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                ps.execute();
                appointmentsTableView.setItems(AppointmentDB.getEveryAppointmentList());
                informationAlert.setContentText("Appointment ID: " + id + " type: " +  type + "\nhas been deleted.");
                informationAlert.showAndWait();
            }
            else if(result.get() == ButtonType.CANCEL){
                informationAlert.setContentText("Did not perform delete");
                informationAlert.showAndWait();
            }
        }
        catch(SQLException se){
            System.out.println("SQL exception Exception thrown: " + se);
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Unable to write values to database. \nPlease confirm your entries and try again.");
            informationAlert.showAndWait();
        }
        catch(NullPointerException e){
            System.out.println("Must have a selection made");
            informationAlert.setHeaderText("Null Pointer Error");
            informationAlert.setContentText("You must make select the appointment you want to delete");
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
     * When this radio button is selected, it shows all the
     * appointments ever created. It also changes label text
     * to no longer be visible
     * @param actionEvent on combo box click
     */
    public void onActionFilterAllAppointments(ActionEvent actionEvent) {
        appointmentsTableView.setItems(AppointmentDB.getEveryAppointmentList());
        totalNumberTxtLbl.setText("");
        totalNumberLbl.setText("");
    }
    /**
     * When this radio button is selected, it shows all the
     * appointments for the month the user is currently in.
     * <p>
     * <b>
     * A lambda is used to filter the all appointments list
     * to get any appointment in the current month.
     * </b>
     * </p>
     * There is a
     * counter that provides the total number of appointments
     * in the month and two labels that get values when this
     * radio button is selected
     *
     * @param actionEvent on combo box click
     */
    public void onActionFilterMonthAppointments(ActionEvent actionEvent) {
        ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        LocalDate ld = LocalDate.now();
        Month month = ld.getMonth();
        AtomicInteger count = new AtomicInteger();
        ObservableList<Appointment> monthFilteredList = AppointmentDB.getEveryAppointmentList().filtered(f ->{
            Timestamp appointmentDate = f.getStart();
            Month appointmentMonth = Month.of(appointmentDate.getMonth() + 1);
            if(appointmentMonth.equals(month)){
                count.getAndIncrement();
                return true;
            }
            return false;
        });
        totalNumberTxtLbl.setText(rb.getString("totalNumberTxt"));
        totalNumberLbl.setText(String.valueOf(count));
        System.out.println(count);
        appointmentsTableView.setItems(monthFilteredList);
    }
    /**
     * When this radio button is selected, it shows all the
     * appointments from now through the next 7 days.
     * <p><b>
     * A lambda is used to filter the all appointments list
     * to get any appointment currently past local date time now
     * plus the next 7 days.
     * </b></p>
     * Label values are moved to null.
     * @param actionEvent on radio button click
     *
     */
    public void onActionFilterWeekAppointments(ActionEvent actionEvent) {
        ObservableList<Appointment> weekFilteredList = AppointmentDB.getEveryAppointmentList().filtered(f ->{
            if(f.getStart().after(Timestamp.valueOf(LocalDateTime.now())) && f.getStart().before(Timestamp.valueOf(LocalDateTime.now().plusWeeks(1)))){
                return true;
            }
            return false;
        });
        totalNumberTxtLbl.setText("");
        totalNumberLbl.setText("");
        appointmentsTableView.setItems(weekFilteredList);
    }
    /**
     * When this radio button is selected, it shows all the
     * appointments from now through the end of day.
     * <p><b>
     * A lambda is used to filter the all appointments list
     * to get any appointment currently past local date time now
     * through the end of day.
     * </b></p>
     * Label values are moved to null.
     * @param actionEvent on radio button click
     *
     */
    public void onActionFilterDailyAppointments(ActionEvent actionEvent) {
        ObservableList<Appointment> dayFilteredList = AppointmentDB.getEveryAppointmentList().filtered(f ->{
            if(f.getStart().after(Timestamp.valueOf(LocalDateTime.now())) && f.getStart().before(Timestamp.valueOf(LocalDateTime.now().plusDays(1)))){
                System.out.println(f.getStart());
                return true;
            }
            return false;
        });
        totalNumberTxtLbl.setText("");
        totalNumberLbl.setText("");
        appointmentsTableView.setItems(dayFilteredList);
    }

    /**
     * <p><b>
     * When the user selects the contact ID out of the combo box,
     * the lambda expression with filter the list to only have
     * appointments who have that contact ID associated with it
     * </b></p>
     * @param actionEvent on radio button click
     *
     */
    public void onActionFilterAppointmentsByContact(ActionEvent actionEvent) {
        ObservableList<Appointment> contactFilteredList = AppointmentDB.getEveryAppointmentList().filtered(f -> {
            Contacts contact = (Contacts) contactIDHomeComboBox.getValue();
            String conID = String.valueOf(contact.getContactID());
            int customerID = Integer.parseInt(conID);
            if (f.getContactID() == customerID) {
                return true;
            }
            return false;
        });
        totalNumberTxtLbl.setText("");
        totalNumberLbl.setText("");
        appointmentsTableView.setItems(contactFilteredList);
    }

    public void onActionGenerateMonthlyReport(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/Views/MonthlyReportView.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 330, 450);
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
}

