package at.fhhgb.catwalker.data;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lisa on 06.06.2016.
 */
public class TimelineData {
    HashMap<String,Post> posts = new HashMap<String,Post>();

    public TimelineData(){
    }

    public TimelineData(Iterable<DataSnapshot> ds){
        for (DataSnapshot d: ds ) {
            Post p = d.getValue(Post.class);
            posts.put(d.getKey(), p);
        }

    }

    //add or modify an entry
    public void add(String key, Post p){
        if(p!=null)
            posts.put(key, p);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        for ( Post p : posts.values()) {
            s.append(p.toString() + "\n");
        }
        s.append("}");
        return s.toString();
    }

    public List<String> toStringList() {
        List<String>  s = new ArrayList<String>();

        for ( Post p : posts.values()) {
            s.add(p.toString());
        }

        return s;
    }

    public void remove(String key) {
        posts.remove(key);
    }

}
