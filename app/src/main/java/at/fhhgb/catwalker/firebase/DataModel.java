package at.fhhgb.catwalker.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;

import at.fhhgb.catwalker.data.*;

/**
 * Created by Lisa on 06.06.2016.
 */
public class DataModel {
    private final String TAG = "DataModel";

    public final String userSection = "User";
    public final String userNameSection = "name";
    public final String universityIdSection = "universityId";
    public final String postSection = "Post";
    public final String universitySection = "University";

    public final String storagePath = "gs://project-1854836955410737173.appspot.com";

    private LocalData data;

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private ValueEventListener userListener;
    private ChildEventListener userChildListener, timelineChildListener, userPostChildListener, universityChildListener;

    public LocalData getLocalData() {
        return data;
    }
    public void setLocalData(LocalData localData) {
        data = localData;
    }


    private DatabaseReference getRef(String section, String id) {
        return database.getReference(section).child(id);
    }


    public DataModel() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        data = new LocalData();
        initListeners();
    }

    //----------------------------------------------------------------------------------------//
    //                            Read from Database Functions                                //
    //----------------------------------------------------------------------------------------//

    //----------------------------- Listener Initialization ----------------------------------//
    public void initListeners(){
        //--------------------------- User -------------------------------//
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);

                Log.d(TAG, "Value is: " + name.toString());
                data.updateUser(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        //---------------------- UniversitySection ------------------------------//
        userChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                data.updateUserList(dataSnapshot.getKey(), dataSnapshot.child("name").getValue(String.class), true);
            }

            @Override

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                data.updateUserList(dataSnapshot.getKey(), dataSnapshot.child("name").getValue(String.class), true);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                data.updateUserList(dataSnapshot.getKey(), dataSnapshot.child("name").getValue(String.class), false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };

        //---------------------- UniversitySection ------------------------------//
        universityChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                data.updateUniversityList(dataSnapshot.getKey(), dataSnapshot.getValue(String.class), true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                data.updateUniversityList(dataSnapshot.getKey(), dataSnapshot.getValue(String.class), true);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                data.updateUniversityList(dataSnapshot.getKey(), dataSnapshot.getValue(String.class), false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };

        //------------------------- Timeline ------------------------------//

        timelineChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                data.addPost(key, p, false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Post p = dataSnapshot.getValue(Post.class);
                //String key = dataSnapshot.getKey();
                //data.updatePost(key, p);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Post p = dataSnapshot.getValue(Post.class);
                //String key = dataSnapshot.getKey();
                //data.removePost(key,p, false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };

        userPostChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                data.addPost(key, p, true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                data.updatePost(key, p);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post p = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                data.removePost(key,p, false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };
    }

    //---------------------------------- Add Change Listener ---------------------------------//

    public void addUserChangeListener(String userId) {
        DatabaseReference userRef = database.getReference(userSection);
        userRef.addChildEventListener(userChildListener);
    }

    public void addTimelinePostChangeListener(String universityId){
        DatabaseReference postRef = database.getReference(postSection);
        Query postByUniversity = postRef.orderByChild("universityId").equalTo(universityId);
        postByUniversity.addChildEventListener(timelineChildListener);
    }

    public void addUserPostChangeListener(String universityId){
        DatabaseReference postRef = database.getReference(postSection);
        Query postByUser = postRef.orderByChild("userId").equalTo(data.getUserId());
        postByUser.addChildEventListener(userPostChildListener);
    }

    public void addUniversityChangeListener() {
        DatabaseReference universityRef = database.getReference(universitySection);
        universityRef.addChildEventListener(universityChildListener);
    }

    //--------------------------------- Remove Listener ------------------------------------//

    public void removeUserListener(String userId) {
        DatabaseReference dataRef =  database.getReference(userSection);
        dataRef.removeEventListener(userChildListener);
    }

    public void removeUniversityListener(String universityId) {
        DatabaseReference dataRef = getRef(userSection,universityId);
        dataRef.removeEventListener(universityChildListener);
    }

    public void removeTimelineChildListener(String universityId) {
        DatabaseReference postRef = database.getReference(postSection);
        Query postByUniversity = postRef.orderByChild("universityId").equalTo(universityId);
        postByUniversity.removeEventListener(timelineChildListener);
    }

    public void removeAllListeners(String userId, String universityId){
        removeUserListener(userId);
        removeUniversityListener(universityId);
        removeTimelineChildListener(universityId);
    }

    //-----------------------------------User Functions---------------------------------------//

    //returns the key of the newly inserted user
    public String addUser(User user) {
        DatabaseReference dataRef = database.getReference(userSection);
        dataRef = dataRef.push();
        dataRef.setValue(user);
        return dataRef.getKey();
    }

    public void updateUser(String userId, String key, String value) {
        getRef(userSection, userId).child(key).setValue(value);
    }

    public void removeUser(String userId) {
        getRef(userSection, userId).removeValue();
    }

    //-------------------------------University Functions------------------------------------//

    public String addUniversity(String name, String cat) {
        if(cat=="")
            cat = "cat";
        DatabaseReference dataRef = getRef(universitySection,name);
        //dataRef = dataRef.push();
        dataRef.setValue(cat);
        return dataRef.getKey();
    }

    public void updateUniversity(String universityId, String cat) {
        getRef(universitySection, universityId).setValue(cat);
    }

    public void removeUniversity(String universityId) {
        getRef(universitySection, universityId).removeValue();
    }

    //----------------------------------Post Functions---------------------------------------//

    public String addPost(Post post) {
        DatabaseReference dataRef = database.getReference(postSection);
        dataRef = dataRef.push();
        dataRef.child("title").setValue(post.getTitle());
        dataRef.child("content").setValue(post.getContent());
        dataRef.child("latitude").setValue(post.getLatitude());
        dataRef.child("longitude").setValue(post.getLongitude());
        dataRef.child("dateTime").setValue(post.getDateTime());
        dataRef.child("userId").setValue(data.getUserId());
        dataRef.child("universityId").setValue(data.getUniversityId());

        dataRef.child("hasImage").setValue(post.getHasImage());

        return dataRef.getKey();
    }

    //todo: public void updatePost(String postId, Post post){}

    public void removePost(String userId, String postId) {
        getRef(postSection, postId).removeValue();
    }

    //----------------------------------Image Functions---------------------------------------//

    public void addImage(Bitmap img, String fileName){
        if(img != null) {
            StorageReference imgRef = storage.getReferenceFromUrl(storagePath).child("image/img" + fileName + ".jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imgRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }
    }

    public void loadImage(final String fileName){
        StorageReference imgRef = storage.getReferenceFromUrl(storagePath).child("image/img" + fileName + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                data.addImage(fileName, img);

                Log.d("Image","Loading Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("Image","Loading Error");
            }
        });
    }
}
