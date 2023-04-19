
package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Daniel LaForce
 */
public class Appointment {

    private int appointmentId;
    private int customerId;
    private String customerName;
    private int userId;
    private String userName;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Appointment(int appointmentId, int customerId, String customerName, int userId, String userName, String type, LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Appointment(int appointmentId, LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = appointmentId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Appointment() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeDisplay() {
        DateTimeFormatter formatLDT = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");

        String formattedLDT = startTime.format(formatLDT);
        return formattedLDT;
        //return "Time is: " + startTime.toString();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getEndTimeDisplay() {
    DateTimeFormatter formatLDT = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");

        String formattedLDT = endTime.format(formatLDT);
        return formattedLDT;
      //  return endTime.toString();
    }

}
