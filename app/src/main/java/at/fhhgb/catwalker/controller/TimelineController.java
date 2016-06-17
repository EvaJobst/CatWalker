package at.fhhgb.catwalker.controller;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.fhhgb.catwalker.TimelineActivity;
import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.data.TimelineData;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;

/**
 * Created by Lisa on 16.06.2016.
 */
public class TimelineController implements PropertyChangeListener{
    TimelineActivity view;
    DataModel model;
    LocalData data;
    ArrayAdapter<String> listViewAdapter;

    public TimelineController(TimelineActivity main){
        view = main;
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();

        data.addPropertyChangeListener(this);

        init();
    }

    public void init(){
        //Todo: Add Preferences for User and UniversityId
        model.addUserChangeListener(data.getUserId());

        model.addTimelinePostChangeListener(data.getUniversityId());
        initListview(null);
    }

    public void initListview(TimelineData timelineData){
        List<String> listViewItems = new ArrayList<String>();

        if(timelineData!=null)
            listViewItems = timelineData.toStringList();
        else
            listViewItems.add("Timeline is loading...");

        listViewAdapter = new ArrayAdapter<String>(view,
                android.R.layout.simple_list_item_1, listViewItems);
        ListView listView = (ListView) view.findViewById(R.id.timelineListView);
        listView.setAdapter(listViewAdapter);


    }

    public void updateListview(Post p, boolean add){
        if(listViewAdapter.getCount()==1){
            listViewAdapter.remove("Timeline is loading...");
        }
        if(p!=null && add)
            listViewAdapter.add(p.toString());
        else if(p!=null)
            listViewAdapter.remove(p.toString());
    }


    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "timeline":
                initListview((TimelineData)event.getNewValue());
                break;
            case "timeline.add":
                updateListview((Post)event.getNewValue(), true);
                break;
            case "timeline.remove":
                updateListview((Post)event.getOldValue(), false);
                Log.d("Remove Post", ""+((Post)event.getOldValue()));
                break;
        }
    }

}
