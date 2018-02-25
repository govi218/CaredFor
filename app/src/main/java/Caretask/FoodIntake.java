package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gov on 5/25/2017.
 *
 */

public class FoodIntake extends Caretask {

    private static final long serialVersionUID = 5285243457795419989L;
    private String breakfast;
    private String lunch;
    private String dinner;

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> Hygiene = (HashMap<String, String>) super.toMap();
        Hygiene.put("breakfast", breakfast);
        Hygiene.put("lunch", lunch);
        Hygiene.put("dinner", dinner);
        return Hygiene;
    }
}
