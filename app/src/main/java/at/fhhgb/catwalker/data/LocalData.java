package at.fhhgb.catwalker.data;

import android.graphics.Bitmap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lisa on 08.06.2016.
 * Contains the locally stored Data.
 */
public class LocalData {
    //User specific Data
    private String userId;
    private String universityId;
    private String user;

    //General Data
    private HashMap<String, String> userList;               //key, userName
    private HashMap<String, String> universityList;         //universityName, catName
    private HashMap<String, Post> allPostsList;             //key, Post
    private HashMap<String, Post> myPostsList;             //key, Post
    private HashMap<String, Bitmap> images;                 //key, Image

    //PropertyChangeSupport for raising propertyChange Events in case that some data has changed.
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public LocalData() {
        userList = new HashMap<>();
        universityList = new HashMap<>();
        allPostsList = new HashMap<>();
        myPostsList = new HashMap<>();
        images = new HashMap<>();
    }

    //----------------------------- PropertyChangeListener ------------------------------------//

    //Hooking a PropertyChangeListener
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    //Removing a PropertyChangeListener
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    //------------------------------------- User -----------------------------------------------//

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    //returns current user
    public String getUser() {
        return userList.get(userId);
    }

    //returns any user
    public String getUser(String userId) {
        return userList.get(userId);
    }

    public void updateUser(String name) {
        String old = "";
        if (user == null)
            user = name;
        else {
            old = user;
            user = name;
        }
        propertyChangeSupport.firePropertyChange("user", old, name);
    }

    public void updateUserList(String key, String value, boolean add) {
        if (add)
            userList.put(key, value);
        else
            userList.remove(key);
        //old val = key, new val = name
        propertyChangeSupport.firePropertyChange("user", key, userList.get(key));
    }

    //--------------------------------- University --------------------------------------------//

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        propertyChangeSupport.firePropertyChange("university.change", this.universityId, universityId);
        this.universityId = universityId;
    }

    public void setUniversityCat(String cat) {
        propertyChangeSupport.firePropertyChange("university.cat", getUniversityList().get(universityId), cat);
        getUniversityList().put(universityId, cat);
    }

    public void setUniversityList(HashMap<String, String> universityList) {
        this.universityList = universityList;
    }

    public HashMap<String, String> getUniversityList() {
        return universityList;
    }

    public void updateUniversityList(String key, String value, boolean add) {
        if (add) {
            universityList.put(key, value);
            propertyChangeSupport.firePropertyChange("university.add", null, key);
        } else {
            universityList.remove(key);
            propertyChangeSupport.firePropertyChange("university.remove", key, null);
        }
    }

    //---------------------------------- Timeline --------------------------------------------//

    public void addPost(String key, Post p, boolean userPost) {
        String property;
        p.setId(key);
        if (userPost) {
            this.myPostsList.put(key, p);
            property = "myPosts.add";
        } else {
            this.allPostsList.put(key, p);
            property = "timeline.add";
        }

        propertyChangeSupport.firePropertyChange(property, null, p);
    }

    public void removePost(String key, Post p, boolean userPost) {
        String property;
        if (userPost) {
            this.myPostsList.remove(key);
            property = "myPosts.remove";
        } else {
            this.allPostsList.remove(key);
            property = "timeline.remove";
        }
        propertyChangeSupport.firePropertyChange(property, p, key);
        //Todo: remove function for the timeline
    }

    public HashMap getAllPostsList() {
        return allPostsList;
    }

    public void resetTimeline(boolean userPost) {
        String property;
        if (userPost) {
            myPostsList = new HashMap<>();
            property = "myPosts.reset";
        } else {
            allPostsList = new HashMap<>();
            property = "timeline.reset";
        }
        propertyChangeSupport.firePropertyChange(property, null, null);
    }

    public void updatePost(String key, Post p) {
        if (allPostsList != null) {
            Post old = this.allPostsList.get(key);
            this.allPostsList.put(key, p);
            propertyChangeSupport.firePropertyChange("timeline.update", old, p);
        }
    }

    public Post getPost(String key){
        Post p =  allPostsList.get(key);
        if(p==null)
            p = myPostsList.get(key);
        return p;
    }

    //Images

    public void addImage(String key, Bitmap img){
        images.put(key,img);
        propertyChangeSupport.firePropertyChange("image.load", key, img);
    }

    public Bitmap getImage(String key){
        return images.get(key);
    }

    public HashMap<String,Post> getMyPostsList() {
        return myPostsList;
    }

    public List<Post> orderPostsByDate(HashMap<String, Post> posts) {
        List<Post> list = null;
        if (posts != null) {
            list = new ArrayList<>(posts.values());
            Collections.sort(list, new Comparator<Post>() {
                @Override
                public int compare(Post lhs, Post rhs) {
                    try {
                        Date date1 = Post.getDateFormat().parse(lhs.getDateTime());
                        Date date2 = Post.getDateFormat().parse(rhs.getDateTime());

                        return (int) (date1.getTime() - date2.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;

                    }
                }
            });
        }
        return list;
    }
}
