package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhhgb.catwalker.R;

public class FragmentAllEntries extends Fragment {
    RecyclerView recyclerView;

    public FragmentAllEntries() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_entries, container, false);

        recyclerView = (RecyclerView)  v.findViewById(R.id.my_entries_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        // Inflate the layout for this fragment
        return v;
    }

}