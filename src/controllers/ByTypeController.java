
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utils.DBLink;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class ByTypeController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label typesOfAppointments;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(typeCount());
        String txt = "Different appointments types this month: ";
        typesOfAppointments.setText(txt + (Integer.toString(typeCount())));
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

    //Query number of different appointment types this month from database
    private int typeCount() {
        int count = 0;
        String pTSQL = "SELECT COUNT(distinct type) FROM appointment where MONTH(start)=MONTH(NOW()) and YEAR(start) = YEAR(NOW());";
        try {
            //**PreparedStatement creation
            PreparedStatement pTypeStatement = DBLink.startConnection().prepareStatement(pTSQL);
            ResultSet rs = pTypeStatement.executeQuery();
            rs.last();
            count = rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }
}
