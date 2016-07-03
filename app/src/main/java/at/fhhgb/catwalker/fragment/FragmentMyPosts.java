package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import at.fhhgb.catwalker.PostAdapter;
import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.ServiceLocator;


public class FragmentMyPosts extends Fragment implements PropertyChangeListener {
    RecyclerView recyclerView;
    List<Post> myPosts;

    public FragmentMyPosts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all_posts, container, false);

        recyclerView = (RecyclerView)  v.findViewById(R.id.all_entries_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ServiceLocator.getDataModel().getLocalData().addPropertyChangeListener(this);
        myPosts = new ArrayList<>();
        PostAdapter adapter = new PostAdapter(myPosts);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    private void updateTimeline(Post newValue, boolean add){
        if(add)
            this.myPosts.add(0,newValue);
        else
            myPosts.remove(newValue);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "myPosts.add":
                updateTimeline((Post)event.getNewValue(), true);
                Log.d("Add Post", ""+((Post)event.getNewValue()));
                break;
            case "myPosts.remove":
                updateTimeline((Post)event.getOldValue(), false);
                Log.d("Remove Post", ""+((Post)event.getOldValue()));
                break;
            case "myPosts.clear":
                myPosts.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d("Clear Posts", ""+((Post)event.getNewValue()));
                break;
        }
    }
}
