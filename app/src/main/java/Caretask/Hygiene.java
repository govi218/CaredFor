package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 5/25/2017.
 *
 */

public class Hygiene extends Caretask {

    private static final long serialVersionUID = -6490553609430713237L;
    private String hygiene;

    public String getHygiene() {
        return hygiene;
    }

    public void setHygiene(String hygiene) {
        this.hygiene = hygiene;
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> Hygiene = (HashMap<String, String>) super.toMap();
        Hygiene.put("hygiene", hygiene);
        return Hygiene;
    }
}
