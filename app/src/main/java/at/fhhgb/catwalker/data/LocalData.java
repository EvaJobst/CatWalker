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
    private User user;

    //General Data
    private HashMap<String, String> userList;               //key, userName
    private HashMap<String, String> universityList;         //universityName, Cat

    //Timeline
    private TimelineData timelineData;

    //PropertyChangeSupport for raising propertyChange Events in case that some data has changed.
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public LocalData(){
        userList = new HashMap<>();
        universityList = new HashMap<>();
        timelineData = new TimelineData();
    }

    //----------------------------- PropertyChangeListener ------------------------------------//

    //Hooking a PropertyChangeListener in
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    //Removing a PropertyChangeListener in
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

    public User getUser(){
        return user;
    }

    public void setUser(User user) {
        if(user == null)
            return;

        if(this.user == null){
            this.user = user;
            return;
        }
        if(user.name.compareTo(this.user.name)!=0){
            updateUser(user.name);
        }
    }

    public void updateUser(String name){
        String old = "";
        if(user == null)
            user = new User(name);
        else{
            old = user.name;
            user.name=name;
        }
        propertyChangeSupport.firePropertyChange("user.name", old, name);
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

    public TimelineData getTimelineData() {
        return timelineData;
    }

    public void addPost(String key, Post p) {
        propertyChangeSupport.firePropertyChange("timeline.add", null, p);
        if(timelineData!=null)
            this.timelineData.add(key, p);
    }

    public void removePost(String key, Post p) {
        if(timelineData!=null)
            this.timelineData.remove(key);
        propertyChangeSupport.firePropertyChange("timeline.remove", p, null);
    }

    public void resetTimeline(){
        timelineData = new TimelineData();
        propertyChangeSupport.firePropertyChange("timeline.clear", null, null);
    }

    public void updatePost(String key, Post p) {
        if(timelineData!=null){
            Post old = this.timelineData.posts.get(key);
            this.timelineData.add(key, p);
            propertyChangeSupport.firePropertyChange("timeline.update", old , p);
        }
    }

}
