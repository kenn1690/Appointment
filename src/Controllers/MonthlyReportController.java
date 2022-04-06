package Controllers;

import Classes.AppointmentDB;
import Utils.DBConnection;
import Utils.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static Classes.AppointmentDB.appointmentTypeList;
import static Main.Main.months;
import static Utils.Alerts.informationAlert;

/**
 * This class controls the monthly report screen. It only
 * initializes with the months list loaded and makes the user
 * select a month first.
 */
public class MonthlyReportController implements Initializable {
    @FXML
    private ComboBox monthCombo;
    @FXML
    private ComboBox typeCombo;
    @FXML
    private Label totalNumAppsLbl;

    /**
     * This method fills the month combo box.
     * @param url unified resource locator
     * @param rb resource bundler to get resources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        monthCombo.setItems(months);
    }

    /**
     * This method takes the user back to the home screen view.
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
     * After choosing a month, the type combo box
     * gets filled with distinct types from the appointments.
     * @param actionEvent on combo box click
     */
    public void onActionChooseMonth(ActionEvent actionEvent) {
        typeCombo.setItems(AppointmentDB.getAppointmentType());
    }

    /**
     * When a user selects a type, it will go through and
     * calculate the total number of appointments with
     * that type and that month. It will then display
     * the total in the view.
     * @param actionEvent on combo box click
     */
    public void onActionChooseType(ActionEvent actionEvent) {
        int count = 0;
        try {
            String type = (String) typeCombo.getValue();
            String month = (String) monthCombo.getValue();

            String getCount = "SELECT COUNT(*) FROM appointments WHERE MONTHNAME(start) =  ? AND TYPE = ?";
            PreparedStatement ps = DBConnection.getConnector().prepareStatement(getCount);
            ps.setString(1, month);
            ps.setString(2, type);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("COUNT(*)");
            }

        }
        catch(SQLException se){
            System.out.println("SQL error: " + se);
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Was not able to pull from Appointments table");
            informationAlert.showAndWait();
        }
        totalNumAppsLbl.setText(String.valueOf(count));
    }
}
