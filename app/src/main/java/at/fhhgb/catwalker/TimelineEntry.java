package at.fhhgb.catwalker;

import android.view.View;
import android.widget.Toast;

/**
 * Created by Eva on 29.05.2016.
 */
public class TimelineEntry {
    protected String timelineTitle;
    protected String timelineDescription;
    protected int imageId;

    public TimelineEntry(String t, String d, int i) {
        timelineTitle = t;
        timelineDescription = d;
        imageId = i;
    }

    public String getTimelineTitle() {
        return timelineTitle;
    }

    public String getTimelineDescription() {
        return timelineDescription;
    }

    public int getImageId() {
        return imageId;
    }

    public String toString() {
        return timelineTitle + "/" + timelineDescription;
    }
}

