package at.fhhgb.catwalker.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.fhhgb.catwalker.fragment.FragmentInfo;
import at.fhhgb.catwalker.fragment.FragmentLocation;
import at.fhhgb.catwalker.fragment.FragmentPicture;
import at.fhhgb.catwalker.R;

public class NewEntryActivity extends AppCompatActivity {
    Fragment info, location, picture;
    public static ImageButton.OnClickListener entryListener;
    FragmentManager mgr = getFragmentManager();
    FragmentTransaction ft;
    public static Bitmap image;

    private String selectedImagePath = "";
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;
    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        info = new FragmentInfo();
        location = new FragmentLocation();
        picture = new FragmentPicture();

        entryListener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(v);
            }
        };

        ImageButton b = null;
        b = (ImageButton) findViewById(R.id.new_btn_info);
        b.setOnClickListener(entryListener);

        setFragment(b);

        b = (ImageButton) findViewById(R.id.new_btn_location);
        b.setOnClickListener(entryListener);

        b = (ImageButton) findViewById(R.id.new_btn_picture);
        b.setOnClickListener(entryListener);

        b = (ImageButton) findViewById(R.id.new_btn_send);
        b.setOnClickListener(entryListener);


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
                if(!FragmentPicture.PICTURE) {
                    final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    //intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
                    startActivityForResult(intentCapture, CAPTURE_IMAGE);
                }

                ft.replace(R.id.new_fragment, picture, "fragmentPicture");
            } break;

            case R.id.new_btn_send : {
                Toast.makeText(NewEntryActivity.this, "Send", Toast.LENGTH_SHORT).show();
            } break;

            default : {
                ft.replace(R.id.new_fragment, info, "fragmentInfo");
            }
        }

        ft.commit();
        setColor(v);
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

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAPTURE_IMAGE) {
                selectedImagePath = getImagePath();
                FragmentPicture.iv.setImageBitmap(decodeFile(selectedImagePath));
            }

            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }
}