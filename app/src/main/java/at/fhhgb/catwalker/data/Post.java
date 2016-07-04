package at.fhhgb.catwalker.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.fhhgb.catwalker.firebase.Resources;

/**
 * Created by Lisa on 06.06.2016.
 */
public class Post {
    private String id;
    private String title;
    private String content;
    private Date dateTime;
    private String userId;
    private String universityId;

    //Todo: implement remaining
    private double longitude;
    private double latitude;
    private boolean hasImage;
    private boolean isExpanded;

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.ENGLISH);

    public Post() {
        this.setTitle("");
        this.setContent("");
        this.dateTime = new Date();
        this.setUserId("");
        this.setUniversityId("");
        this.isExpanded = false;
    }

    public Post(String title, String content, String dateTime, String userId, String universityId){
        this.setTitle(title);
        this.setContent(content);
        this.setDateTime(dateTime);
        this.setUserId(userId);
        this.setUniversityId(universityId);
    }


    public void setDateTime(String DateTime){
        DateFormat format = new SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.ENGLISH);
        try {
            this.dateTime = format.parse(DateTime);
            Log.d("Firebase-Test", "DateTime: "+ format.format(this.dateTime));
        } catch (ParseException e) {
            Log.d("Firebase-Test", "DATEPARSE ERROR");
        }
    }

    public String getDateTime(){
        return dateFormat.format(dateTime);
    }
    public static DateFormat getDateFormat(){
        return dateFormat;
    }

    @Override
    public String toString() {
        return getTitle() +" - "+getDateTime()+" ( "+ getUniversityId() +" )\n\n"+ getContent() +"\n";
    }

    public String getId(){
        return id;
    }
    public void setId(String key) {
        this.id = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return Resources.getLocalData().getUser(userId);
    }

    public String getUserId() {
        return userId;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
