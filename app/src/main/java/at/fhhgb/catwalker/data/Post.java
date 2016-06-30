package at.fhhgb.catwalker.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lisa on 06.06.2016.
 */
public class Post {
    private String title;
    private String content;
    private Date dateTime;
    private String userId;
    private String universityId;

    //Todo: implement remaining
    private String longitude;
    private String latitude;
    private String image;

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.ENGLISH);

    public Post() {
        this.setTitle("");
        this.setContent("");
        this.dateTime = new Date();
        this.setUserId("");
        this.setUniversityId("");
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

    //todo: Better isEqual function
    public boolean isEqual(Post p){
        if ( this.toString().equals(p.toString()) && getUserId().equals(p.getUserId()) && getUniversityId().equals(p.getUniversityId()))
            return true;
        return false;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
