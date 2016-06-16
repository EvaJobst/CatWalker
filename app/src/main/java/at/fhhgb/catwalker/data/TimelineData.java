package at.fhhgb.catwalker.data;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lisa on 06.06.2016.
 */
public class TimelineData {
    List<Post> posts;
    public TimelineData(){}

    public TimelineData(Iterable<DataSnapshot> ds){
        posts=new ArrayList<Post>();
        for (DataSnapshot d: ds ) {
            Post p = d.getValue(Post.class);
            //p.setDateTime((String)d.child("dateTime").getValue());
            posts.add(p);
        }

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");
        for(int i=0; i<posts.size() ;i++)
            s.append(posts.get(i).toString()+" | ");
        s.append("}");
        return s.toString();
    }

    public String[] toStringArray() {
        String[] s = new String[posts.size()];

        for(int i=0; i<posts.size() ;i++)
            s[i] = posts.get(i).toString()+" ";

        return s;
    }

}
