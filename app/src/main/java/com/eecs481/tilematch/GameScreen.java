package com.eecs481.tilematch;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.Chronometer;
import android.widget.TableLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class GameScreen extends Activity {

    Chronometer timer;
    ArrayList<String> imgTags; // This keeps an array of all tile images
    HashMap<Long, String> tagMap = new HashMap<Long, String>();
    private Handler pauseHandler = new Handler();
    Long firstID, secondID;
    long stopTime = 0;
    long distanceFromHighScore = 0;
    int guard = 0;
    int numClicked = 0;
    int numMatched = 0;
    int maxNumMatched;

    AnimatorSet setRightOut1;
    AnimatorSet setRightOut2;
    AnimatorSet setRightIn1;
    AnimatorSet setRightIn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize animation objects for tile flipping animation
        setRightOut1 = (AnimatorSet) AnimatorInflater
                .loadAnimator(getApplicationContext(), R.animator.card_flip_right_out);
        setRightOut2 = (AnimatorSet) AnimatorInflater
                .loadAnimator(getApplicationContext(), R.animator.card_flip_right_out);
        setRightIn1 = (AnimatorSet) AnimatorInflater
                .loadAnimator(getApplicationContext(), R.animator.card_flip_right_in);
        setRightIn2 = (AnimatorSet) AnimatorInflater
                .loadAnimator(getApplicationContext(), R.animator.card_flip_right_in);

        // Shuffles the images used for tile backgrounds
        imgTags = new ArrayList<>(
                Arrays.asList("alarm", "apple", "attendant", "basketball", "bbq",
                        "bed", "burger", "cactus", "chair", "chicken", "circle", "clock", "coffee",
                        "cupcake", "diamond", "fries", "glasses", "heart", "hexagon", "hotdog",
                        "house", "luggage", "medal", "music", "phone", "popsicle", "shelf", "soccer",
                        "square", "star", "stopwatch", "tablet", "triangle", "weights"));
        Collections.shuffle(imgTags);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    public void stopTimer() {
        // Stops the timer when the game goes out of view
        stopTime = timer.getBase() - SystemClock.elapsedRealtime();
        timer.stop();
    }

    public void startTimer() {
        // Starts the timer back up again
        timer.setBase(SystemClock.elapsedRealtime() + stopTime);
        timer.start();
    }

    public void drawBackground(TableLayout gameBoard) {
        // Sets the background image to the game board
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String imagePath = settings.getString("select_picture", "default");
        if (imagePath.equals("default")) return;

        Log.i("[GameScreen]", "Set background image to: " + imagePath);
        Bitmap btm = BitmapFactory.decodeFile(imagePath);
        BitmapDrawable btd = new BitmapDrawable(btm);
        gameBoard.setBackground(btd);
    }

    public void quitButtonClick(View v) {
        // Creates a popup that asks the user if they wish to quit
        Log.i("[GameScreen]", "quitButtonClick in Game Screen");
        stopTimer();
        AlertDialog.Builder quitAlert = new AlertDialog.Builder(this);
        quitAlert.setMessage("Are you sure you want to quit the game?");

        quitAlert.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitHelper();
            }
        });
        quitAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTimer();
                dialog.dismiss();
            }
        });

        quitAlert.setCancelable(false);
        quitAlert.create().show();
    }

    public void quitHelper() {
        // Exits the game screen
        this.finish();
    }

    public void setImage(ImageButton ib, String targetTag, AnimatorSet in, AnimatorSet out) {
        // Changes the image on button ib

        Log.i("[GameScreen]", "targetTag set: " + targetTag);
        ib.setTag(targetTag);
        int picId = getResources().getIdentifier(targetTag, "drawable", "com.eecs481.tilematch");
        ib.setImageResource(picId);

        if (in == null || out == null) return;

        in.setTarget(ib);
        out.setTarget(ib);
        out.start();
        in.start();
    }

    public int tryLock() {
        // Locks the buttons
        int temp = guard;
        guard = 1;
        return temp;
    }

    public void releaseGuard() {
        guard = 0;
    }

    public void clickHelper(View v) {
        ImageButton ib = (ImageButton) findViewById(v.getId());
        String s;
        s = "Clicked Button: " + v.getId();
        Log.i("[GameScreen]", s);

        numClicked++;
        Log.i("[GameScreen]", "numClicked: " + numClicked);
        if (numClicked == 1) {
            // Set image
            firstID = new Long(v.getId());
            String targetTag = tagMap.get(firstID);
            setImage(ib, targetTag, setRightIn1, setRightOut1);
            ib.setEnabled(false);

            /* When you click on a tile, wait for the animation
               to end before releasing the guard */
            pauseHandler.postDelayed(new Runnable() {
                public void run() {
                    releaseGuard();
                }
            }, 255);
        }
        else if (numClicked == 2) {
            // Set image
            secondID = new Long(v.getId());
            String targetTag = tagMap.get(secondID);
            setImage(ib, targetTag, setRightIn1, setRightOut1);

            // Check if tiles match
            final ImageButton ib1 = (ImageButton) findViewById(firstID.intValue());
            final ImageButton ib2 = (ImageButton) findViewById(secondID.intValue());

            if (tagMap.get(secondID).equals(tagMap.get(firstID))) {
                Log.i("[GameScreen]", "Tiles match");
                numMatched++;

                // If match, pause for 0.5 seconds, then remove tiles
                pauseHandler.postDelayed(new Runnable() {
                    public void run() {
                        pauseMatchCallBack(ib1, ib2);
                    }
                }, 500);
                if (numMatched == maxNumMatched) {
                    Log.i("[GameScreen]", "All tiles matched, timer stopped");
                    // Stop timer and check for high score
                    final long score = checkScore();

                    Log.i("[GameScreen]", "Show winning popup");
                    pauseHandler.postDelayed(new Runnable() {
                        public void run() {
                            // Create pop up message after delaying
                            showPopUp(score);
                        }
                    }, 1000);
                }
            }
            else {
                Log.i("[GameScreen]", "Tiles don't match");
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
        releaseGuard();
    }

    public void pauseCallBack(ImageButton ib1, ImageButton ib2) {
        ib1.setEnabled(true);
        setImage(ib2, "blank", setRightIn1, setRightOut1);
        setImage(ib1, "blank", setRightIn2, setRightOut2);

        /* If the tiles do not match, wait for animation to end
           before releasing the guard */
        pauseHandler.postDelayed(new Runnable() {
            public void run() {
                releaseGuard();
            }
        }, 255);
    }

    public void clickedTile(View v) {
        if (tryLock() == 0) {
            clickHelper(v);
        }
    }

    public void showPopUp(long score) {
        AlertDialog.Builder popUp = new AlertDialog.Builder(this);
        popUp.setMessage(popUpMessage(score));
        popUp.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitHelper();
            }
        });
        popUp.setCancelable(true);
        popUp.create().show();
    }

    public String popUpMessage(long score) {
        String message = "Congratulations! You won!\n";
        if (distanceFromHighScore == 0)
            message += "You got a high score of " + score + " seconds!";
        else
            message += "You are " + distanceFromHighScore + " seconds away from a high score";
        return message;
    }

    public long checkScore() {
        timer.stop();
        long gameTime = SystemClock.elapsedRealtime() - timer.getBase() - 500;
        long bestTime;

        SharedPreferences scores = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor changeScore = scores.edit();
        String difficultyLevel = scores.getString("select_difficulty", "2");

        if (difficultyLevel.equals("1")) {
            bestTime = scores.getLong("easy_score", 0);
            if (bestTime == 0 || gameTime < bestTime) {
                changeScore.putLong("easy_score", gameTime);
                changeScore.commit();
            }
            else
                distanceFromHighScore = gameTime/1000 - bestTime/1000;
        }
        else if (difficultyLevel.equals("2")) {
            bestTime = scores.getLong("medium_score", 0);
            if (bestTime == 0 || gameTime < bestTime) {
                changeScore.putLong("medium_score", gameTime);
                changeScore.commit();
            }
            else
                distanceFromHighScore = gameTime/1000 - bestTime/1000;
        }
        else if (difficultyLevel.equals("3")) {
            bestTime = scores.getLong("hard_score", 0);
            if (bestTime == 0 || gameTime < bestTime) {
                changeScore.putLong("hard_score", gameTime);
                changeScore.commit();
            }
            else
                distanceFromHighScore = gameTime/1000 - bestTime/1000;
        }
        else
            Log.e("[GameScreen]", "difficultyLevel unexpected: " + difficultyLevel);

        if (distanceFromHighScore == 0)
            return gameTime/1000;
        else
            return 0;
    }
}
