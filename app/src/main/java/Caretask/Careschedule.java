package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gov on 5/25/2017.
 *
 */

public class Careschedule extends Caretask {


    private static final long serialVersionUID = -1636454896983445232L;

    private String arrival;
    private String departure;

    public Careschedule(){}

    public Careschedule(String arrival, String departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> Careschedule = (HashMap<String, String>) super.toMap();
        Careschedule.put("arrival", arrival);
        Careschedule.put("departure", departure);
        return Careschedule;
    }
}
