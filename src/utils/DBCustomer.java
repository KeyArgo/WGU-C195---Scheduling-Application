package utils;

import com.mysql.jdbc.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customers;
import model.Users;
import model.City;

/**
 *
 * @author Daniel LaForce
 */
public class DBCustomer {

    //Method for verifying user input on login screen
    public static boolean verifyUserInfo(String User, String Pass) {
        String sqlData = "Select * FROM user WHERE userName=? AND password=?";
        System.out.println(sqlData);
        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(sqlData);
            statement.setString(1, User);
            statement.setString(2, Pass);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String userName = rs.getString("userName");
                String userPass = rs.getString("password");
                System.out.println("test: " + userName);
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("User has not authenticated: ");
        return false;
    }

    //Method to pull city list from database
    public static ObservableList<City> getAllCities() {
        ObservableList<City> cities = FXCollections.observableArrayList();
        String sqlData = "SELECT cityId,city FROM city";
        System.out.println(sqlData);
        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(sqlData);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int cityId = rs.getInt("cityId");
                String cityName = rs.getString("city");
                City city = new City(cityId, cityName);
                System.out.println(cityId + " " + cityName);
                cities.add(city);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cities;
    }

    //Method to pull customer list from database
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String sqlData = "SELECT customerId,customerName FROM customer";
        System.out.println(sqlData);
        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(sqlData);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                Customers customer = new Customers(customerId, customerName);
                System.out.println(customerId + " " + customerName);
                customers.add(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    //Method to get user list from database
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> users = FXCollections.observableArrayList();
        String sqlData = "SELECT userId,userName FROM user";
        System.out.println(sqlData);
        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(sqlData);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                Users user = new Users(userId, userName);
                System.out.println(userId + " " + userName);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    //Method to pull appointments from database
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointment = FXCollections.observableArrayList();
        String sqlData = "SELECT appointmentId,customer.customerId,customerName,user.userId,userName,type,start,end FROM appointment,customer,user \n"
                + "WHERE customer.customerId = appointment.customerId AND user.userId = appointment.userId";
        System.out.println(sqlData);
        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(sqlData);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String customerName = rs.getString("customerName");
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String type = rs.getString("type");
                Timestamp start = rs.getTimestamp("start");
                LocalDateTime startTime = start.toLocalDateTime();//Time zone conversion
                startTime = zoneTimeToSystem(startTime);
                Timestamp end = rs.getTimestamp("end");
                LocalDateTime endTime = end.toLocalDateTime();//Time zone conversion
                endTime = zoneTimeToSystem(endTime);
                Appointment ap = new Appointment(appointmentId, customerId, customerName, userId, userName, type, startTime, endTime);
                appointment.add(ap);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return appointment;
    }

    //Method to convert UTC time to system time
    public static LocalDateTime zoneTimeToSystem(LocalDateTime dBTime) {
        //Zone database value to UTC
        ZoneId utcZID = ZoneId.of("UTC");
        ZonedDateTime zdtStart = dBTime.atZone(utcZID);

        //Convert zoned UTC time to systemDefault
        ZonedDateTime localTimeZoned = zdtStart.withZoneSameInstant(ZoneId.systemDefault());

        //Convert back to LocalDateTime
        LocalDateTime ldtZoned = convert(localTimeZoned);
        return ldtZoned;
    }

    //Method to convert system time to UTC time.
    private static LocalDateTime systemTimeToUTC(LocalDateTime dBTime) {
        //Zone database value to UTC
        ZoneId utcZID = ZoneId.of("UTC");
        ZonedDateTime zdtStart = dBTime.atZone(ZoneId.systemDefault());

        //Convert zoned UTC time to systemDefault
        ZonedDateTime localTimeZoned = zdtStart.withZoneSameInstant(utcZID);

        //Convert back to LocalDateTime
        LocalDateTime ldtZoned = convert(localTimeZoned);
        return ldtZoned;
    }

    //Method to convert ZonedDateTime to LocalDateTime
    private static LocalDateTime convert(ZonedDateTime zdtStart) {
        return zdtStart.toLocalDateTime();
    }

