package at.fhhgb.catwalker.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private HashMap<String, Post> userPostsList;             //key, Post


    //PropertyChangeSupport for raising propertyChange Events in case that some data has changed.
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public LocalData(){
        userList = new HashMap<>();
        universityList = new HashMap<>();
        allPostsList = new HashMap<>();
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
    public String getUser(){
        return userList.get(userId);
    }

    //returns any user
    public String getUser(String userId){
        return userList.get(userId);
    }

    public void updateUser(String name){
        String old = "";
        if(user == null)
            user = name;
        else{
            old = user;
            user=name;
        }
        propertyChangeSupport.firePropertyChange("user", old, name);
    }

    public void updateUserList(String key, String value, boolean add) {
        if(add)
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

    public void setUniversityList(HashMap<String,String> universityList) {
        this.universityList = universityList;
    }

    public HashMap<String,String> getUniversityList() {
        return universityList;
    }

    public void updateUniversityList(String key, String value, boolean add) {
        if(add){
            universityList.put(key,value);
            propertyChangeSupport.firePropertyChange("university.add", null, key);
        }else{
            universityList.remove(key);
            propertyChangeSupport.firePropertyChange("university.remove", key, null);
        }
    }

    //---------------------------------- Timeline --------------------------------------------//

    public void addPost(String key, Post p, boolean userPost) {
        propertyChangeSupport.firePropertyChange("timeline.add", null, p);
        if(userPost)
            this.userPostsList.put(key,p);
        else
            this.allPostsList.put(key, p);
    }

    public void removePost(String key, Post p, boolean userPost) {
        if(userPost)
            this.userPostsList.remove(key);
        else
            this.allPostsList.remove(key);
        propertyChangeSupport.firePropertyChange("timeline.remove", p, key);
        //Todo: remove function for the timeline
    }

    public void resetTimeline(boolean userPost){
        if(userPost)
            userPostsList = new HashMap<>();
        else
            allPostsList = new HashMap<>();
        propertyChangeSupport.firePropertyChange("timeline.clear", null, null);
    }

    public void updatePost(String key, Post p) {
        if(allPostsList!=null){
            Post old = this.allPostsList.get(key);
            this.allPostsList.put(key, p);
            propertyChangeSupport.firePropertyChange("timeline.update", old , p);
        }
    }


}
