package user;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Gov on 5/16/2017.
 *
 */

public class Relative extends User {

    private String relation;
    public Relative(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber);
    }

    public Relative(){}

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
    public Map<String, Object> toMap() {
        return super.toMap();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }


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


    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
