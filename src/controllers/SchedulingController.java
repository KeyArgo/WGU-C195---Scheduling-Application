package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import utils.DBCustomer;
import utils.DBLink;

/**
 * FXML Controller class
 *
 * @author Daniel LaForce
 */
public class SchedulingController implements Initializable {

    @FXML
    private Button modifyButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button exitButton;
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
    @FXML
    private TableColumn<?, ?> user;
    @FXML
    private ToggleGroup toggles;
    @FXML
    private Label scheduling;
    @FXML
    private RadioButton all;
    @FXML
    private RadioButton month;
    @FXML
    private RadioButton week;
    @FXML
    private Button backButton;
    private static Appointment handOff = null;
    private static model.Customers custHandOff = null;
    private model.Customers Customer = null;
    DateTimeFormatter formatLDT = DateTimeFormatter.ofPattern("hh:mm a");

    //Method to return Appointment handOff
    public static Appointment getHandOff() {
        return handOff;
    }

    //Method to return Customer handOff
    public static model.Customers getCustHandOff() {
        return custHandOff;
    }

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
        modifyButton.setText(langTranslation.getString("Modify"));
        deleteButton.setText(langTranslation.getString("Delete"));
        addButton.setText(langTranslation.getString("Add"));
        backButton.setText(langTranslation.getString("Back"));
        scheduling.setText(langTranslation.getString("Scheduling"));
        all.setText(langTranslation.getString("all"));
        month.setText(langTranslation.getString("Month"));
        week.setText(langTranslation.getString("Week"));
        pullSchedulingData();

    }

    //Method to populate schedulingTable
    private void pullSchedulingData() {
        DBLink.startConnection();
        schedulingTable.setItems(DBCustomer.getAllAppointments());
        customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        user.setCellValueFactory(new PropertyValueFactory<>("userName"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTimeDisplay"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTimeDisplay"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    private void onModify(ActionEvent event) throws IOException {
        handOff = schedulingTable.getSelectionModel().getSelectedItem();
        if (handOff == null) {
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyAppointment.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        handOff = schedulingTable.getSelectionModel().getSelectedItem();
        if (handOff == null) {
            return;
        }
        int appointmentIdInt = handOff.getAppointmentId();
        int customerIdInt = handOff.getCustomerId();
        int userIdInt = handOff.getUserId();
        DBCustomer.deleteAppointment(appointmentIdInt, customerIdInt, userIdInt);
        schedulingTable.setItems(DBCustomer.getAllAppointments());
    }

    @FXML
    private void onAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void onAll(ActionEvent event) {
        schedulingTable.setItems(DBCustomer.getAllAppointments());
    }

    //*Using a lamda to filter appointments based on Month
    @FXML
    private void onMonth(ActionEvent event) {//possible lamda
        ObservableList<Appointment> appointments = DBCustomer.getAllAppointments();
        int Month = LocalDateTime.now().getMonthValue();
        ObservableList<Appointment> monthAppointments = appointments.filtered(a -> {
            if (Month == a.getStartTime().getMonthValue()) {
                return true;
            }
            return false;
        });
        schedulingTable.setItems(monthAppointments);
    }

    //*Using a lamda to filter appointments based on Week
    @FXML
    private void onWeek(ActionEvent event) {//possible lamda
        ObservableList<Appointment> appointments = DBCustomer.getAllAppointments();

        ObservableList<Appointment> weekAppointments = appointments.filtered(a -> {
            LocalDateTime sunday = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
            while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
                sunday = sunday.minusDays(1);
            }
            if (a.getStartTime().isAfter(sunday) && a.getStartTime().isBefore(sunday.plusDays(7))) {
                return true;
            }
            return false;
        });
        schedulingTable.setItems(weekAppointments);
    }
}
