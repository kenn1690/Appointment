package Main;

import Classes.*;
import Utils.DBConnection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

/**
 * This is the main class. It mostly fills array lists.
 * It also makes the initial connection call to the database
 * and the closing call.
 */
public class Main extends Application {
    /**
     * Array list to hold hours.
     */
    public static ObservableList<String> hours = FXCollections.observableArrayList();
    /**
     * Array list to hold minutes.
     */
    public static ObservableList<String> minutes = FXCollections.observableArrayList();
    /**
     * Array list to hold months.
     */
    public static ObservableList<String> months = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Views/LoginView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
    /**
     * Fills the hours and minutes array lists.
     */
    public static void fillHoursAndMinutes(){
        hours.add("08");
        hours.add("09");
        hours.add("10");
        hours.add("11");
        hours.add("12");
        hours.add("13");
        hours.add("14");
        hours.add("15");
        hours.add("16");
        hours.add("17");
        hours.add("18");
        hours.add("19");
        hours.add("20");
        hours.add("21");
        hours.add("22");

        minutes.add("00");
        minutes.add("15");
        minutes.add("30");
        minutes.add("45");
    }
    /**
     * Fills the months list.
     */
    public static void fillMonthList(){
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");


    }
    /**
     * This static void method fills the lists that will only change
     * by a filter/lambda expression. This also starts the connection
     * and closes the connection.
     * @param args needed to run main
     */
    public static void main(String[] args) {
        Connection connection = DBConnection.startConnection(); //Database Connection
        Country.fillCountryList();
        FirstLevelDivision.fillDivisionList();
        ContactsDB.fillContactsIDList();
        UserDB.fillUserList();
        UserDB.fillUserIDList();
        fillHoursAndMinutes();
        fillMonthList();
        launch(args);
        DBConnection.closeConnection();
    }
}
