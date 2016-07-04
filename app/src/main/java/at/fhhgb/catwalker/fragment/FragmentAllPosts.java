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
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.Resources;

public class FragmentAllPosts extends Fragment implements PropertyChangeListener{
    RecyclerView recyclerView;
    LocalData data;
    List<Post> allPosts;

    public FragmentAllPosts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_posts, container, false);

        Resources.getLocalData().addPropertyChangeListener(this);
        this.allPosts = new ArrayList<>();

        recyclerView = (RecyclerView)  v.findViewById(R.id.my_entries_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        PostAdapter adapter = new PostAdapter(allPosts);
        recyclerView.setAdapter(adapter);


        // Inflate the layout for this fragment
        return v;

    }

    private void updateTimeline(Post newValue, boolean add){
        if(add)
            this.allPosts.add(0,newValue);
        else
            allPosts.remove(newValue);
        recyclerView.getAdapter().notifyItemChanged(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "timeline.add":
                updateTimeline((Post)event.getNewValue(), true);
                Log.d("Add Post", ""+event.getNewValue());
                break;
            case "timeline.remove":
                updateTimeline((Post)event.getOldValue(), false);
                Log.d("Remove Post", ""+event.getOldValue());
                break;
            case "timeline.clear":
                allPosts.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d("Clear Posts", ""+event.getNewValue());
                break;
        }
    }

}