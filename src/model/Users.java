
package model;

/**
 *
 * @author Daniel LaForce
 */

public class Users {

    private int userId;
    private String userName;

    public Users(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAllUsers() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //The combobox uses this toString method to display the userName in its list
    public String toString() {
        return userName;
    }
}
