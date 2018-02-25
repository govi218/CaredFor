package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 5/25/2017.
 *
 */

public class Activities extends Caretask {

    private static final long serialVersionUID = -2053757813700538667L;

    private String activity;

    private String vacuuming;
    private String laundry;
    private String cleanRooms;
    private String garbage;
    private String dishwash;

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    @Override
    public Map<String, String> toMap(){
        HashMap<String, String> map = (HashMap<String, String>) super.toMap();
        map.put("activity", activity);
        map.put("vacuuming", vacuuming);
        map.put("laundry", laundry);
        map.put("cleanRooms", cleanRooms);
        map.put("garbage", garbage);
        map.put("dishwash", dishwash);
        return map;
    }

}


