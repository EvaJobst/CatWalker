package at.fhhgb.catwalker.data;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lisa on 15.06.2016.
 */
public class University {
    private String name;
    private HashMap<String, Cat> cats;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
