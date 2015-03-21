package com.eecs481.tilematch;

import android.os.Bundle;
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
            tagMap.put(buttonId.get(i),imgTags.get(0));
            tagMap.put(buttonId.get(i+4),imgTags.get(1));
            tagMap.put(buttonId.get(i+8),imgTags.get(2));
            tagMap.put(buttonId.get(i+12),imgTags.get(3));
            tagMap.put(buttonId.get(i+16),imgTags.get(4));
            tagMap.put(buttonId.get(i+20),imgTags.get(5));
        }

        // Loop through image buttons and set tag to "blank"
        for (int i=1;i<=24;i++) {
            ImageButton ib = (ImageButton) findViewById(buttonId.get(i-1).intValue());
            ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib.setPadding(10,10,10,10);
            ib.setAdjustViewBounds(true);
            super.setImage(ib, "blank", null, null);
        }
    }
}
