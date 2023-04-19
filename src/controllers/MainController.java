package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class MainController implements Initializable {

    @FXML
    private Button schedulingButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Label mainMenu;
    @FXML
    private Button reports;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResourceBundle langTranslation;
        langTranslation = ResourceBundle.getBundle("AEScheduling/Nat", Locale.getDefault());
        addCustomerButton.setText(langTranslation.getString("addCustomer"));
        customersButton.setText(langTranslation.getString("customers"));
        logOutButton.setText(langTranslation.getString("logOut"));
        reports.setText(langTranslation.getString("reports"));
        mainMenu.setText(langTranslation.getString("mainMenu"));
    }

    @FXML
    private void onCustomersButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customersView.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onAddButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onSchedulingButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/scheduling.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onLogOutButton(ActionEvent event) throws IOException {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Log Out of Scheduler?");
        alertConfirm.setHeaderText("Are you sure you would like to LOG OUT?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) logOutButton.getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
        }
    }

    @FXML
    private void onReportsButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
