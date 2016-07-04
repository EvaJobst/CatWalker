package at.fhhgb.catwalker.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import at.fhhgb.catwalker.data.Post;
import at.fhhgb.catwalker.firebase.Resources;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        info = new FragmentInfo();
        location = new FragmentLocation();
        picture = new FragmentPicture();

        ImageButton b;
        b = (ImageButton) findViewById(R.id.new_btn_info);
        b.setOnClickListener(this);

        setFragment(b);

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
                    final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, picture.setImageUri());
                    startActivityForResult(intentCapture, picture.CAPTURE_IMAGE);
                }

                ft.replace(R.id.new_fragment, picture, "fragmentPicture");
            } break;

            case R.id.new_btn_send : {
                Post post = new Post();

                EditText editTitle = (EditText)findViewById(R.id.new_info_title);
                EditText editContent = (EditText)findViewById(R.id.new_info_description);

                if(editTitle != null)
                    post.setTitle(editTitle.getText().toString());
                if(editContent != null)
                    post.setContent(editContent.getText().toString());


                //Todo: insert google maps coordinates
                post.setLatitude(48.315782);
                post.setLongitude(14.285175);

                if(post.getTitle().isEmpty() && post.getContent().isEmpty()){
                    Toast.makeText(NewPostActivity.this, "There's nothing to send here.", Toast.LENGTH_SHORT).show();
                }else {
                    finish();
                    Toast.makeText(NewPostActivity.this, "Send", Toast.LENGTH_SHORT).show();
                    //push post to database && store the image

                    if(picture.image!=null)
                    {
                        post.setHasImage(true);
                        String key = Resources.getDataModel().addPost(post);
                        Resources.getDataModel().addImage(picture.image, key);
                    }else{
                        post.setHasImage(false);
                        Resources.getDataModel().addPost(post);
                    }
                }

            } break;

            default : {
                ft.replace(R.id.new_fragment, info, "fragmentInfo");
            }
        }

        ft.commit();
        setColor(v);
    }

    public void setColor(View v) {
        if(v.getId() != R.id.new_btn_send) {
            View info = findViewById(R.id.new_btn_info);
            info.setBackgroundColor(0);

            View location = findViewById(R.id.new_btn_location);
            location.setBackgroundColor(0);

            View picture = findViewById(R.id.new_btn_picture);
            picture.setBackgroundColor(0);

            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        picture.onActivityResult(requestCode, resultCode, data);
    }
}