
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.DBLink;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class ByCustomerController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label numberOfCustomers;

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String txt = "Total number of customers: ";
        numberOfCustomers.setText(txt + (Integer.toString(customerCount())));
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    //Query total number of customers from database
    private int customerCount() {
        int count = 0;
        String pCCSQL = "SELECT COUNT(customerId) FROM customer;";

        try {

            //**PreparedStatement creation
            PreparedStatement pTypeStatement = DBLink.startConnection().prepareStatement(pCCSQL);
            ResultSet rs = pTypeStatement.executeQuery();
            rs.last();
            count = rs.getInt("COUNT(customerId)");
            System.out.println("DBCustomer says: COUNT is " + count);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }
}
