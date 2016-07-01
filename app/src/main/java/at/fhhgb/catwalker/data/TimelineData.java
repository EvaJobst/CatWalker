package at.fhhgb.catwalker.data;

import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lisa on 06.06.2016.
 */
public class TimelineData {
    HashMap<String,Post> posts = new HashMap<String,Post>();

    public TimelineData(){
        posts = new HashMap<String,Post>();
    }

    public TimelineData(Iterable<DataSnapshot> ds){
        posts = new HashMap<String,Post>();
        for (DataSnapshot d: ds ) {
            Post p = d.getValue(Post.class);
            add(d.getKey(), p);
        }
    }

    //add or modify an entry
    public void add(String key, Post p){
        p.setId(key);
        if(p!=null)
            posts.put(key, p);
    }

    public void remove(String key) {
        posts.remove(key);
    }

    public List<Post> orderByDate(final boolean ascending) {
        List<Post> list = new ArrayList<Post>();

        for ( Post p : posts.values()) {
            list.add(p);
        }
        Collections.sort(list, new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                try {
                    Date date1 = Post.getDateFormat().parse( lhs.getDateTime() );
                    Date date2 = Post.getDateFormat().parse( lhs.getDateTime() );
                    if(ascending)
                        return (int) (date1.getTime()-date2.getTime());
                    else
                        return (int) -(date1.getTime()-date2.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return list;
    }

    public List<Post> select(final boolean ascending) {
        List<Post> list = new ArrayList<Post>();

        for ( Post p : posts.values()) {
            list.add(p);
        }
        Collections.sort(list, new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                try {
                    Date date1 = Post.getDateFormat().parse( lhs.getDateTime() );
                    Date date2 = Post.getDateFormat().parse( lhs.getDateTime() );
                    if(ascending)
                        return (int) (date1.getTime()-date2.getTime());
                    else
                        return (int) -(date1.getTime()-date2.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return list;
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

}
