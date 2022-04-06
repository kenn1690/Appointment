package Utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static Utils.Alerts.informationAlert;

/**
 * This class supplies all the information to connect
 * to the wgu database.
 */
public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ06eJo?connectionTimeZone=SERVER";
    // JDBC URL
    private static final String jdbc = protocol + vendorName + ipAddress;
    //Driver and connection interface reference
    private static String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection connector = null;
    private static String userName = "U06eJo";
    private static final String password = "53688743719";

    /**
     * This class returns a connector to be able to connect
     * to the database. If the connection fails, an appropriate
     * error is returned.
     * @return a connector to the database
     */
    public static Connection startConnection() {
        try{
            Class.forName(MYSQLJDBCDriver);
            connector = (Connection) DriverManager.getConnection(jdbc, userName, password);
            System.out.println("Connection successful");
        }
        catch(ClassNotFoundException | SQLException e){
           System.out.println(e.getMessage());
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Unable to connect to Database. \nPlease confirm your connection and try again.");
            informationAlert.showAndWait();
        }
        return connector;
    }

    /**
     * This class closes the connector to the DB.
     */
    public static void closeConnection(){
        try{
            connector.close();
            System.out.println("Connection closed.");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            informationAlert.setHeaderText("SQL Error");
            informationAlert.setContentText("Unable to connect to Database. \nPlease confirm your connection and try again.");
            informationAlert.showAndWait();
        }
    }

    /**
     * This returns the connection to the database and is called
     * anytime an SQL statement needs to be run.
     * @return gets the connector to the database for use
     */
    public static Connection getConnector(){
        return connector;
    }
}

