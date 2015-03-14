package com.eecs481.tilematch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

class imageAdapter extends ArrayAdapter<File> {

    public imageAdapter(Context context, ArrayList<File> picList) {
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
            //Note: may need to add function to change picture size when too big
            Bitmap next = BitmapFactory.decodeFile(current.getAbsolutePath());
            BitmapDrawable setPic = new BitmapDrawable(next);
            currentPic.setBackground(setPic);
            currentPic.setTag(current.getAbsolutePath());
        }
        catch(OutOfMemoryError e) {
            Log.i("[Pic]", "The picture is too large");
        }

        return convertView;
    }
}

public class PictureList extends ActionBarActivity {

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

        File sdcard = Environment.getExternalStorageDirectory();
        File dirs = new File(sdcard.getAbsolutePath());

        ArrayList<File> pics = new ArrayList<>();
        findPics(dirs, pics);
        Log.i("[Pic]", "Number of pics found: " + pics.size());

        imageAdapter adapter = new imageAdapter(this, pics);
        GridView picList = (GridView) findViewById(R.id.picList);
        picList.setAdapter(adapter);
    }
}