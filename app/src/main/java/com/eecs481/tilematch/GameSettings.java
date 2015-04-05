package com.eecs481.tilematch;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

public class GameSettings extends PreferenceActivity {

    private Preference pr; // Keeps track of user preferences

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();

        ActionBar topBar = getActionBar();
        topBar.setDisplayHomeAsUpEnabled(true);
        topBar.setIcon(android.R.color.transparent);

        pr = findPreference("select_picture");
        pr.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pictureButtonClick();
                return false;
            }
        });

        pr = findPreference("use_ask_library");
        pr.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                askButtonClick();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void pictureButtonClick() {
        Log.i("[btn]", "Clicked Picture button");
        startActivity(new Intent(this, PictureList.class));
    }

    public void askButtonClick() {
        // Temporary popup until we implement ASK Library
        Log.i("[btn]", "Clicked ASK button");
        AlertDialog.Builder popUp = new AlertDialog.Builder(this);
        popUp.setMessage("This feature has not been implemented yet");
        popUp.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setCheckboxFalse();
            }
        });
        popUp.setCancelable(true);
        popUp.create().show();
    }

    public void setCheckboxFalse() {
        // Set the checkbox back to unchecked
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor changeSettings = settings.edit();
        changeSettings.putBoolean("use_ask_library", false);
        changeSettings.commit();
    }

    private void setupSimplePreferencesScreen() {
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
}
