package at.fhhgb.catwalker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import at.fhhgb.catwalker.R;
import at.fhhgb.catwalker.activity.RegisterActivity;
import at.fhhgb.catwalker.data.LocalData;
import at.fhhgb.catwalker.data.User;
import at.fhhgb.catwalker.firebase.DataModel;
import at.fhhgb.catwalker.firebase.ServiceLocator;

/**
 * Created by Lisa on 17.06.2016.
 */
public class RegisterController implements PropertyChangeListener{

    RegisterActivity view;
    DataModel model;
    LocalData data;
    List<String> universityList;

    public RegisterController(RegisterActivity view){
        this.view = view;
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();

        //model.addUniversityChangeListener();
        data.addPropertyChangeListener(this);
    }

    public void initDependentListeners(){
        model.addTimelinePostChangeListener(data.getUniversityId());
        model.addUserChangeListener();
        model.addMyPostChangeListener(data.getUserId());
    }

    public boolean storePreferences(){
        String universityId = registerUniversity();
        String userId = registerUser();
        if(userId!= null && universityId != null) {

            SharedPreferences.Editor editor = view.getSharedPreferences("CatWalker_Data",Context.MODE_PRIVATE).edit();
            data.setUserId(userId);
            data.setUniversityId(universityId);
            editor.putString("userId", userId);
            editor.putString("universityId", universityId);
            editor.apply();

            //initDependentListeners();

            return true;
        }
        return false;
    }

    //returns true if a UserId was found
    public boolean restorePreferences(){
        if(data.restorePreferences(view)){
            //initDependentListeners();
            return true;
        }
        return false;
    }

    private String registerUniversity() {
        String universityId = null;
        EditText universityName = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);
        EditText universityCat = (EditText) view.findViewById(R.id.registerCat);
        String name = universityName.getText().toString();
        String cat = universityCat.getText().toString();
        if(isInputValid()) {
            universityId = model.addUniversity(name,cat);
        }
        return universityId;
    }

    private boolean isInputValid(){
        EditText userName = (EditText) view.findViewById(R.id.registerNameText);
        AutoCompleteTextView universityName = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);
        EditText universityCat = (EditText) view.findViewById(R.id.registerCat);
        return !universityName.getText().toString().equals("") && !userName.getText().toString().equals("") && !universityCat.toString().equals("");
    }

    private String registerUser() {
        String userId = null;
        EditText userName = (EditText) view.findViewById(R.id.registerNameText);
        String name = userName.getText().toString();
        if(isInputValid()) {
            userId = model.addUser(new User(name));
        }
        return userId;
    }

    public void initUniversityList(){
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);

        universityList = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view,
                android.R.layout.simple_dropdown_item_1line, universityList);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);
                updateUniversityCat(autoCompleteTextView.getText().toString());
            }
        });
    }

    public void updateUniversityList(String key, boolean add){
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);
        if(add)
            universityList.add(key);
        else
            universityList.remove(key);
    }

    private void updateUniversityCat(String university) {
        EditText universityCat = (EditText) view.findViewById(R.id.registerCat);
        if(data.getUniversityList().containsKey(university)){
            universityCat.setText(data.getUniversityList().get(university));
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch(event.getPropertyName()){
            case "university":
                updateUniversityList((String)event.getNewValue(), true);
                break;
            case "university.add":
                updateUniversityList((String)event.getNewValue(), true);
                break;
            case "university.remove":
                updateUniversityList((String)event.getOldValue(), false);
                break;
            case "university.cat":
                updateUniversityCat((String)event.getNewValue());
                break;
        }
    }

    public void signIn() {
        ServiceLocator.getAuth().signIn(view);
    }
}
