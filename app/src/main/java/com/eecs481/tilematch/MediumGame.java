package com.eecs481.tilematch;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.Chronometer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MediumGame extends ActionBarActivity {

    Chronometer timer;
    HashMap<Long, String> tagMap = new HashMap<Long, String>();
    int numClicked = 0;
    Long  firstID, secondID;
    private Handler pauseHandler = new Handler();
    int guard = 0;
    int numMatched = 0;

    public void quitHelper() {
        this.finish();
    }

    public void quitButtonClick(View v) {
        Log.i("[MediumGame]", "quitButtonClick in Game Screen");
        quitHelper();
    }

    public void setImage(ImageButton ib, String targetTag){
        if (targetTag.equals("circle")) {
            ib.setImageResource(R.drawable.circle);
            ib.setTag("circle");
        }
        else if (targetTag.equals("square")) {
            ib.setImageResource(R.drawable.square);
            ib.setTag("square");
        }
        else if (targetTag.equals("star")) {
            ib.setImageResource(R.drawable.star);
            ib.setTag("star");
        }
        else if (targetTag.equals("triangle")) {
            ib.setImageResource(R.drawable.triangle);
            ib.setTag("triangle");
        }
        else if (targetTag.equals("blank")) {
            ib.setImageResource(R.drawable.blank);
            ib.setTag("blank");
        }
    }

    // Locks the buttons
    public int tryLock() {
        int temp = guard;
        guard = 1;
        return temp;
    }

    public void clickHelper(View v){
        ImageButton ib = (ImageButton) findViewById(v.getId());
        String s;
        s = "" + v.getId();
        Log.i("[MediumGame]", "Clicked button");
        Log.i("[MediumGame]", s);

        numClicked++;
        Log.i("[MediumGame]", "numClicked: "+ numClicked);
        if (numClicked == 1){
            // Set image
            firstID = new Long(v.getId());
            String targetTag = tagMap.get(firstID);
            setImage(ib, targetTag);
            ib.setEnabled(false);
            guard = 0;
        }
        else if (numClicked == 2) {
            // Set image
            secondID = new Long(v.getId());
            String targetTag = tagMap.get(secondID);
            setImage(ib, targetTag);

            // Check if tiles match
            ImageButton ib1 = (ImageButton) findViewById(firstID.intValue());
            ImageButton ib2 = (ImageButton) findViewById(secondID.intValue());
            if (tagMap.get(secondID).equals(tagMap.get(firstID))) {
                Log.i("[MediumGame]", "tile matched!");
                numMatched++;

                // If match, pause for 0.5 seconds, then remove tiles
                pauseHandler.postDelayed(new Runnable() {
                    public void run() {
                        pauseMatchCallBack();
                    }
                }, 500);
                if (numMatched == 8) {
                    Log.i("[MediumGame]", "All tile matched, timer freeze!");
                    // Freeze timer after all tiles are matched
                    timer.stop();

                    Log.i("[MediumGame]", "Show congratulations sign!");
                    // Create pop up message
                    showPopUp();
                }
            }
            else {
                Log.i("[MediumGame]", "tile not match :-<");
                // Wait for 2 seconds if they don't match
                pauseHandler.postDelayed(new Runnable() {
                    public void run() {
                        pauseCallBack();
                    }
                }, 2000);
            }
            numClicked = 0;
        }
    }

    public void pauseMatchCallBack(){
        ImageButton ib1 = (ImageButton) findViewById(firstID.intValue());
        ImageButton ib2 = (ImageButton) findViewById(secondID.intValue());
        ib2.setVisibility(View.INVISIBLE);
        ib2.setOnClickListener(null);
        ib1.setVisibility(View.INVISIBLE);
        ib1.setOnClickListener(null);
        guard = 0;
    }

    public void pauseCallBack(){
        ImageButton ib1 = (ImageButton) findViewById(firstID.intValue());
        ImageButton ib2 = (ImageButton) findViewById(secondID.intValue());
        ib1.setEnabled(true);
        setImage(ib2, "blank");
        setImage(ib1, "blank");
        guard = 0;
    }

    public void clickedTile(View v) {
        if (tryLock() == 0) {
            clickHelper(v);
        }
    }

    public void showPopUp() {
        AlertDialog.Builder popUp = new AlertDialog.Builder(this);
        popUp.setMessage("Congratulations! You won!");
        popUp.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Closes the popup
                quitHelper();
            }
        });
        popUp.setCancelable(true);
        popUp.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medium_game);

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
        buttonId.add( new Long(R.id.button9));
        buttonId.add( new Long(R.id.button10));
        buttonId.add( new Long(R.id.button11));
        buttonId.add( new Long(R.id.button12));
        buttonId.add( new Long(R.id.button13));
        buttonId.add( new Long(R.id.button14));
        buttonId.add( new Long(R.id.button15));
        buttonId.add( new Long(R.id.button16));

        // Randomizes ArrayList
        Collections.shuffle(buttonId);

        // Initialized the map
        for (int i=0;i<4;i++){
            tagMap.put(buttonId.get(i),"circle");
            tagMap.put(buttonId.get(i+4),"square");
            tagMap.put(buttonId.get(i+8),"star");
            tagMap.put(buttonId.get(i+12),"triangle");
        }

        // Loop through image buttons and set tag to "blank"
        for (int i=1;i<=16;i++){
            ImageButton ib = (ImageButton) findViewById(buttonId.get(i-1).intValue());
            setImage(ib, "blank");
        }
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
