package com.eecs481.tilematch;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

public class GameSettings extends PreferenceActivity {

    // Keeps track of user preferences
    private Preference pref;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();

        ActionBar topBar = getActionBar();
        topBar.setDisplayHomeAsUpEnabled(true);
        topBar.setIcon(android.R.color.transparent);

        pref = findPreference("select_picture");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                pictureButtonClick();
                return false;
            }
        });

        // Get the current best scores and display them
        SharedPreferences scores = PreferenceManager.getDefaultSharedPreferences(this);
        long easy = scores.getLong("easy_score", 0) / 1000;
        long medium = scores.getLong("medium_score", 0) / 1000;
        long hard = scores.getLong("hard_score", 0) / 1000;

        Preference easyPref = findPreference("easy_score");
        if (easy == 0)
            easyPref.setTitle("Easy Difficulty: " + "          " + "None");
        else
            easyPref.setTitle("Easy Difficulty: " + "          " + easy + " seconds");

        Preference mediumPref = findPreference("medium_score");
        if (medium == 0)
            mediumPref.setTitle("Medium Difficulty: " + "    " + "None");
        else
            mediumPref.setTitle("Medium Difficulty: " + "    " + medium + " seconds");

        Preference hardPref = findPreference("hard_score");
        if (hard == 0)
            hardPref.setTitle("Hard Difficulty: " + "          " + "None");
        else
            hardPref.setTitle("Hard Difficulty: " + "          " + hard + " seconds");
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

    private void setupSimplePreferencesScreen() {
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);
    }
}
