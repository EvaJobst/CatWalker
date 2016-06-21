package at.fhhgb.catwalker.data;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Lisa on 05.06.2016.
 */
public class User {
    public String name;

    public User(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: "+name;
    }

}
