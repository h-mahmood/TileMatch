package com.eecs481.tilematch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class ImageAdapter extends ArrayAdapter<File> {

    public ImageAdapter(Context context, ArrayList<File> picList) {
        super(context, 0, picList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File current = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.picture_object, parent, false);
        }

        ImageView currentPic = (ImageView) convertView.findViewById(R.id.picture_object);
        try {
            // Loads the next image into the current view
            BitmapLoader nextPic = new BitmapLoader(currentPic);
            String filePath = current.getAbsolutePath();
            nextPic.execute(filePath);
            currentPic.setTag(filePath);
        }
        catch(OutOfMemoryError e) {
            Log.e("[Pic]", "The picture is too large");
        }

        return convertView;
    }
}

class BitmapLoader extends AsyncTask<String, Void, Bitmap> {

    final WeakReference<ImageView> picView;

    public BitmapLoader(ImageView view) {
        picView = new WeakReference<>(view);
    }

    @Override
    protected void onPostExecute(Bitmap pic) {
        if (pic != null && picView != null) {
            // Sets the view
            final ImageView view = picView.get();
            if (view != null)
                view.setImageBitmap(pic);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // Creates a new thread for each image
        String path = params[0];
        return resizePic(path);
    }

    public Bitmap resizePic(String path) {
        final BitmapFactory.Options option = new BitmapFactory.Options();

        // Decreases the size/quality of the image for faster loading
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);
        option.inSampleSize = findPicSize(option);
        option.inJustDecodeBounds = false;
        option.inPreferredConfig = Bitmap.Config.RGB_565;
        option.inDither = true;

        return BitmapFactory.decodeFile(path, option);
    }

    public int findPicSize(BitmapFactory.Options option) {
        // Finds what factor to scale the image down to
        int size = 1;
        int width = option.outWidth;
        int height = option.outHeight;

        if (width > 180 || height > 180) {
            int hWidth = width / 2;
            int hHeight = height / 2;
            while ((hWidth / size) > 180 && (hHeight / size) > 180)
                size *= 2;
        }
        return size;
    }
}

public class PictureList extends ActionBarActivity {

    ArrayList<File> pics;

    void findPics(File root, final ArrayList<File> pics) {
        if (!root.exists())
            return;
        try {
            File[] listDir = root.listFiles();
            for (File current : listDir) {
                if (current.isDirectory())
                    findPics(current, pics);
                FilenameFilter filt = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.endsWith(".jpg");
                    }
                };
                if (filt.accept(current, current.getAbsolutePath()))
                    pics.add(current);

            }
        } catch (Exception e) {
            Log.i("[Pic]", "error");
        }
    }

    public void pictureClick(View v) {
        // Changes preference settings to record filePath of background image
        String filePath = (String) v.getTag();
        Log.i("[PictureClick]", "Chose this background image: " + filePath);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor changeSettings = settings.edit();
        changeSettings.putString("select_picture", filePath);
        changeSettings.commit();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);

        // Adds a back button to the menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File sdcard = Environment.getExternalStorageDirectory();
        File pictures = new File(sdcard.getAbsolutePath() + "/Pictures/");
        File camera = new File(sdcard.getAbsolutePath() + "/DCIM/Camera/");
        File downloads = new File(sdcard.getAbsolutePath() + "/Download/");

        // Finds all images stored in the directories listed above
        pics = new ArrayList<>();
        findPics(pictures, pics);
        findPics(camera, pics);
        findPics(downloads, pics);
        Log.i("[Pic]", "Number of pics found: " + pics.size());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Dynamically loads each image
        ImageAdapter adapter = new ImageAdapter(this, pics);
        GridView picList = (GridView) findViewById(R.id.picList);
        picList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}