package at.fhhgb.catwalker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.RegisterActivity;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.User;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;

/**
 * Created by Lisa on 17.06.2016.
 */
public class RegisterController{

    RegisterActivity view;
    DataModel model;
    LocalData data;

    public RegisterController(RegisterActivity view){
        this.view = view;
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();
    }

    public boolean storePreferences(){
        String universityId = registerUniversity();
        String userId = registerUser();
        if(userId!= null && universityId != null) {
            SharedPreferences.Editor editor = view.getPreferences(Context.MODE_PRIVATE).edit();
            data.setUserId(userId);
            editor.putString("userId", userId);
            editor.putString("universityId", universityId);
            editor.commit();
            return true;
        }
        return false;
    }

    //returns true if a UserId was found
    public boolean restorePreferences(){
        SharedPreferences settings = view.getPreferences(Context.MODE_PRIVATE);
        String userId = settings.getString("userId", null);
        String universityId = settings.getString("universityId", null);
        if(userId !=null && universityId!=null) {
            data.setUserId(userId);
            data.setUniversityId(universityId);
            return true;
        }
        return false;
    }

    private String registerUniversity() {
        String universityId = null;
        EditText userName = (EditText) view.findViewById(R.id.registerUniversityText);
        String name = userName.getText().toString();
        if(name != "") {
            universityId = model.addUniversity(name);
        }
        return universityId;
    }

    //todo: add/choose university function
    private String registerUser() {
        String userId = null;
        EditText userName = (EditText) view.findViewById(R.id.registerNameText);
        String name = userName.getText().toString();
        if(name != "") {
            userId = model.addUser(new User(name));
        }
        return userId;
    }

}
