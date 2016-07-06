package at.fhhgb.catwalker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import at.fhhgb.catwalker.activity.SettingsActivity;
import at.fhhgb.catwalker.data.*;
import at.fhhgb.catwalker.firebase.*;

/**
 * This class contains the business logic for a SettingsActivity.
 */
public class SettingsController implements PropertyChangeListener{
    SettingsActivity view;
    DataModel model;
    LocalData data;
    EditTextPreference userNamePreference, catNamePreference;
    ListPreference universityPreference;
    /**
     * Stores the University names for the University ListPreference.
     */
    CharSequence[] values;

    public SettingsController(){
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();
        data.addPropertyChangeListener(this);
        model.addUniversityChangeListener();
    }

    public void setView(SettingsActivity view){
        this.view = view;
    }

    /**
     * Binds the userNamePreference to the {@link #preferenceChangeListener} and the {@link #preferenceClickListener}.
     * It also initializes it with the currently set user name.
     * @param preference user name EditTextPreference
     */
    public void bindUsernameToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        if(data.getUser()!=null)
            preferenceChangeListener.onPreferenceChange(preference, data.getUser());

        userNamePreference =  (EditTextPreference) preference;

    }
    /**
     * Binds the catNamePreference to the {@link #preferenceChangeListener} and the {@link #preferenceClickListener}.
     * It also initializes it with the currently set cat name.
     * @param preference cat name EditTextPreference
     */
    public void bindCatToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        if(data.getUniversityList()!=null)
            preferenceChangeListener.onPreferenceChange(preference, data.getUniversityList().get(data.getUniversityId()));
        catNamePreference =  (EditTextPreference) preference;

    }

    /**
     * Binds the catNamePreference to the {@link #preferenceChangeListener} and the {@link #preferenceClickListener}.
     * It also initializes it with a call of {@link #updateUniversityListView()}
     * @param preference university ListPreference
     */
    public void bindUniversityListToValue(ListPreference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        universityPreference = preference;
        universityPreference.setSummary(data.getUniversityId());
        updateUniversityListView();
    }

    /**
     * This Listener handles the click events for the three registered settings.
     */
    private Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            boolean isHandled = false;

            if(preference.getKey().equals("settings_username")){
                if(data.getUser()!=null )
                    userNamePreference.getEditText().setText(data.getUser());
                isHandled = true;
            }
            else if (preference.getKey().equals("settings_university")){
                isHandled = true;
            }
            else if (preference.getKey().equals("settings_cat")){
                if(data.getUniversityList()!=null )
                    catNamePreference.getEditText().setText(data.getUniversityList().get(data.getUniversityId()));
                isHandled = true;
            }

            return isHandled;
        }
    };

    /**
     * This Listener handles the PreferenceChange events for the three registered settings.
     * It is used to update database listeners and save the data.
     */
    private Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean isHandled = false;
            if(preference.getKey().equals("settings_username")){
                updateUserData(newValue.toString());
                isHandled=true;
            }else if (preference.getKey().equals("settings_university")){

                String newID = (String) newValue;

                //store preference
                SharedPreferences.Editor editor = view.getSharedPreferences("CatWalker_Data",Context.MODE_PRIVATE).edit();
                editor.putString("universityId", newID);
                editor.apply();
                //update TimelineChildListener
                model.removeTimelineChildListener(data.getUniversityId());
                model.addTimelinePostChangeListener(newID);
                data.resetTimeline(false);


                //update data
                data.setUniversityId(newID);



                isHandled = true;
            }else if (preference.getKey().equals("settings_cat")){
                model.updateUniversity(data.getUniversityId(), (String)newValue);

                isHandled = true;
            }
            return isHandled;
        }
    };

    public void updateUserData(String name){
        model.updateUser(data.getUserId(), model.userNameSection , name);
    }

    private void updateUniversityListView(){
        values = data.getUniversityList().keySet().toArray(new String[0]);
        //values = data.getUniversityList().values().toArray(new String[0]);

        if(values.length>0)
            universityPreference.setEntryValues(values);
        else
            universityPreference.setEntryValues(new CharSequence[]{"1"});
        if(values.length>0)
            universityPreference.setEntries(values);
        else
            universityPreference.setEntries(new CharSequence[]{"Loading..."});

    }

    /**
     * Handles university and user specific propertyChangeEvents
     * @param event
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()){
            case "user":
                String newValue = (String)event.getNewValue();
                break;
            case "university.add":
                updateUniversityListView();
                break;
            case "university.remove":
                updateUniversityListView();
                break;
            case "university.change":
                if(universityPreference!=null)
                    universityPreference.setSummary((String)event.getNewValue());
                break;
        }
    }
}
