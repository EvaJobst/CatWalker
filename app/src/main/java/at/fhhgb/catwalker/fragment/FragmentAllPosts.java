package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import at.fhhgb.catwalker.PostAdapter;
import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.data.Post;

public class FragmentAllPosts extends Fragment {
    RecyclerView recyclerView;
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

        recyclerView = (RecyclerView)  v.findViewById(R.id.my_entries_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        PostAdapter adapter = new PostAdapter(allPosts);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

}