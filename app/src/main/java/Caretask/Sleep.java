package Caretask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gov on 5/26/2017.
 *
 */

public class Sleep extends Caretask{

    private static final long serialVersionUID = -4728388051457504961L;
    private String sleep;

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> map = (HashMap<String, String>) super.toMap();
        map.put("sleep", sleep);
        return map;
    }
}
