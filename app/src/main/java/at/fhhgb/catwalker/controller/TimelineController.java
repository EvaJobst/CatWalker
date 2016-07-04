package at.fhhgb.catwalker.controller;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.Resources;
import at.fhhgb.catwalker.fragment.FragmentAllPosts;

/**
 * Created by Lisa on 16.06.2016.
 */
public class TimelineController implements PropertyChangeListener{
    FragmentAllPosts view;
    DataModel model;
    LocalData data;
    ArrayAdapter<String> listViewAdapter;

    public TimelineController(FragmentAllPosts main){
        view = main;
        model = Resources.getDataModel();
        data = Resources.getLocalData();

        data.addPropertyChangeListener(this);
        initListview();
    }

    public void initListview(){
        /*
        List<String> listViewItems = new ArrayList<String>();

        Post p = new Post("Title", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", Post.getDateFormat().format( new Date()), data.getUserId(), data.getUniversityId());
       // model.addPost(data.getUserId(), p);

        if(data.getTimelineData()!=null)
         //   listViewItems = data.getTimelineData().toStringList();

        listViewAdapter = new ArrayAdapter<String>(view.getActivity(),
                android.R.layout.simple_list_item_1, listViewItems);
        //ListView listView = (ListView) view.getActivity().findViewById(R.id.allEntriesListView);
        listView.setAdapter(listViewAdapter);
        */
    }

    public void updateListview(Post p, boolean add){
        /*
        if(listViewAdapter.getCount()==1){
            listViewAdapter.remove("Timeline is loading...");
        }
        if(p!=null && add)
            listViewAdapter.add(p.toString());
        else if(p!=null)
            listViewAdapter.remove(p.toString());

        */
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "timeline.add":
                updateListview((Post)event.getNewValue(), true);
                break;
            case "timeline.remove":
                updateListview((Post)event.getOldValue(), false);
                Log.d("Remove Post", ""+event.getOldValue());
                break;
            case "timeline.update":
                initListview();
                break;
            case "timeline.clear":
                initListview();
                break;
        }
    }

}
