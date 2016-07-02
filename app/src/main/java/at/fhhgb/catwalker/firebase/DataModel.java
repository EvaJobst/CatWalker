package at.fhhgb.catwalker.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    private LocalData data;

    private FirebaseDatabase database;
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

        timelineChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                String key = dataSnapshot.getKey();
                data.addPost(key, p, false);
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
    //----------------------------------------------------------------------------------------//
    //                             Write to Database Functions                                //
    //----------------------------------------------------------------------------------------//

    //stores any value in any place in the database
    public void saveToDatabase(String section, String[] keys, String val) {
        DatabaseReference dataRef = database.getReference(section);
        for (String key : keys) {
            dataRef = dataRef.child(key);
        }
        dataRef.setValue(val);
    }


    //-----------------------------------User Functions---------------------------------------//

    //returns the key of the newly inserted user
    public String addUser(User user) {
        DatabaseReference dataRef = database.getReference(userSection);
        dataRef = dataRef.push();
        dataRef.setValue(user);
        return dataRef.getKey();
    }

    public void updateUser(String userId, User user) {
        getRef(userSection, userId).setValue(user);
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

        String imagePath = "images/"+dataRef.getKey()+".jpg";
        dataRef.child("image").setValue(imagePath);

        //Todo: Upload image function
        //pushImage(imagePath);

        return dataRef.getKey();
    }

    //todo: public void updatePost(String postId, Post post){}

    public void removePost(String userId, String postId) {
        getRef(postSection, postId).removeValue();
    }

}
