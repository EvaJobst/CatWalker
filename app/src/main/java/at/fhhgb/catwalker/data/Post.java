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
    public String title;
    public String content;
    private Date dateTime;
    public String userId;
    public String universityId;

    //Todo: implement remaining
    public String longitude;
    public String lattitude;
    public String image;
    public String catId;

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.ENGLISH);

    public Post() {
        this.title = "";
        this.content = "";
        this.dateTime = new Date();
        this.userId = "";
        this.universityId = "";
    }

    public Post(String title, String content, String dateTime, String userId, String universityId){
        this.title = title;
        this.content = content;
        this.setDateTime(dateTime);
        this.userId = userId;
        this.universityId = universityId;

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
        return title+" - "+getDateTime()+"\n\n"+content+"\n";
    }

    //todo: Better isEqual function
    public boolean isEqual(Post p){
        if ( this.toString().equals(p.toString()) && userId.equals(p.userId) && universityId.equals(p.universityId))
            return true;
        return false;
    }

}
