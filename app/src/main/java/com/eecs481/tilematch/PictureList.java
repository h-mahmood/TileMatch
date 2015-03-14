package com.eecs481.tilematch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

class imageAdapter extends ArrayAdapter<Bitmap> {

    public imageAdapter(Context context, ArrayList<Bitmap> picList) {
        super(context, 0, picList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap current = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.picture_object, parent, false);
        }

        ImageView currentPic = (ImageView) convertView.findViewById(R.id.picture_button);
        BitmapDrawable setPic = new BitmapDrawable(current);
        currentPic.setBackground(setPic);

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
        }
        catch(Exception e) {
            Log.i("[Pic]", "error");
        }
    }

    void printPics() {
        File sdcard = Environment.getExternalStorageDirectory();
        File dirs = new File(sdcard.getAbsolutePath());

        ArrayList<File> pics = new ArrayList<>();
        findPics(dirs, pics);

        if (pics.isEmpty())
            Log.i("[Pic]", "No pics found");
        for (int x = 0; x < pics.size(); x++)
            Log.i("[Pic]", pics.get(x).getAbsolutePath());
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

        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        for (int x = 0; x < pics.size(); x++) {
            try {
                //Note: may need to add function to change picture size when too big
                Bitmap next = BitmapFactory.decodeFile(pics.get(x).getAbsolutePath());
                bitmapList.add(next);
            }
            catch(OutOfMemoryError e) {
                Log.i("[Pic]", "The picture is too large");
            }
        }

        imageAdapter adapter = new imageAdapter(this, bitmapList);
        ListView picList = (ListView) findViewById(R.id.picList);
        picList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture_list, menu);
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
