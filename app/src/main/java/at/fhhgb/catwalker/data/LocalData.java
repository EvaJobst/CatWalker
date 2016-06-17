package at.fhhgb.catwalker.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;

/**
 * Created by Lisa on 08.06.2016.
 * Contains the locally stored Data.
 */
public class LocalData {
    private String userId;
    private String universityId;
    private User userData;
    private University university;
    private TimelineData timelineData;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public LocalData(String userId){
        this(userId, ServiceLocator.getDataModel());
    }

    public LocalData(){
        this("", ServiceLocator.getDataModel());
    }

    //registers all needed Listeners
    public LocalData(String userId, DataModel model){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public User getUser(){
        return userData;
    }

    public void updateUserData(String name){
        String old = "";
        if(userData==null)
            userData=new User(name);
        else{
            old = userData.name;
            userData.name=name;
        }
        propertyChangeSupport.firePropertyChange("user.name", old, name);
    }

    public void updateUserData(User user) {
        if(user == null)
            return;

        if(userData == null){
            userData = user;
            return;
        }
        if(user.name.compareTo(userData.name)!=0){
            updateUserData(user.name);
        }
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

    //------------------------------- University ----------------------------------------------//

    public void setUniversityName(String name) {
        propertyChangeSupport.firePropertyChange("university.name", university.getName(), name);
        university.setName(name);
    }

    public TimelineData getTimelineData() {
        return timelineData;
    }

    public void setTimelineData(TimelineData timelineData) {
        propertyChangeSupport.firePropertyChange("timeline", getTimelineData(), timelineData);
        this.timelineData = timelineData;
    }

    public void addPost(Post p) {
        propertyChangeSupport.firePropertyChange("timeline.add", null, p);
        if(timelineData!=null)
            this.timelineData.add(p);
    }

    public void removePost(Post p) {
        if(timelineData!=null)
            this.timelineData.remove(p);
        propertyChangeSupport.firePropertyChange("timeline.remove", p, null);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }
}
