package com.example.android.newsapp.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.example.android.newsapp.R;
import com.example.android.newsapp.SettingsActivity;
import com.example.android.newsapp.data.Section;
import com.example.android.newsapp.helper.Config;
import com.example.android.newsapp.helper.Utils;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends PreferenceFragmentCompat implements android.support.v7.preference.Preference.OnPreferenceChangeListener {



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        Context context = getContext();
        createSettings(context);
    }

    private void bindPreferenceSummaryToValue(Context context, Preference preference, String key, String initialValue) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(key, initialValue);
        onPreferenceChange(preference, value);
    }

    private void createSettings(final Context context) {

        //Create the pref screen
        PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(context);

        //Create a category
        PreferenceCategory category = new PreferenceCategory(context);
        preferenceScreen.addPreference(category);

        category.addPreference(createEditPreference(context));
        category.addPreference(createListPreference(context));

        setPreferenceScreen(preferenceScreen);
    }

    /**
     * Create ListPreferences
     *
     * @param context Act Context
     * @return ListPreference
     */
    private Preference createListPreference(Context context) {

        ListPreference listPreference = new ListPreference(context);
        listPreference.setTitle(R.string.newssection);
        listPreference.setKey(Config.LISTKEY);
        listPreference.setOnPreferenceChangeListener(this);



        List<String> sectionReadable = new ArrayList<>();
        List<String> sectionID = new ArrayList<>();

        List<Section> sectionList = Utils.getSavedSections(context);
        for(int i = 0; i < sectionList.size(); i++){

            Section section = sectionList.get(i);
            sectionReadable.add(section.getWebTitle());
            sectionID.add(section.getSectionId());
        }


        String[] entries = sectionReadable.toArray(new String[sectionReadable.size()]);//Lesbar
        String[] entryValues = sectionID.toArray(new String[sectionID.size()]);//Werte für url gebrauch

        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);

        listPreference.setEntries(entries);
        listPreference.setEntryValues(entryValues);

        bindPreferenceSummaryToValue(context, listPreference, Config.LISTKEY, Config.INITIALSECTION);

        return listPreference;
    }

    /**
     * Create EditPreferences
     * @param context Act Context
     * @return EditPreference
     */
    private Preference createEditPreference(Context context) {
        EditTextPreference limit = new EditTextPreference(context);
        limit.setKey(Config.LIMITKEY);
        limit.setDialogLayoutResource(R.layout.layout);
        limit.setTitle(R.string.newslimit);
        limit.setOnPreferenceChangeListener(this);
        bindPreferenceSummaryToValue(context,limit, Config.LIMITKEY, Config.INITIALLIMIT);
        return limit;
    }


    /**
     * Called when Preferences changed
     * @param preference
     * @param newValue New input value
     * @return boolean
     */
    @Override
    public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            if(index >= 0){
                CharSequence[] labels = listPreference.getEntries();
                preference.setSummary(labels[index]);
            }
        }else {
            preference.setSummary(stringValue);
        }

        return true;
    }
}
