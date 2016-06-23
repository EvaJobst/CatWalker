package at.fhhgb.catwalker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.activity.NewEntryActivity;


public class FragmentPicture extends Fragment {
    public static ImageView iv;
    public static boolean PICTURE = false;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public FragmentPicture() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture, container, false);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.new_picture_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PICTURE = true;
            }
        });

        iv = (ImageView) v.findViewById(R.id.new_picture_image);

        //put bitmapimage in your imageview
        iv.setImageBitmap(NewEntryActivity.image);

        // Inflate the layout for this fragment
        return v;
    }
}