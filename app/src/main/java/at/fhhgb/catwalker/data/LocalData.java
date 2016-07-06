package at.fhhgb.catwalker.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
 * Contains the locally stored Data and provides getter and setter to interact with it.
 * Furthermore it throws PropertyChange events whenever data is changed.
 */
public class LocalData {
    //User specific Data
    private String userId;
    private String universityId;
    private String user;

    /**
     * Hashmap that stores all key, user pairs
     */
    private HashMap<String, String> userList;               //key, userName
    /**
     * Hashmap that stores all universityName, cat pairs
     */
    private HashMap<String, String> universityList;         //universityName, catName
    /**
     * Hashmap that stores all key, university post pairs
     */
    private HashMap<String, Post> allPostsList;             //key, Post
    /**
     * Hashmap that stores all key, user post pairs
     */
    private HashMap<String, Post> myPostsList;             //key, Post
    /**
     * Hashmap that stores all post key, image pairs
     */
    private HashMap<String, Bitmap> images;                 //key, Image

    /**
     *     PropertyChangeSupport for raising propertyChange Events in case that some data has changed.
     */
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public LocalData() {
        userList = new HashMap<>();
        universityList = new HashMap<>();
        allPostsList = new HashMap<>();
        myPostsList = new HashMap<>();
        images = new HashMap<>();
    }

    /**
     * Restores the Shared Preferences (userId, universityId)
     * @param view a view from which the SharedPreferences are acessed.
     * @return Returns true if it could restore the data.
     */
    public boolean restorePreferences(Activity view){
        SharedPreferences settings = view.getSharedPreferences("CatWalker_Data", Context.MODE_PRIVATE);
        String userId = settings.getString("userId", null);
        String universityId = settings.getString("universityId", null);

        if(userId !=null && universityId!=null) {
            setUserId(userId);
            setUniversityId(universityId);
            return true;
        }
        return false;
    }

    //----------------------------- PropertyChangeListener ------------------------------------//

    /**
     *     Hooking in a PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removing a PropertyChangeListener
     */
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

    /**
     * @return Returns the current user.
     */
    public String getUser() {
        return userList.get(userId);
    }

    /**
     * @param userId the userId for which the data will be fetched
     * @return returns the user data
     */
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

    /**
     * Adds an element from the timeline.
     * @param userPost Specifies whether the {@link #myPostsList}(true) or the {@link #allPostsList}(false) will be changed.
     */
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
    /**
     * Removes an element from the timeline.
     * @param userPost Specifies whether the {@link #myPostsList}(true) or the {@link #allPostsList}(false) will be changed.
     */
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

    /**
     * Resets the timeline to it's initial state.
     * @param userPost Specifies whether the {@link #myPostsList}(true) or the {@link #allPostsList}(false) will be changed.
     */
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

    /**
     * Adds an image to {@link #images} and throws a PropertyChange event for it.
     * @param key the post id to which the image belongs
     * @param img the actual image
     */
    public void addImage(String key, Bitmap img){
        images.put(key,img);
        propertyChangeSupport.firePropertyChange("image.load", key, img);
    }
    /**
     * @param key the post id to which the image belongs
     * @return an image from the {@link #images}
     */
    public Bitmap getImage(String key){
        return images.get(key);
    }

    public HashMap<String,Post> getMyPostsList() {
        return myPostsList;
    }

    /**
     * @param posts the HashMap that shall be soterd and converted to a list
     * @return Returns a list whith all posts orderd by Date
     */
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
