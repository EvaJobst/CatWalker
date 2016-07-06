package at.fhhgb.catwalker.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        database.setPersistenceEnabled(true);
        storage = FirebaseStorage.getInstance();
        data = new LocalData();
        initListeners();
    }

    //----------------------------------------------------------------------------------------//
    //                            Read from Database Functions                                //
    //----------------------------------------------------------------------------------------//

    //----------------------------- Listener Initialization ----------------------------------//

    /**
     * This function does not attach the listeners to database references it only initializes their functionality.
     */
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

    /**
     * This function adds a userChangeListener to the user database reference.
     */
    public void addUserChangeListener() {
        DatabaseReference userRef = database.getReference(userSection);
        userRef.keepSynced(true);
        userRef.addChildEventListener(userChildListener);
    }

    /**
     * This function adds a timelineChildListener to a database query where all posts with the given universityId.
     * @param universityId specifies the university
     */
    public void addTimelinePostChangeListener(String universityId){
        DatabaseReference postRef = database.getReference(postSection);
        postRef.keepSynced(true);
        Query postByUniversity = postRef.orderByChild("universityId").equalTo(universityId);
        postByUniversity.addChildEventListener(timelineChildListener);
    }

    /**
     * This function adds a addMyPostChangeListener to a database query where all posts are selected that were created by the current userId.
     */
    public void addMyPostChangeListener(){
        DatabaseReference postRef = database.getReference(postSection);
        Query postByUser = postRef.orderByChild("userId").equalTo(data.getUserId());
        postByUser.keepSynced(true);
        postByUser.addChildEventListener(userPostChildListener);
    }

    /**
     * This function adds the universityChildListener to the university database reference.
     */
    public void addUniversityChangeListener() {
        DatabaseReference universityRef = database.getReference(universitySection);
        universityRef.keepSynced(true);
        universityRef.addChildEventListener(universityChildListener);
    }

    /**
     * This function attaches all listeners to the database.
     * This includes the following:
     *   - {@link #addUniversityChangeListener()}
     *   - {@link #addTimelinePostChangeListener(String)}
     *   - {@link #addUserChangeListener()}
     *   - {@link #addMyPostChangeListener()}
     * @param universityId defines the university for the timeline
     */
    public void addAllListeners(String universityId){
        addUserChangeListener();
        addUniversityChangeListener();
        addTimelinePostChangeListener(universityId);
        addMyPostChangeListener();
    }

    //--------------------------------- Remove Listener ------------------------------------//

    private void removeUserListener() {
        DatabaseReference dataRef =  database.getReference(userSection);
        dataRef.removeEventListener(userChildListener);
    }

    private void removeUniversityListener(String universityId) {
        DatabaseReference dataRef = getRef(userSection,universityId);
        dataRef.removeEventListener(universityChildListener);
    }
    public void removeTimelineChildListener(String universityId) {
        DatabaseReference postRef = database.getReference(postSection);
        Query postByUniversity = postRef.orderByChild("universityId").equalTo(universityId);
        postByUniversity.removeEventListener(timelineChildListener);
    }
    private void removeMyPostsChildListener(String userId) {
        DatabaseReference postRef = database.getReference(postSection);
        Query postByUniversity = postRef.orderByChild("userId").equalTo(userId);
        postByUniversity.removeEventListener(timelineChildListener);
    }

    /**
     * This function removes all listeners frome the database.
     * This includes the following:
     *   - {@link #removeUniversityListener(String)}
     *   - {@link #removeTimelineChildListener(String)}
     *   - {@link #removeUserListener()}
     *   - {@link #removeMyPostsChildListener(String)}
     * @param universityId defines the university for the timeline
     */
    public void removeAllListeners(String userId, String universityId){
        removeUserListener();
        removeUniversityListener(universityId);
        removeTimelineChildListener(universityId);
        removeMyPostsChildListener(userId);

    }

    //-----------------------------------User Functions---------------------------------------//

    /**
     * Creates a new User.
     * @param user the new user
     * @return Returns the key of the newly created user.
     */
    public String addUser(User user) {
        DatabaseReference dataRef = database.getReference(userSection);
        dataRef = dataRef.push();
        dataRef.setValue(user);
        return dataRef.getKey();
    }

    /**
     * Updates one of the fields of the user.
     * @param userId the user that will be updated
     * @param key the actual field within the user that will be changed
     * @param value the new value
     */
    public void updateUser(String userId, String key, String value) {
        getRef(userSection, userId).child(key).setValue(value);
    }

    public void removeUser(String userId) {
        getRef(userSection, userId).removeValue();
    }

    //-------------------------------University Functions------------------------------------//

    /**
     * Creates a new University
     * @param name The university name
     * @param cat The cat name
     * @return returns the key
     */
    public String addUniversity(String name, String cat) {
        if(cat=="")
            cat = "cat";
        DatabaseReference dataRef = getRef(universitySection,name);
        //dataRef = dataRef.push();
        dataRef.setValue(cat);
        return dataRef.getKey();
    }

    /**
     * Updates the university information
     * @param universityId the university that will be edited
     * @param cat   the new cat name
     */
    public void updateUniversity(String universityId, String cat) {
        getRef(universitySection, universityId).setValue(cat);
    }

    public void removeUniversity(String universityId) {
        getRef(universitySection, universityId).removeValue();
    }

    //----------------------------------Post Functions---------------------------------------//

    /**
     * Creates a new post.
     * @param post the post which will be added to the database
     * @return the key of the new post
     */
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

    /**
     * Stores an image in the image folder of the firebase storage.
     * @param img the new image
     * @param fileName the filename
     */
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

    /**
     * Loads an image from the firebase storage.
     * @param fileName the key of the post where the image belongs to
     */
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
