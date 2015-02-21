package com.eecs481.tilematch;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MediumGame extends ActionBarActivity {


    public void clickedTile(View v) {

        ImageButton ib = (ImageButton) findViewById(v.getId());
        String s;
        s = "" + v.getId();
        Log.i("[MediumGame]", "Clicked button");
        Log.i("[MediumGame]", s);

        if(ib.getTag() == "circle")
        {
            ib.setImageResource(R.drawable.square);
            ib.setTag("square");
        }
        else
        {
            ib.setImageResource(R.drawable.circle);
            ib.setTag("circle");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Loop through image buttons and set tag to "blank"

        setContentView(R.layout.activity_medium_game);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medium_game, menu);
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
