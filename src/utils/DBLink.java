
package utils;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Daniel LaForce
 */
public class DBLink {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String venderName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/U06KoO";

    //JDBC URL
    private static final String jdbcURL = protocol + venderName + ipAddress;

    //Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    //Database credentials
    private static final String userName = "U06KoO"; //UserName
    private static String password = "53688789478";

    //Method to open database connection
    public static Connection startConnection() {

        try {

            if (conn != null && !conn.isClosed()) {
                return conn;
            }

            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return conn;

    }

    //Method to close database connection
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection Closed");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
