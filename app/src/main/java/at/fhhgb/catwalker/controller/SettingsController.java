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
 * Created by Lisa on 14.06.2016.
 */
public class SettingsController implements PropertyChangeListener{
    SettingsActivity view;
    DataModel model;
    LocalData data;
    EditTextPreference userNamePreference;
    ListPreference universityPreference;
    CharSequence[] keys, values;

    public SettingsController(){
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();
        data.addPropertyChangeListener(this);
        model.addUniversityChangeListener();
    }

    public void setView(SettingsActivity view){
        this.view = view;
    }

    public void bindUsernameToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        if(data.getUser()!=null)
            preferenceChangeListener.onPreferenceChange(preference, data.getUser());

        userNamePreference =  (EditTextPreference) preference;

    }

    public void bindUniversityListToValue(ListPreference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        universityPreference = preference;
        universityPreference.setSummary(data.getUniversityId());
        updateUniversityListView();
    }

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

            return isHandled;
        }
    };

    private Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean isHandled = false;
            if(preference.getKey().equals("settings_username")){
                updateUserData(newValue.toString());
                isHandled=true;
            }else if (preference.getKey().equals("settings_university")){

                String newID = (String) newValue;

                //update TimelineChildListener
                model.removeTimelineChildListener(data.getUniversityId());
                model.addTimelinePostChangeListener(newID);
                data.resetTimeline();

                //update data
                data.setUniversityId(newID);

                //store preference
                SharedPreferences.Editor pref = view.getPreferences(Context.MODE_PRIVATE).edit();
                pref.putString("universityId", data.getUniversityId());
                pref.apply();


                isHandled = true;
            }
            return isHandled;
        }
    };


    public void updateUserData(String name){
        model.updateUser(data.getUserId(), model.userNameSection , name);
    }

    //Todo: UniversitySettings & CatSettings

    private void updateUniversityListView(){
        keys = data.getUniversityList().keySet().toArray(new String[0]);
        //values = data.getUniversityList().values().toArray(new String[0]);

        if(keys.length>0)
            universityPreference.setEntryValues(keys);
        else
            universityPreference.setEntryValues(new CharSequence[]{"1"});
        if(keys.length>0)
            universityPreference.setEntries(keys);
        else
            universityPreference.setEntries(new CharSequence[]{"Loading..."});

    }

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
