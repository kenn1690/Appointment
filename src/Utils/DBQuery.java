package Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static Utils.Alerts.informationAlert;
import static Utils.DBConnection.startConnection;

/**
 * This class helps manage the query that is being made.
 * It makes for less code where the query is being called.
 * It also has the ability to return a result set
 */
public class DBQuery {

    private static String dbQuery;
    private static PreparedStatement psStatement;
    private static ResultSet rs;

    public static void makeQuery(String query){
        dbQuery = query;
        try{
            psStatement = startConnection().prepareStatement(query);
            // determine query execution
            if(dbQuery.contains("SELECT")) {
                rs = psStatement.executeQuery();
            }
            if(dbQuery.contains("DELETE")||dbQuery.contains("INSERT")||dbQuery.contains("UPDATE")) {
                psStatement.executeUpdate();
            }

        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Unable to connect to Database. \nPlease confirm your connection and try again.");
            informationAlert.showAndWait();
        }
    }

    /**
     * returns the result set
     * @return gets the result set
     */
    public static ResultSet getResult(){
        return rs;
    }

}
