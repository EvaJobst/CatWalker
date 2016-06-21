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
            posts.add(p);
        }

    }

    public void add(Post p){
        if(p!=null)
            posts.add(p);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        for(int i=0; i<posts.size() ;i++)
            s.append(posts.get(i).toString()+"\n");
        s.append("}");
        return s.toString();
    }

    public List<String> toStringList() {
        List<String>  s = new ArrayList<String>();

        for(int i=0; i<posts.size() ;i++)
            s.add(posts.get(i).toString());

        return s;
    }

    //removes first occurence  of a post (should most likely be the only)
    public void remove(Post p) {
        for (int i = 0; i < posts.size(); i++) {
            if(posts.get(i).isEqual(p)){
                posts.remove(i);
                return;
            }
        }

    }


}
