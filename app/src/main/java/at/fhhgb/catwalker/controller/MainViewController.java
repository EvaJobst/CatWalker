package at.fhhgb.catwalker.controller;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import at.fhhgb.catwalker.MainActivity;
import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.data.TimelineData;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;

/**
 * Created by Lisa on 16.06.2016.
 */
public class MainViewController implements PropertyChangeListener{
    MainActivity view;
    DataModel model;
    LocalData data;

    public MainViewController(MainActivity main){
        view = main;
        model = ServiceLocator.getDataHandler();
        data = model.getLocalData();
        data.addPropertyChangeListener(this);

        init();
    }

    public void init(){
        model.addUserChangeListener("1");
        model.addUniversityTimelineInitializationListeners( "1" );
        updateListview(null);
    }

    public void updateListview(TimelineData timelineData){
        String[] listViewItems = new String[]{"Timeline is loading..."};
        if(timelineData!=null)
            listViewItems = timelineData.toStringArray();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view,
                android.R.layout.simple_list_item_1, listViewItems);
        ListView listView = (ListView) view.findViewById(R.id.timelineListView);
        listView.setAdapter(adapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "timeline":
                updateListview((TimelineData)event.getNewValue());
                break;
        }
    }
}
