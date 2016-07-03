package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import at.fhhgb.catwalker.R;


public class FragmentInfo extends Fragment {
    public FragmentInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        EditText edit = (EditText)v.findViewById(R.id.new_info_description);
        ;
        Log.i("COLOR", String.valueOf(edit.getCurrentHintTextColor()));
        // Inflate the layout for this fragment
        return v;
    }
}