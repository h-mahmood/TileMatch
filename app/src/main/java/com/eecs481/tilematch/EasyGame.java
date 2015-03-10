package com.eecs481.tilematch;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Chronometer;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class EasyGame extends GameScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_easy_game);
        maxNumMatched = 4;

        // Start timer
        timer = (Chronometer) findViewById(R.id.timer);
        timer.start();

        ArrayList<Long> buttonId = new ArrayList<Long>();
        buttonId.add( new Long(R.id.button1));
        buttonId.add( new Long(R.id.button2));
        buttonId.add( new Long(R.id.button3));
        buttonId.add( new Long(R.id.button4));
        buttonId.add( new Long(R.id.button5));
        buttonId.add( new Long(R.id.button6));
        buttonId.add( new Long(R.id.button7));
        buttonId.add( new Long(R.id.button8));

        // Randomize ArrayList
        Collections.shuffle(buttonId);

        // Initialize the map
        for (int i=0;i<2;i++) {
            tagMap.put(buttonId.get(i),"circle");
            tagMap.put(buttonId.get(i+2),"square");
            tagMap.put(buttonId.get(i+4),"star");
            tagMap.put(buttonId.get(i+6),"triangle");
        }

        // Loop through image buttons and set tag to "blank"
        for (int i=1;i<=8;i++) {
            ImageButton ib = (ImageButton) findViewById(buttonId.get(i-1).intValue());
            ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib.setPadding(10,10,10,10);
            ib.setAdjustViewBounds(true);
            super.setImage(ib, "blank");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_easy_game, menu);
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
