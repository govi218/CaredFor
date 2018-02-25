package user;

import java.util.ArrayList;

/**
 * Created by Gov on 5/16/2017.
 *
 */

public class Caretaker extends User {
    public Caretaker(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber);
    }
    public Caretaker(){}

    @Override
    public void setUserID(String userID) {
        super.setUserID(userID);
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public String getUserID() {
        return super.getUserID();
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    public String getSeniorID() {
        return super.getSeniorID();
    }

    @Override
    public void setSeniorID(String seniorID) {
        super.setSeniorID(seniorID);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }
}
