package com.eecs481.tilematch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MenuScreen extends ActionBarActivity {

    public void playButtonClick(View v) {

        Log.i("[btn]", "playButtonClick in MenuScreen.java");
        startActivity(new Intent(this, MediumGame.class));
    }

    public void settingButtonClick(View v) {

        Log.i("[btn]", "settingButtonClick in MenuScreen.java");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}