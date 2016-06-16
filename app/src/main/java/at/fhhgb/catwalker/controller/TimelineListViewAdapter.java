package at.fhhgb.catwalker.controller;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

import at.fhhgb.catwalker.data.Post;

/**
 * Created by Lisa on 16.06.2016.
 */
public class TimelineListViewAdapter extends ArrayAdapter<Post> {


    public TimelineListViewAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
    }
}
