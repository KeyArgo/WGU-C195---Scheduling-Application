/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import model.City;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DBCustomer;

/**
 * FXML Controller class
 *
 * @author Argo
 */
public class ModifyCustomerController implements Initializable {

    @FXML
    private TextField customerIdText;
    @FXML
    private TextField customerNameText;
    @FXML
    private TextField addressText;
    @FXML
    private TextField phoneText;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<City> cityCombo;
    @FXML
    private Button modifyButton;
    @FXML
    private TextField addressIdText;
    private model.Customers Customer = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pullCustomerData();

    }

    //Method to populate Modify Customer form using user selection from the database record
    private void pullCustomerData() {
        //Retrieve handoff from CustomerViewController
        Customer = CustomersViewController.getHandOff();
        cityCombo.setItems(DBCustomer.getAllCities());
        cityCombo.getSelectionModel().selectFirst();
        customerIdText.setText(Integer.toString(Customer.getCustomerId()));
        customerNameText.setText(Customer.getCustomerName());
        addressText.setText(Customer.getAddress());
        phoneText.setText(Customer.getPhone());
        addressIdText.setText(Integer.toString(Customer.getAddressId()));

        for (int i = 0; i < cityCombo.getItems().size(); i++) {
            City city = cityCombo.getItems().get(i);
            if (city.getCityId() == Customer.getCityId()) {
                cityCombo.setValue(city);
                break;
            }
        }
    }

    @FXML
    private void onModifyButton(ActionEvent event) throws IOException {
        int customerId = 0;
        int addressId = 0;
        String customerIdString = customerIdText.getText();
        customerId = Integer.valueOf(customerIdString);
        String addressIdString = addressIdText.getText();
        addressId = Integer.valueOf(addressIdString);
        String customerName = customerNameText.getText();
        String address = addressText.getText();
        String phone = phoneText.getText();
        City city = cityCombo.getValue();
        DBCustomer.modifyCustomer(customerId, addressId, customerName, address, phone, city.getCityId());

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
        }
    }

    @FXML
    void onCityCombo(ActionEvent event) {
    }

}
