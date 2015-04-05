package com.eecs481.tilematch;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;
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

        // Sets the image background for the board
        TableLayout gameBoard = (TableLayout)findViewById(R.id.easy_game_board);
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

        // Randomize ArrayList
        Collections.shuffle(buttonId);


        // Initialize the map
        for (int i=0;i<2;i++) {
            tagMap.put(buttonId.get(i),imgTags.get(0));
            tagMap.put(buttonId.get(i+2),imgTags.get(1));
            tagMap.put(buttonId.get(i+4),imgTags.get(2));
            tagMap.put(buttonId.get(i+6),imgTags.get(3));
        }

        // Loop through image buttons and set tag to "blank"
        for (int i=1;i<=8;i++) {
            ImageButton ib = (ImageButton) findViewById(buttonId.get(i-1).intValue());
            ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ib.setPadding(10,10,10,10);
            ib.setAdjustViewBounds(true);
            super.setImage(ib, "blank", null, null);
        }
    }
}
