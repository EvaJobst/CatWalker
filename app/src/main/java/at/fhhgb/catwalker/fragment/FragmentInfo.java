package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.Resources;


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

        TextView author = (TextView)v.findViewById(R.id.new_info_author);

        String currentTime = Post.getDateFormat().format(new Date());
        TextView time = (TextView)v.findViewById(R.id.new_info_date);
        time.setText(currentTime);

        author.setText(Resources.getLocalData().getUser());
        Log.i("COLOR", String.valueOf(edit.getCurrentHintTextColor()));
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        String currentTime = Post.getDateFormat().format(new Date());
        TextView time = (TextView)getActivity().findViewById(R.id.new_info_date);
        time.setText(currentTime);
    }
}