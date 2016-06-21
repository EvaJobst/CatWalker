package at.fhhgb.catwalker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewEntryActivity extends AppCompatActivity {
    Fragment info, location, picture;
    public static ImageButton.OnClickListener entryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        info = new FragmentInfo();
        location = new FragmentLocation();
        picture = new FragmentPicture();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        entryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(v);

                FragmentManager mgr = getFragmentManager();
                FragmentTransaction ft = mgr.beginTransaction();

                switch(v.getId()) {
                    case R.id.new_btn_info : {
                        ft.replace(R.id.new_fragment, info, "fragmentInfo");
                    } break;

                    case R.id.new_btn_location : {
                        ft.replace(R.id.new_fragment, location, "fragmentLocation");
                    } break;

                    case R.id.new_btn_picture : {
                        ft.replace(R.id.new_fragment, picture, "fragmentPicture");
                    } break;

                    case R.id.new_btn_send : {
                        Toast.makeText(NewEntryActivity.this, "Send", Toast.LENGTH_SHORT).show();
                    } break;

                    default : {}
                }

                ft.commit();
            }
        };

        ImageButton b = null;
        b = (ImageButton) findViewById(R.id.new_btn_info);
        b.setOnClickListener(entryListener);

        b = (ImageButton) findViewById(R.id.new_btn_location);
        b.setOnClickListener(entryListener);

        b = (ImageButton) findViewById(R.id.new_btn_picture);
        b.setOnClickListener(entryListener);

        b = (ImageButton) findViewById(R.id.new_btn_send);
        b.setOnClickListener(entryListener);
    }

    public void setColor(View v) {
        View info = findViewById(R.id.new_btn_info);
        info.setBackgroundColor(0);

        View location = findViewById(R.id.new_btn_location);
        location.setBackgroundColor(0);

        View picture = findViewById(R.id.new_btn_picture);
        picture.setBackgroundColor(0);

        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }
}