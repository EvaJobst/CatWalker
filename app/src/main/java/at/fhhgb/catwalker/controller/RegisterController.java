package at.fhhgb.catwalker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
 * This class contains the business logic for a RegistrationActivity.
 */
public class RegisterController implements PropertyChangeListener{

    DataModel model;
    LocalData data;
    /**
     * Stores a List with all university names for the autocomplete Textview.
     */
    List<String> universityList;
    /**
     * Contains a reference to the activity that is updated by this controller.
     */
    RegisterActivity view;


    /**
     * Initializes the controller and starts the required database listeners.
     * Furthermore it registers this controller as a PropertyChangeListener for the LocalData instance.
     * @param view The activity that will be managed by this controller.
     */
    public RegisterController(RegisterActivity view){
        this.view = view;
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();

        model.addUniversityChangeListener();
        model.addUserChangeListener();
        data.addPropertyChangeListener(this);
    }

    /**
     * Initializes all listeners that are dependent on user data (UserId and UniversityId)
     */
    public void initDependentListeners(){
        model.addTimelinePostChangeListener(data.getUniversityId());
        model.addMyPostChangeListener();
    }

    /**
     * Calls {@link #registerUniversity()} and {@link #registerUser()} ()} to push the userdata to firebase.
     * Stores the new userdata as SharedPreferences if the previouse function calls were successful.
     * @return returns true if the user could be created and false if it wasn't.
     */
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

            initDependentListeners();

            return true;
        }
        return false;
    }

    /**
     * This function attempts to restore previously saved user data from the Shared Preferences
     * @return Returns true if user data was found.
     */
    public boolean restorePreferences(){
        if(data.restorePreferences(view)){
            initDependentListeners();
            return true;
        }
        return false;
    }

    /**
     * Attempts to store the University in Firebase.
     * @return Returns the Name of the university. If the input was invalid it returns null.
     */
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

    /**
     * Attempts to store the User in Firebase.
     * @return Returns the key where the user is stored. If the input was invalid it returns null.
     */
    private String registerUser() {
        String userId = null;
        EditText userName = (EditText) view.findViewById(R.id.registerNameText);
        String name = userName.getText().toString();
        if(isInputValid()) {
            userId = model.addUser(new User(name));
        }
        return userId;
    }

    /**
     * Checks whether all fields are correctly filled.
     * @return True if the input is correct, otherwise false.
     */
    private boolean isInputValid(){
        EditText userName = (EditText) view.findViewById(R.id.registerNameText);
        AutoCompleteTextView universityName = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);
        EditText universityCat = (EditText) view.findViewById(R.id.registerCat);
        return !universityName.getText().toString().equals("") && !userName.getText().toString().equals("") && !universityCat.toString().equals("");
    }

    /**
     * Initializes the AutoCompleteTextView with a new ArrayAdapter. The data is taken from {@link #universityList}.
     * It also adds a TextChangedListener with updates the cat name if an already existing university is entered.
     */
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

    /**
     * This function updates {@link #universityList} whenever new data arrives.
     * @param key University name
     * @param add Adds the university to the list if true, otherwise removes it.
     */
    public void updateUniversityList(String key, boolean add){
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.registerUniverstiyAutoComplete);
        if(add)
            universityList.add(key);
        else
            universityList.remove(key);
        Log.d("register","university.add: "+key);
    }

    /**
     * Updates the cat name in the activity if the university name was valid.
     * @param university The university for which the cat will be searched.
     */
    private void updateUniversityCat(String university) {
        EditText universityCat = (EditText) view.findViewById(R.id.registerCat);
        if(data.getUniversityList().containsKey(university)){
            universityCat.setText(data.getUniversityList().get(university));
        }
    }

    /**
     * The PropertyChangeEventListener which reacts on university and cat specific changes with specific calls of {@link #updateUniversityList(String, boolean)} or  {@link #updateUniversityCat(String)}} .
     * @param event
     */
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

    /**
     * Starts the Firebase Authentication process.
     */
    public void signIn() {
        ServiceLocator.getAuth().signIn(view);
    }
}
