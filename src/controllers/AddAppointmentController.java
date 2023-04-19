
package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;
import model.Users;
import utils.DBCustomer;
import utils.DBLink;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class AddAppointmentController implements Initializable {

    private TextField phoneText;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<Users> userCombo;
    @FXML
    private ComboBox<Customers> customerCombo;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private Button addCustomerButton;
    @FXML
    private TextField appointmentIdText;
    @FXML
    private TextField typeText;
    @FXML
    private DatePicker datePicker;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userCombo.setItems(DBCustomer.getAllUsers());
        userCombo.getSelectionModel().selectFirst();
        customerCombo.setItems(DBCustomer.getAllCustomers());
        startCombo.getSelectionModel().selectFirst();

        LocalTime start = LocalTime.of(6, 0);
        LocalTime end = LocalTime.of(18, 30);

        while (start.isBefore(end)) {
            startCombo.getItems().add(start);
            start = start.plusMinutes(15);
        }
        start = LocalTime.of(6, 15);
        end = LocalTime.of(18, 45);

        while (start.isBefore(end)) {
            endCombo.getItems().add(start);
            start = start.plusMinutes(15);
        }

        for (int i = 0; i < userCombo.getItems().size(); i++) {
            Users user = userCombo.getItems().get(i);
        }
        
        for (int n = 0; n < customerCombo.getItems().size(); n++) {
            Customers customer = customerCombo.getItems().get(n);
        }
    }

    private void initAddAppointment() {
    }

    @FXML
    private void onCancelButton(ActionEvent event) throws IOException {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Cancel");
        alertConfirm.setHeaderText("Are you sure you would to cancel?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/scheduling.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
        }
    }

    @FXML
    private void onAddButton(ActionEvent event) throws IOException {
        Customers customer = customerCombo.getValue();
        Users user = userCombo.getValue();
        String type = typeText.getText();
        LocalDate date = datePicker.getValue();
        LocalTime start = startCombo.getValue();
        LocalTime end = endCombo.getValue();

        if (DBCustomer.hasOverlap(user.getUserId(), 0, date, start, end)) {
            error();
            System.out.println("Overlap");
            return;
        }

        DBCustomer.CreateAppointment(customer.getCustomerId(), user.getUserId(), type, date, start, end);

        Parent root = FXMLLoader.load(getClass().getResource("/view/scheduling.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        DBLink.startConnection();
    }

    private void error() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Appointment Overlap");
        alert.setContentText("Appointment start/end time overlaps with another");
        alert.showAndWait();
    }
}
