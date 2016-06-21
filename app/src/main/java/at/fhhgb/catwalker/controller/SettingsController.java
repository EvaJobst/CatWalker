package at.fhhgb.catwalker.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;
import android.widget.EditText;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Objects;

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
    EditTextPreference userNamePreference;
    ListPreference universityPreference;
    CharSequence[] keys, values;

    public SettingsController(){
        model = ServiceLocator.getDataModel();
        data = model.getLocalData();
        data.addPropertyChangeListener(this);
        model.addUniversityChangeListener();
        model.addUniversityNameChangeListener(data.getUniversityId());
    }

    public void setView(SettingsActivity view){
        this.view = view;
    }

    public void bindUsernameToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        if(data.getUser()!=null)
            preferenceChangeListener.onPreferenceChange(preference, data.getUser().name);

        userNamePreference =  (EditTextPreference) preference;

    }

    public void bindUniversityListToValue(ListPreference preference) {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preference.setOnPreferenceClickListener(preferenceClickListener);

        universityPreference = preference;
        updateUniversityListView();
    }

    private Preference.OnPreferenceClickListener preferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            boolean isHandled = false;

            if(preference.getKey().equals("settings_username")){
                if(data.getUser()!=null )
                    userNamePreference.getEditText().setText(data.getUser().name);
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
                int i= 1;
                data.setUniversityId((String) newValue);
                SharedPreferences.Editor pref = view.getPreferences(Context.MODE_PRIVATE).edit();
                pref.putString("universityId", data.getUniversityId());
                pref.commit();
                model.fetchTimelineByUniversityOnce(data.getUniversityId());
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
            case "user.name":
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
