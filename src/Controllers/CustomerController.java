package Controllers;

import Classes.Customer;
import Classes.CustomerDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static Utils.Alerts.choiceAlert;
import static Utils.Alerts.informationAlert;

/**
 * This class controls the customer view. It displays
 * the customer data and allows the user to access the
 * add, modify, delete customer options
 */
public class CustomerController implements Initializable {
    @FXML
    private Button toHomeViewBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button modifyBtn;
    @FXML
    private TableColumn addressCol;
    @FXML
    private TableColumn postalCodeCol;
    @FXML
    private TableColumn divisionIDCol;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn phoneCol;
    @FXML
    private TableView customerTableView;

    /**
     * This void method helps set all the label text on initialization.
     * The label text is pulled from the language resource bundle.
     * Also, it fills certain combo boxes. Lastly, it fills the customer
     * table with some customer information.
     * @param url unified resource locator
     * @param rb resource bundler to get resources
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        cancelBtn.setText(rb.getString("cancel"));
        addBtn.setText(rb.getString("add"));
        modifyBtn.setText((rb.getString("modify")));
        deleteBtn.setText(rb.getString("delete"));
        toHomeViewBtn.setText(rb.getString("toHomeView"));
        customerTableView.setItems(CustomerDB.getEveryCustomer());
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

    }
    /**
     * This action event takes the user to the add customer screen.
     * @param actionEvent on button click
     */
    public void onActionAddCustomer(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/Views/AddCustomerView.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 799, 499);
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            System.out.println(e);
            System.out.println("IO Exception thrown: " + e);
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
     * This action event takes the user to the modify customer screen.
     * @param actionEvent on button click
     */
    public void onActionModifyCustomer(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/ModifyCustomerView.fxml"));
            loader.load();
            ModifyCustomerController mcc = loader.getController();
            mcc.receiveCustomerInfo((Customer) customerTableView.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Modify Product Screen");
            stage.setScene(new Scene(scene));
        }
        catch(IOException e){
            System.out.println(e);
            System.out.println("IO Exception thrown: " + e);
            informationAlert.setHeaderText("Input or Output Error");
            informationAlert.setContentText("Unable to bring up view, please try again.");
            informationAlert.showAndWait();
        }
        catch(NullPointerException e){
            System.out.println("Must have a selection made");
            informationAlert.setHeaderText("Null Pointer Error");
            informationAlert.setContentText("You must make select the customer you want to modify");
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
     * This allows the user to delete the selected customer.
     * Before deleting it asks the user if they really want
     * to delete the customer and presents them an option.
     * The customer won't be deleted until the "Ok" option is made.
     * There is a check from another method in here to confirm the
     * Customer has no appointments associated with them. If it does
     * the other method shows an error message.
     * @param actionEvent on button click
     */
    public void onActionDeleteCustomer(ActionEvent actionEvent) {
        try {
            choiceAlert.setHeaderText("Confirm Delete");
            choiceAlert.setContentText("Are you sure you want to delete this Customer?");
            Optional<ButtonType> result = choiceAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Customer customerToDelete = (Customer) customerTableView.getSelectionModel().getSelectedItem();
                CustomerDB.deleteCustomer(customerToDelete);
                customerTableView.setItems(CustomerDB.getEveryCustomer());
            }
            else if(result.get() == ButtonType.CANCEL){
                informationAlert.setContentText("Did not perform delete");
                informationAlert.showAndWait();
            }

        }
        catch(NullPointerException e){
            System.out.println("Must have a selection made");
            informationAlert.setHeaderText("Null Pointer Error");
            informationAlert.setContentText("You must make a selection. No item deleted.");
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
    public void onActionToHome(ActionEvent actionEvent) {
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
}
