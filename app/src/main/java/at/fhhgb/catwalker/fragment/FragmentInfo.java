package at.fhhgb.catwalker.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.activity.NewPostActivity;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.ServiceLocator;


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
        updateFields(v);

        EditText title = (EditText) v.findViewById(R.id.new_info_title);
        title.setText(NewPostActivity.title);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                NewPostActivity.title = s.toString();
            }
        });

        EditText content = (EditText) v.findViewById(R.id.new_info_description);
        content.setText(NewPostActivity.content);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                NewPostActivity.content = s.toString();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    /**
     * Shows Author and current Time
     * @param view
     */
    public void updateFields(View view){
        TextView author = (TextView)view.findViewById(R.id.new_info_author);

        String currentTime = Post.getDateFormat().format(new Date());
        TextView time = (TextView)view.findViewById(R.id.new_info_date);
        time.setText(currentTime);

        author.setText(ServiceLocator.getDataModel().getLocalData().getUser());
    }

    @Override
    public void onResume() {
        super.onResume();
        ServiceLocator.getDataModel().getLocalData().restorePreferences(getActivity());
        updateFields(getView());
    }
}