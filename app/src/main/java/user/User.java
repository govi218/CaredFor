package user;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gov on 5/14/2017.
 * A user of CaredFor
 */

public abstract class User implements Serializable{

    private static final long serialVersionUID = 3366622501044354019L;

    //user values
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String seniorID;
    private String password;
    private String userID;


    //Constructors, blank constructor is used for Firebase queries
    User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    User(){}


    //Sea of getters and setters
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSeniorID() {
        return seniorID;
    }

    public void setSeniorID(String seniorID) {
        this.seniorID = seniorID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        /* hash function goes here */
        this.password = password;
    }

    //Return a map of all user details for pushing to db
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phoneNumber", phoneNumber);
        result.put("password", password);
        result.put("email", email);
        result.put("seniorID", seniorID);
        return result;
    }
}
