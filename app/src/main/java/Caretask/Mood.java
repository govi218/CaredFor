package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gov on 5/26/2017.
 *
 */

public class Mood extends Caretask {
    private String mood;

    public Mood(){}

    public Mood(String mood) {
        this.mood = mood;
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> map = (HashMap<String, String>) super.toMap();
        map.put("mood", mood);
        return map;
    }
}
