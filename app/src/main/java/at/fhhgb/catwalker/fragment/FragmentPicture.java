package at.fhhgb.catwalker.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import at.fhhgb.catwalker.R;


public class FragmentPicture extends Fragment {
    public ImageView iv;
    public Bitmap image;
    private String selectedImagePath = "";
    final public int CAPTURE_IMAGE = 2;
    String imgPath;

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
                final Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(intentCapture, CAPTURE_IMAGE);
            }
        });

        iv = (ImageView) v.findViewById(R.id.new_picture_image);

        if (image != null) {
            iv.setImageBitmap(image);
        }

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAPTURE_IMAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedImagePath = getImagePath();
                    if (selectedImagePath!=null) {
                        image = decodeFile(selectedImagePath);
                        image = fixRotation(image, selectedImagePath);
                        iv.setImageBitmap(image);
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            final int REQUIRED_SIZE = 700;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            Log.d("Image", "not found!");
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap fixRotation(Bitmap img, String imagePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(getImagePath());
        } catch (IOException e) {
            Log.d("Image", "Can't read Exif data.");
        }
        if (exif != null) {
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            img = rotate(img, rotationInDegrees);

        } else {
            if (img.getWidth() > img.getHeight()) {
                img = rotate(img, 90);
            }
        }
        return img;
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap img, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return img;
    }
}