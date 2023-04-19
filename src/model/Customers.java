
package model;

/**
 *
 * @author Daniel LaForce
 */
public class Customers {

    private int customerId;
    private String customerName;
    private int addressId;
    private String address;
    private String phone;
    private int cityId;
    private String cityName;

    public Customers(int customerId, String customerName, int addressId, String address, String phone, int cityId, String cityName) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.address = address;
        this.phone = phone;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public Customers(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return (customerName);
    }  

}
