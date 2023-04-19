
package controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customers;
import utils.DBCustomer;
import utils.DBLink;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class CustomersViewController implements Initializable {

    @FXML
    private Button modifyButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    public TableView<Customers> customersTable;
    @FXML
    private TableColumn<?, ?> customerID;
    @FXML
    private TableColumn<?, ?> customerName;
    @FXML
    private TableColumn<?, ?> customerAddress;
    @FXML
    private TableColumn<?, ?> customerCity;
    @FXML
    private TableColumn<?, ?> customerPhone;
    @FXML
    private Button addButton;
    @FXML
    private TableColumn<?, ?> customerAddressId;
    private static Customers handOff = null;

    //Public Method to return Customers handOff
    public static Customers getHandOff() {
        return handOff;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        pullCustomersViewData();
    }

    //method to populate customersTable
    private void pullCustomersViewData() {
        DBLink.startConnection();
        customersTable.setItems(DBCustomer.createCustomer());
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAddressId.setCellValueFactory(new PropertyValueFactory<>("addressId"));
    }

    @FXML
    private void onModify(ActionEvent event) {
        handOff = customersTable.getSelectionModel().getSelectedItem();
        if (handOff == null) {
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyCustomer.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelButton(ActionEvent event) throws IOException {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Cancel");
        alertConfirm.setHeaderText("Are you sure you would to cancel?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            DBLink.startConnection();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        handOff = customersTable.getSelectionModel().getSelectedItem();
        if (handOff == null) {
            return;
        }
        int customerIdInt = handOff.getCustomerId();
        int addressIdInt = handOff.getAddressId();
        DBCustomer.deleteCustomer(customerIdInt, addressIdInt);
        customersTable.setItems(DBCustomer.createCustomer());
    }

    @FXML
    private void onAddButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
