
package controllers;

import com.mysql.jdbc.Connection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import utils.DBCustomer;
import utils.DBLink;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */

public class LoginController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private TextField customerIDText;
    @FXML
    private Button exitButton;
    @FXML
    private TextField userIdTxt;
    @FXML
    private TextField passwordTxt;
    @FXML
    private Label schedulerLogin;
    @FXML
    private Label userId;
    @FXML
    private Label password;
    @FXML
    private Label warning;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Connection connect = null;
    private Statement statement = null;


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
        DBLink dblink = new DBLink();

        loginButton.setText(langTranslation.getString("Login"));
        exitButton.setText(langTranslation.getString("Exit"));
        schedulerLogin.setText(langTranslation.getString("Title"));
        userId.setText(langTranslation.getString("userId"));
        password.setText(langTranslation.getString("password"));
        warning.setText(langTranslation.getString("warning"));
    }

    //Test credentials (test:test)
    @FXML
    private void onLoginButton(ActionEvent event) throws SQLException {
        String error = null;
        System.out.println("loginButton");
        String querySQL = "SELECT * FROM user";

        try {
            String userName = userIdTxt.getText();
            String password = passwordTxt.getText();
            boolean v = DBCustomer.verifyUserInfo(userName, password);

            if (v) {
                String logUser = userName;
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.hide();
                upcomingApt();
                Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                logger(logUser + " logged in at " + LocalDateTime.now());
            } else {
                error();
            }
        } catch (IOException ex) {
            Logger.getLogger(AEScheduling.Scheduling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onExitButton(ActionEvent event) {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Cancel");
        alertConfirm.setHeaderText("Are you sure you would to EXIT?");
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        } else {
        }
    }

    public static void logger(String logTxt) {
        String ts = "test";
        try {
            System.out.println("Write log to file: " + logTxt);
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(
                    new File("log.txt"),
                    true))) {
                pw.append(logTxt + "\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AEScheduling.Scheduling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void errorLog(String errorTxt) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try {
            System.out.println("Write error to file: " + ts);
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(
                    new File("errorlog" + ts + ".txt"),
                    true ))) {
                pw.append(errorTxt + "\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AEScheduling.Scheduling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void error() {
        ResourceBundle langTranslation;
        langTranslation = ResourceBundle.getBundle("AEScheduling/Nat", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(langTranslation.getString("auth"));
        alert.setContentText(langTranslation.getString("incorrect"));
        alert.showAndWait();
    }

    private void upcomingApt() {
        ObservableList<Appointment> appointments = DBCustomer.getAllAppointments();
        System.out.println("upcomingApt block started");
        
        //*Lamda to filter upcoming appointments within 15 minutes
        ObservableList<Appointment> flist = appointments.filtered(a -> {
            LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.now());
            
            if (a.getStartTime().isAfter(now) && a.getStartTime().isBefore(now.plusMinutes(15))) {
                return true;
            }
            return false;
        });
        
        if (flist.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment");
            alert.setHeaderText("You have an upcoming appointment within 15 minutes!");
            alert.setContentText("Appointment coming up");
            alert.showAndWait();
        }
    }
}
