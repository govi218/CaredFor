package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 5/25/2017.
 *
 */

public class Medication extends Caretask {

    private static final long serialVersionUID = -3168042257998058342L;
    private String medication;

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getMedication() {
        return medication;
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> Hygiene = (HashMap<String, String>) super.toMap();
        Hygiene.put("medication", medication);
        return Hygiene;
    }

}
