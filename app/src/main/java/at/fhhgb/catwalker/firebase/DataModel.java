package at.fhhgb.catwalker.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

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
    private ValueEventListener userListener, universityListener, catListener;
    private ChildEventListener timelineChildListener;

    public LocalData getLocalData() {
        return data;
    }

    public DataModel() {
        database = FirebaseDatabase.getInstance();
        data = new LocalData("1", this);
    }

    private DatabaseReference getRef(String section, String id) {
        return database.getReference(section).child(id);
    }

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

    public String addUniversity(String name) {
        DatabaseReference dataRef = database.getReference(universitySection);
        dataRef = dataRef.push();
        dataRef.setValue(name);
        return dataRef.getKey();
    }

    public void updateUniversity(String universityId, String name) {
        getRef(universitySection, universityId).setValue(name);
    }

    public void removeUniversity(String universityId) {
        getRef(universitySection, universityId).removeValue();
    }

    //----------------------------------Post Functions---------------------------------------//

    public String addPost(String userId, Post post) {
        DatabaseReference dataRef = database.getReference(postSection);
        dataRef = dataRef.push();
        dataRef.setValue(post);
        return dataRef.getKey();
    }

    //public void updatePost(String postId, Post post){}
    public void removePost(String userId, String postId) {
        getRef(postSection, postId).removeValue();
    }

    //-------------------------------- ValueChangeListener ---------------------------------//

    public void addUserChangeListener(String userId) {
        DatabaseReference userRef = getRef(userSection, userId);

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);

                Log.d(TAG, "Value is: " + name.toString());
                data.updateUserData(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        userRef.addValueEventListener(userListener);
    }

    public void addUniversityTimelineInitializationListeners(String universityId) {
        DatabaseReference postRef = database.getReference(postSection);

        Query postByUniversity = postRef.orderByChild("universityId").equalTo(universityId);
        //for the initialization
        postByUniversity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TimelineData timelineData = new TimelineData(dataSnapshot.getChildren());
                data.setTimelineData(timelineData);
                Log.d(TAG, "Value is: " + timelineData.toString());

                addPost(data.getUserId(), new Post("Hello","Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",Post.getDateFormat().format(new Date()), data.getUserId(), "1"));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //for updates
        addTimelinePostChangeListener(postByUniversity);
    }

    public void addTimelinePostChangeListener(Query databaseReference){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p = dataSnapshot.getValue(Post.class);
                data.addPost(p);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Todo: updateTimelineElement
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post p = dataSnapshot.getValue(Post.class);
                data.removePost(p);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void fetchPostDataOnce(String postId) {
          /* TODO : implement posts query */
        DatabaseReference postRef = getRef(postSection, postId);
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post p = dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void universityNameChangeListener(String universityId) {
        DatabaseReference userRef = getRef(universitySection, universityId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + name.toString());
                data.setUniversityName(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //--------------------------------- Remove Listener ------------------------------------//

    public void removeUserListener(String userId) {
        DatabaseReference dataRef = getRef(userSection,userId);
        dataRef.removeEventListener(userListener);
    }

    public void removeUniversityListener(String userId) {
        DatabaseReference dataRef = getRef(userSection,userId);
        dataRef.removeEventListener(userListener);
    }

    public void removeTimelineChildListener() {
        DatabaseReference dataRef = database.getReference(postSection);
        dataRef.removeEventListener(timelineChildListener);
    }

}
