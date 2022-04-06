package Controllers;

import Classes.User;
import Classes.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import static Utils.Alerts.informationAlert;

/**
 * This class controls the login view. The user
 * can either enter credentials and sign in or
 * exit the application. It also displays the
 * system default time.
 */
public class LoginController implements Initializable {
    @FXML
    private Label zoneIDTextLbl;
    @FXML
    private Label zoneIDLbl;
    @FXML
    private Label userNameLbl;
    @FXML
    private Label passwordLbl;
    @FXML
    private Button submitBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private TextField userNameTxt;
    @FXML
    private PasswordField passwordTxt;
    /**
     * This void method helps set all the label text on initialization.
     * The label text is pulled from the language resource bundle.
     * @param url unified resource locator
     * @param rb resource bundler to get resources
     **/
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        userNameLbl.setText(rb.getString("username"));
        passwordLbl.setText(rb.getString("password"));
        submitBtn.setText(rb.getString("submit"));
        exitBtn.setText(rb.getString("exit"));
        zoneIDLbl.setText(UserDB.showZoneID());
        zoneIDTextLbl.setText(rb.getString("zoneID"));
    }

    /**
     * This button will log the user in if the credentials
     * match from the database.
     * @param actionEvent on button click
     */
    @FXML
    public void onActionSubmitToLogin(ActionEvent actionEvent) {
        ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
        try{
            String userName = userNameTxt.getText();
            String password = passwordTxt.getText();

            boolean validLogin = UserDB.loginCheck(userName, password);
            if(validLogin) {
                UserDB.getAppointmentAtLogin();
                //currentUser.setUserName(userName);
                Parent root = FXMLLoader.load(getClass().getResource("/Views/HomeView.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 507);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            }
            else{
                System.out.println("Login failed");
                informationAlert.setHeaderText(rb.getString("loginFailedTitle"));
                informationAlert.setContentText(rb.getString("loginFailedText"));
                informationAlert.showAndWait();
            }
        }
        catch(IOException e){
            System.out.println(e);
            informationAlert.setHeaderText("Input or Output Error");
            informationAlert.setContentText("Unable to bring up view, please try again.");
            informationAlert.showAndWait();
        }
    }

    /**
     * Exits the program
     * @param actionEvent on button click
     */
    @FXML
    public void onActionCloseProgram(ActionEvent actionEvent) {
        System.exit(0);
    }
}
