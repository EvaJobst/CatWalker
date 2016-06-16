package at.fhhgb.catwalker.controller;

import android.widget.EditText;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import at.fhhgb.catwalker.*;
import at.fhhgb.catwalker.data.*;
import at.fhhgb.catwalker.firebase.*;

/**
 * Created by Lisa on 14.06.2016.
 */
public class SettingsController implements PropertyChangeListener{
    SettingsActivity view;
    DataModel model;
    LocalData data;

    public SettingsController(SettingsActivity view){
        this.view = view;
        //ServiceLocator.register(LocalData.class, new Object[]{"1"});

        //ServiceLocator.register(DataModel.class);

        //model = ServiceLocator.getInstance(DataModel.class);
        model = ServiceLocator.getDataHandler();
        data = model.getLocalData();
        data.addPropertyChangeListener(this);
        init();
    }

    public void init(){
        //registers all needed database event listeners
        model.addUserChangeListener(data.getUserId());
    }

    public void cleanUp(){
        //removes the database event listeners
        model.removeUserListener(data.getUserId());
        //model.removeUniversityListener(data.getUniversityId());
    }

    public void updateUserView(String newName){
        EditText name =  null; //(EditText) view.findViewById( R.id.set_UserName);
        if(name.getText().toString() == "")
            name.setText(newName);
    }

    public void updateUserData(String name){
        model.updateUser(data.getUserId(), model.userNameSection , name);
    }

    //Todo: UniversitySettings & CatSettings

/*
    public void addPost(){
        String title = ((EditText) view.findViewById( R.id.postTitle)).getText().toString();
        String content = ((EditText) view.findViewById( R.id.postContent)).getText().toString();
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        Post post = new Post(title, content, date, data.getUserId(), data.getUniversityId());
        model.addPost(data.getUserId(), post);
    }
*/
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "user.name":
                String newValue = (String)event.getNewValue();
                updateUserView(newValue);
                break;
            case "user.universityId":

                break;
            case "university.name":

                break;
            case "university.cat":

                break;
        }
    }
}
