
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Users;
import utils.DBCustomer;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class ByUserController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private ComboBox<Users> userCombo;
    @FXML
    private TableView<Appointment> schedulingTable;
    @FXML
    private TableColumn<?, ?> startTime;
    @FXML
    private TableColumn<?, ?> endTime;
    @FXML
    private TableColumn<?, ?> type;
    @FXML
    private TableColumn<?, ?> customer;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userCombo.setItems(DBCustomer.getAllUsers());
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTimeDisplay"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTimeDisplay"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
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


    @FXML
    private void onUserCombo(ActionEvent event) {
        Users user = userCombo.getValue();
        ObservableList<Appointment> appointments = DBCustomer.getAllAppointments();
        
        //*Using a lamda to filter appointments based on userId
        ObservableList<Appointment> uList = appointments.filtered(a -> {
            if(user.getUserId() == a.getUserId()){
                return true;
            }
            return false;
        });
        schedulingTable.setItems(uList);
    }
}
