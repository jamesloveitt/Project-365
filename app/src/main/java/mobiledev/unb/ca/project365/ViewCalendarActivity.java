package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
    This class displays all the user's saved photos in a GridView layout.
    TODO: Find a way to categorize each grid by month.
    TODO: Add the ability to click on thumbnails to view the full-size picture.
 */

public class ViewCalendarActivity extends Activity {

    private File mStorageDir;
    private String mSavedPhotoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/365Project";
    private List<Photo> photos = new ArrayList<Photo>();

    private static final String TAG ="Debug ViewCalendar";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar);

        mStorageDir = createDirectory(mSavedPhotoDirectory);

        photos = loadSavedPhotos(mStorageDir);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        PhotoGridAdapter adapter = new PhotoGridAdapter(this, photos);
        gridView.setAdapter(adapter);
    }

    private File createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            mStorageDir = new File(directoryPath);
            mStorageDir.mkdirs();
        }
        return directory;
    }

    private List<Photo> loadSavedPhotos(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                // Avoid adding folders
                if(file.isFile()) {
                    photos.add(new Photo(file.getAbsolutePath(), "test caption"));
                }
            }
        }
        return photos;
    }
}
