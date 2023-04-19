
package model;


/**
 *
 * @author Daniel LaForce
 */
public class City {

    private int cityId;
    private String cityName;

    public City(int cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getAllCities() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    //The combobox uses this toString method to display the cityName in its list
    public String toString() {
        return cityName;
    }
}
