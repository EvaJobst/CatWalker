package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.controller.TimelineController;
import at.fhhgb.catwalker.data.TimelineData;

public class FragmentAllEntries extends Fragment {

    TimelineController controller;

    public FragmentAllEntries() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new TimelineController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_entries, container, false);
    }

}