package com.eecs481.tilematch;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.Chronometer;
import java.util.HashMap;

public class GameScreen extends ActionBarActivity {

    Chronometer timer;
    HashMap<Long, String> tagMap = new HashMap<Long, String>();
    int numClicked = 0;
    Long  firstID, secondID;
    private Handler pauseHandler = new Handler();
    int guard = 0;
    int numMatched = 0;
    int maxNumMatched;

    public void quitHelper() {
        this.finish();
    }

    public void quitButtonClick(View v) {
        Log.i("[GameScreen]", "quitButtonClick in Game Screen");
        quitHelper();
    }

    public void setImage(ImageButton ib, String targetTag) {
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

    public int tryLock() {
        // Locks the buttons
        int temp = guard;
        guard = 1;
        return temp;
    }

    public void clickHelper(View v) {
        ImageButton ib = (ImageButton) findViewById(v.getId());
        String s;
        s = "" + v.getId();
        Log.i("[GameScreen]", "Clicked button");
        Log.i("[GameScreen]", s);

        numClicked++;
        Log.i("[GameScreen]", "numClicked: " + numClicked);
        if (numClicked == 1) {
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
            final ImageButton ib1 = (ImageButton) findViewById(firstID.intValue());
            final ImageButton ib2 = (ImageButton) findViewById(secondID.intValue());
            if (tagMap.get(secondID).equals(tagMap.get(firstID))) {
                Log.i("[GameScreen]", "tile matched!");
                numMatched++;

                // If match, pause for 0.5 seconds, then remove tiles
                pauseHandler.postDelayed(new Runnable() {
                    public void run() {
                        pauseMatchCallBack(ib1, ib2);
                    }
                }, 500);
                if (numMatched == maxNumMatched) {
                    Log.i("[GameScreen]", "All tile matched, timer freeze!");
                    // Freeze timer after all tiles are matched
                    timer.stop();

                    Log.i("[GameScreen]", "Show congratulations sign!");
                    // Create pop up message
                    showPopUp();
                }
            }
            else {
                Log.i("[GameScreen]", "tile not match :-<");
                // Wait for 2 seconds if they don't match
                pauseHandler.postDelayed(new Runnable() {
                    public void run() {
                        pauseCallBack(ib1, ib2);
                    }
                }, 2000);
            }
            numClicked = 0;
        }
    }

    public void pauseMatchCallBack(ImageButton ib1, ImageButton ib2) {
        ib2.setVisibility(View.INVISIBLE);
        ib2.setOnClickListener(null);
        ib1.setVisibility(View.INVISIBLE);
        ib1.setOnClickListener(null);
        guard = 0;
    }

    public void pauseCallBack(ImageButton ib1, ImageButton ib2) {
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
}
