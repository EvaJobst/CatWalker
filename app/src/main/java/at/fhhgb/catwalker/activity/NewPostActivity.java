package at.fhhgb.catwalker.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import at.fhhgb.catwalker.TypefaceSpan;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;
import at.fhhgb.catwalker.fragment.FragmentInfo;
import at.fhhgb.catwalker.fragment.FragmentLocation;
import at.fhhgb.catwalker.fragment.FragmentPicture;
import at.fhhgb.catwalker.R;

public class NewPostActivity extends AppCompatActivity implements ImageButton.OnClickListener {
    FragmentPicture picture;
    FragmentInfo info;
    FragmentLocation location;
    FragmentManager mgr = getFragmentManager();
    FragmentTransaction ft;
    public static Location loc;         // saves longitude and latitude values for positioning
    public static String title = "";    // saves title of post
    public static String content = "";  // saves description/content of post

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Custom Font for Title
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new TypefaceSpan(this, "Champagne_Limousines-Thick.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        // Fragments
        info = new FragmentInfo();
        location = new FragmentLocation();
        picture = new FragmentPicture();

        // Bottom Bar Buttons
        ImageButton b;
        b = (ImageButton) findViewById(R.id.new_btn_info);
        b.setOnClickListener(this);

        setFragment(b); // sets Info-Fragment as default Fragment

        b = (ImageButton) findViewById(R.id.new_btn_location);
        b.setOnClickListener(this);

        b = (ImageButton) findViewById(R.id.new_btn_picture);
        b.setOnClickListener(this);

        b = (ImageButton) findViewById(R.id.new_btn_send);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setFragment(v);
    }

    /**
     * Deals with Bottom Bar Icon-Clicks: </p>
     * - Performs Fragment-Transaction </p>
     * - Opens Camera application when no picture is available, if one is set it opens the Picture-Fragment </p>
     * - Sends post and saves values to database
     * @param v View that reacted to click-event
     */
    public void setFragment(View v) {
        ft = mgr.beginTransaction();

        switch(v.getId()) {
            case R.id.new_btn_info : {
                ft.replace(R.id.new_fragment, info, "fragmentInfo");
            } break;

            case R.id.new_btn_location : {
                ft.replace(R.id.new_fragment, location, "fragmentLocation");
            } break;

            case R.id.new_btn_picture : {
                if(picture.image == null) {
                    // Opens local camera application
                    final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, picture.setImageUri());
                    startActivityForResult(intentCapture, picture.CAPTURE_IMAGE);
                }

                ft.replace(R.id.new_fragment, picture, "fragmentPicture");
            } break;

            case R.id.new_btn_send : {
                Post post = new Post();

                if(loc == null) {
                    post.setLatitude(0);
                    post.setLongitude(0);
                }

                else {
                    post.setLatitude(loc.getLatitude());
                    post.setLongitude(loc.getLongitude());
                }

                // Control structure since title or description is required
                if(title.isEmpty() && content.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "There's nothing to send here.", Toast.LENGTH_SHORT).show();
                }else {
                    post.setTitle(title);
                    post.setContent(content);
                    title ="";
                    content="";
                    finish(); // closes Activity
                    Toast.makeText(NewPostActivity.this, "Send", Toast.LENGTH_SHORT).show();
                    //push post to database && store the image

                    if(picture.image!=null)
                    {
                        post.setHasImage(true);
                        String key = ServiceLocator.getDataModel().addPost(post);
                        ServiceLocator.getDataModel().addImage(picture.image, key);
                    }else{
                        post.setHasImage(false);
                        ServiceLocator.getDataModel().addPost(post);
                    }
                }

            } break;

            default : {
                ft.replace(R.id.new_fragment, info, "fragmentInfo");
            }
        }

        ft.commit();
        setColor(v); // changes background color of icon
    }

    /**
     * Changes colour of all icons to default colour </p>
     * Sets background colour of View v to orange
     * @param v View that reacted to click-event
     */
    public void setColor(View v) {
        if(v.getId() != R.id.new_btn_send) {
            ImageButton info = (ImageButton) findViewById(R.id.new_btn_info);
            info.setBackgroundColor(0);

            View location = findViewById(R.id.new_btn_location);
            location.setBackgroundColor(0);

            View picture = findViewById(R.id.new_btn_picture);
            picture.setBackgroundColor(0);

            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    /**
     * Calls onActivityResult from Fragment Picture </p>
     * Method is needed for Camera application
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        picture.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Catwalker","Restart NewPost");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        data.restorePreferences(this);
        data.resetTimeline(false);
        data.resetTimeline(true);
        model.addAllListeners(data.getUniversityId());
    }

    @Override
    protected void onStop() {
        Log.d("Catwalker","Stop New Post");
        DataModel model = ServiceLocator.getDataModel();
        LocalData data = model.getLocalData();
        model.removeAllListeners(data.getUserId(), data.getUniversityId());
        super.onStop();
    }
}