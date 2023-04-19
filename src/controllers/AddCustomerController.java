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
 * @author Daniel LaForce
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Button addCustomerButton;
    @FXML
    private TextField customerIDText;
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cityCombo.setItems(DBCustomer.getAllCities());
        cityCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void onAddButton(ActionEvent event) throws IOException {
        String errorText = missingInformation();
        //if no missing fields
        if (errorText.equals("None")) {
            missingInformation();
            String customerName = customerNameText.getText();
            String address = addressText.getText();
            String phone = phoneText.getText();
            City city = cityCombo.getValue();
            System.out.println("on add");
            DBCustomer.CreateCustomer(customerName, address, phone, city.getCityId());
            Parent root = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            // display error message if missing information
        } else {
            error(errorText);
        }
    }

    @FXML
    private void onCancelButton(ActionEvent event) throws IOException {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Cancel");
        alertConfirm.setHeaderText("Are you sure you would to cancel?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
        }
    }

    private void error(String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Missing Required Information");
        alert.setContentText("Please correct the following error: " + e);
        alert.showAndWait();
    }

    private String missingInformation() {
        String e;
        if (customerNameText.getText().equals("")) {
            e = "'Customer Name' is required.";
        } else if (addressText.getText().equals("")) {
            e = "Address is required";
        } else if (phoneText.getText().equals("")) {
            e = "'Phone number is required.";
        } else {
            e = "None";
        }
        return e;
    }

    @FXML
    void onCityCombo(ActionEvent event) {
    }

}
