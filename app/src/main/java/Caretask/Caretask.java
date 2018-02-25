package Caretask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gov on 5/24/2017.
 * Generic update class
 */

public abstract class Caretask implements Serializable{

    private static final long serialVersionUID = 448337401378002492L;

    private String caretakerComments;
    private String relativeComments;

    Caretask() {}

    public String getCaretakerComments() {
        return caretakerComments;
    }

    public void setCaretakerComments(String caretakerComments) {
        this.caretakerComments = caretakerComments;
    }

    public String getRelativeComments() {
        return relativeComments;
    }

    public void setRelativeComments(String relativeComments) {
        this.relativeComments = relativeComments;
    }

    public Map<String, String> toMap(){
        HashMap<String, String> caretask = new HashMap<>();
        caretask.put("caretakerComments", caretakerComments);
        caretask.put("relativeComments", relativeComments);
        return caretask;
    }
}
