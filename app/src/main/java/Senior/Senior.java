package Senior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import user.User;

/**
 * Created by Gov on 5/19/2017.
 * Represents a Senior
 */

public class Senior implements Serializable {

    private static final long serialVersionUID = -5407434525942192051L;

    private String seniorID;
    private String firstName;
    private String lastName;
    private String address;
    private String dateOfBirth;


    private String sex;
    private ArrayList<String> listeners = new ArrayList<>();
    //private String wellnessScore;
    //private ArrayList<User> relations;

    private Map<String, Map<String, String>> foodIntake;

    private Map<String, Map<String, String>> hygiene;
    private Map<String, Map<String, String>> medication;
    private Map<String, Map<String, String>> careSchedule;
    private Map<String, Map<String, String>> activities;
    private Map<String, Map<String, String>> mood;
    private Map<String, Map<String, String>> sleep;
    private Map<String, Map<String, String>> chat;

    public Senior (){}

    public Senior (String firstName, String lastName, String address, String dateOfBirth, String sex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.listeners = new ArrayList<>();
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public String getSeniorID() {
        return seniorID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSeniorID(String seniorID) {
        this.seniorID = seniorID;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<String> listeners) {
        this.listeners = listeners;
    }

    public void addListener(String listenerID) {
        listeners.add(listenerID);
    }

    public Map<String, Map<String, String>> getHygiene() {
        return hygiene;
    }

    public void setHygiene(Map<String, Map<String, String>> hygiene) {
        this.hygiene = hygiene;
    }

    public Map<String, Map<String, String>> getMedication() {
        return medication;
    }

    public void setMedication(Map<String, Map<String, String>> medication) {
        this.medication = medication;
    }

    public Map<String, Map<String, String>> getFoodIntake() {
        return foodIntake;
    }

    public void setFoodIntake(Map<String, Map<String, String>> foodIntake) {
        this.foodIntake = foodIntake;
    }

    public Map<String, Map<String, String>> getCareSchedule() {
        return careSchedule;
    }

    public void setCareSchedule(Map<String, Map<String, String>> careSchedule) {
        this.careSchedule = careSchedule;
    }

    public Map<String, Map<String, String>> getActivities() {
        return activities;
    }

    public void setActivities(Map<String, Map<String, String>> activities) {
        this.activities = activities;
    }

    public Map<String, Map<String, String>> getMood() {
        return mood;
    }

    public void setMood(Map<String, Map<String, String>> mood) {
        this.mood = mood;
    }

    public Map<String, Map<String, String>> getSleep() {
        return sleep;
    }

    public void setSleep(Map<String, Map<String, String>> sleep) {
        this.sleep = sleep;
    }

    public Map<String, Object> toMap(){

        HashMap<String, Object> senior = new HashMap<>();
        senior.put("firstName", firstName);
        senior.put("lastName", lastName);
        senior.put("address", address);
        senior.put("dateOfBirth", dateOfBirth);
        senior.put("sex", sex);
        senior.put("hygiene", hygiene);
        senior.put("medication", medication);
        senior.put("foodIntake", foodIntake);
        senior.put("careSchedule", careSchedule);
        senior.put("activities", activities);
        senior.put("mood", mood);
        senior.put("sleep", sleep);
        senior.put("listeners", listeners);
        senior.put("seniorID", seniorID);

        return senior;
    }
}