    //Method to pull customer records from database and popular into a customer list
    public static ObservableList<Customers> createCustomer() {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String sqlData = "SELECT customerId,customerName,customer.addressId,address,phone,address.cityId,city FROM customer,address,city \n"
                + "WHERE customer.addressId=address.addressId AND city.cityId=address.cityId";
        System.out.println(sqlData);

        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(sqlData);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String customerName = rs.getString("customerName");
                int customerId = rs.getInt("customerId");
                int addressId = rs.getInt("addressId");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int cityId = rs.getInt("cityId");
                String city = rs.getString("city");
                Customers customer = new Customers(customerId, customerName, addressId, address, phone, cityId, city);
                customers.add(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    //Method to update customer record in database with user input values on modify customer form
    public static void modifyCustomer(int customerId, int addressId, String customerName, String address, String phone, int cityId) {
        System.out.println("DBCustomer says: Customer Id is '" + customerId + "' Customer name is '" + customerName + "' address is '" + address + "' CityId is '" + cityId + "' addressId is '" + addressId + "'");

        try {
            //Update customer Table MYSQL statement
            String uCSQL = "UPDATE customer set customerName = ?, addressId = ? WHERE customerId = ?";
            PreparedStatement uCStatement = DBLink.startConnection().prepareStatement(uCSQL);

            //**Replace "?" with variable values
            uCStatement.setString(1, customerName);
            uCStatement.setInt(2, addressId);
            uCStatement.setInt(3, customerId);
            uCStatement.execute();

            //**Update address Table MYSQL statement
            String uASQL = "UPDATE address set address = ?, phone = ?, cityId = ? WHERE addressId = ?";

            //**Update address Table record PreparedStatement creation
            PreparedStatement uAStatement = DBLink.startConnection().prepareStatement(uASQL);

            //**Replace "?" with variable values
            uAStatement.setString(1, address);
            uAStatement.setString(2, phone);
            uAStatement.setInt(3, cityId);
            uAStatement.setInt(4, addressId);
            uAStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    //Method to delete user selected customer record from database
    public static void deleteCustomer(int addressId, int customerId) {
        String dCSQL = "DELETE from customer WHERE customer.customerId = ?";
        String dASQL = "DELETE from address WHERE address.addressId = ?";
        String dApSQL = "DELETE from appointment WHERE customerId = ?";
        try {

            //**PreparedStatement creation
            PreparedStatement dApStatement = DBLink.startConnection().prepareStatement(dApSQL);

            //**Replace "?" with variable values
            dApStatement.setInt(1, customerId);

            //**Execute query and retrieve results
            dApStatement.execute();

            //**PreparedStatement creation
            PreparedStatement dCStatement = DBLink.startConnection().prepareStatement(dCSQL);

            //**Replace "?" with variable values
            dCStatement.setInt(1, customerId);

            //**Execute query and retrieve results
            dCStatement.execute();

            //**PreparedStatement creation
            PreparedStatement dAStatement = DBLink.startConnection().prepareStatement(dASQL);

            //**Replace "?" with variable values
            dAStatement.setInt(1, addressId);

            //**Execute query and retrieve results
            dAStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Method to add user input values for when adding a new customer, to the database
    public static void CreateCustomer(String customerName, String customerAddress, String phone, int cityId) {
        String aSQL = "INSERT INTO address VALUES (NULL, ?, '',?,'',?,NOW(),'',NOW(),'')";
        String cSQL = "INSERT INTO customer VALUES (NULL, ?, ?, 1, NOW(), '', NOW(), '')";

        try {
            PreparedStatement statement = DBLink.startConnection().prepareStatement(aSQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customerAddress);
            statement.setInt(2, cityId);
            statement.setString(3, phone);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int addressId = rs.getInt(1);

            PreparedStatement cStatement = DBLink.startConnection().prepareStatement(cSQL);
            cStatement.setString(1, customerName);
            cStatement.setInt(2, addressId);
            cStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Method to add user input values on Create Appointment form into database appointment table
    public static void CreateAppointment(int customerId, int userId, String type, LocalDate date, LocalTime start, LocalTime end) {
        String apSQL = "INSERT INTO appointment VALUES (NULL, ?,?,'','','','',?,'',?,?,NOW(),?,NOW(),?)";

        try {
            LocalDateTime SLDT;
            LocalDateTime ELDT;
            SLDT = LocalDateTime.of(date, start);
            SLDT = systemTimeToUTC(SLDT);
            ELDT = LocalDateTime.of(date, end);
            ELDT = systemTimeToUTC(ELDT);
            Timestamp ts = Timestamp.valueOf(SLDT); //Timezone conversion
            Timestamp te = Timestamp.valueOf(ELDT);//Timezone conversion

            PreparedStatement apStatement = DBLink.startConnection().prepareStatement(apSQL, Statement.RETURN_GENERATED_KEYS);
            apStatement.setInt(1, customerId);
            apStatement.setInt(2, userId);
            apStatement.setString(3, type);
            apStatement.setTimestamp(4, ts);
            apStatement.setTimestamp(5, te);
            apStatement.setString(6, type);
            apStatement.setString(7, type);
            apStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Method to modify database record with user inputs within the Modify Appointment form
    public static void modifyAppointment(int appointmentId, int customer, int user, String type, LocalDate date, LocalTime start, LocalTime end) {
        try {
            //Update appointment Table MYSQL statement
            String uApSQL = "UPDATE appointment set customerId = ?, userId = ?, type = ?, start = ?, end = ? WHERE appointmentId = ?";
            LocalDateTime SLDT;
            LocalDateTime ELDT;
            SLDT = LocalDateTime.of(date, start);
            SLDT = systemTimeToUTC(SLDT);
            ELDT = LocalDateTime.of(date, end);
            ELDT = systemTimeToUTC(ELDT);
            Timestamp ts = Timestamp.valueOf(SLDT); //Timezone conversion
            Timestamp te = Timestamp.valueOf(ELDT);//Timezone conversion

            //**Update customer Table record PreparedStatement creation
            PreparedStatement uApStatement = DBLink.startConnection().prepareStatement(uApSQL);

            //**Replace "?" with variable values
            uApStatement.setInt(1, customer);
            uApStatement.setInt(2, user);
            uApStatement.setString(3, type);
            uApStatement.setTimestamp(4, ts);
            uApStatement.setTimestamp(5, te);
            uApStatement.setInt(6, appointmentId);
            uApStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    //Method to delete record in database for user selection
    public static void deleteAppointment(int appointmentId, int customerId, int userId) {
        System.out.println("DBCustomer says: Appointment Id is '" + appointmentId + "' customerId is '" + customerId + "'" + "' userId is '" + userId + "'");

        String dASQL = "DELETE from appointment WHERE appointment.appointmentId = ?";

        try {
            //**PreparedStatement creation
            PreparedStatement dApStatement = DBLink.startConnection().prepareStatement(dASQL);

            //**Replace "?" with variable values
            dApStatement.setInt(1, appointmentId);

            //**Execute query and retrieve results
            dApStatement.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    //Method to alert when there is an overlap in the time of another appointment when creating a new appointment
    public static boolean hasOverlap(int userId, int appointmentId, LocalDate date, LocalTime start, LocalTime end) {
        ObservableList<Appointment> appointments = DBCustomer.getAllAppointments();

        //*Using a lamda to filter appointments based on userId
        ObservableList<Appointment> uList = appointments.filtered(a -> {
            if (userId == a.getUserId() && appointmentId != a.getAppointmentId()) {
                return true;
            }
            return false;
        });

        LocalDateTime pS = LocalDateTime.of(date, start);
        LocalDateTime pE = LocalDateTime.of(date, end);
        for (Appointment a : uList) {
            LocalDateTime aS = a.getStartTime();
            LocalDateTime aE = a.getEndTime();
            if ((aS.isAfter(pS) || aS.isEqual(pS)) && aS.isBefore(pE)) {
                return true;
            }
            if (aE.isAfter(pS) && (aE.isBefore(pE) || aE.isEqual(pE))) {
                return true;
            }

            if ((aS.isBefore(pS) || aS.isEqual(pS)) && (aE.isAfter(pE) || aE.isEqual(pE))) {
                return true;
            }
        }
        return false;
    }
}
