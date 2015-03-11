package com.eecs481.tilematch;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.Collections;


public class HardGame extends GameScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_game);

        maxNumMatched = 12;

        // Start timer
        timer = (Chronometer) findViewById(R.id.timer);
        timer.start();

        // Sets the image background for the board
        TableLayout gameBoard = (TableLayout)findViewById(R.id.hard_game_board);
        drawBackground(gameBoard);

        ArrayList<Long> buttonId = new ArrayList<Long>();
        buttonId.add( new Long(R.id.button1));
        buttonId.add( new Long(R.id.button2));
        buttonId.add( new Long(R.id.button3));
        buttonId.add( new Long(R.id.button4));
        buttonId.add( new Long(R.id.button5));
        buttonId.add( new Long(R.id.button6));
        buttonId.add( new Long(R.id.button7));
        buttonId.add( new Long(R.id.button8));
        buttonId.add( new Long(R.id.button9));
        buttonId.add( new Long(R.id.button10));
        buttonId.add( new Long(R.id.button11));
        buttonId.add( new Long(R.id.button12));
        buttonId.add( new Long(R.id.button13));
        buttonId.add( new Long(R.id.button14));
        buttonId.add( new Long(R.id.button15));
        buttonId.add( new Long(R.id.button16));
        buttonId.add( new Long(R.id.button17));
        buttonId.add( new Long(R.id.button18));
        buttonId.add( new Long(R.id.button19));
        buttonId.add( new Long(R.id.button20));
        buttonId.add( new Long(R.id.button21));
        buttonId.add( new Long(R.id.button22));
        buttonId.add( new Long(R.id.button23));
        buttonId.add( new Long(R.id.button24));

        // Randomizes ArrayList
        Collections.shuffle(buttonId);

        // Initialized the map
        for (int i=0;i<4;i++) {
            tagMap.put(buttonId.get(i),"circle");
            tagMap.put(buttonId.get(i+4),"square");
            tagMap.put(buttonId.get(i+8),"star");
            tagMap.put(buttonId.get(i+12),"triangle");
            tagMap.put(buttonId.get(i+16),"star"); //Change to new patterns for these
            tagMap.put(buttonId.get(i+20),"triangle"); //Change to new patterns for these
        }

        // Loop through image buttons and set tag to "blank"
        for (int i=1;i<=24;i++) {
            ImageButton ib = (ImageButton) findViewById(buttonId.get(i-1).intValue());
            ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib.setPadding(10,10,10,10);
            ib.setAdjustViewBounds(true);
            super.setImage(ib, "blank");
        }
    }
}
