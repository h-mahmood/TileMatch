package com.eecs481.tilematch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MenuScreen extends Activity {

    public void playButtonClick(View v) {
        Log.i("[btn]", "playButtonClick in MenuScreen.java");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String difficultyLevel = settings.getString("select_difficulty", "2");

        if (difficultyLevel.equals("1")) {
            Log.i("[MenuScreen]", "Selected EasyGame");
            startActivity(new Intent(this, EasyGame.class));
        }
        else if (difficultyLevel.equals("2")) {
            Log.i("[MenuScreen]", "Selected MediumGame");
            startActivity(new Intent(this, MediumGame.class));
        }
        else if (difficultyLevel.equals("3")) {
            Log.i("[MenuScreen]", "Selected HardGame");
            startActivity(new Intent(this, HardGame.class));
        }
        else
            Log.e("[MenuScreen]", "difficultyLevel unexpected: " + difficultyLevel);
    }

    public void settingButtonClick(View v) {
        Log.i("[btn]", "settingButtonClick in MenuScreen.java");
        startActivity(new Intent(this, GameSettings.class));
    }

    public void exitButtonClick(View v) {
        Log.i("[btn]", "exitButtonClick in MenuScreen.java");
        this.finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_screen);
    }
}