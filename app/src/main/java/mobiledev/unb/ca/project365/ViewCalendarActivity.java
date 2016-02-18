package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ViewCalendarActivity extends Activity {

    private List<Photo> photos = new ArrayList<Photo>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar);

        photos = loadSavedPhotos();
    }

    private List<Photo> loadSavedPhotos() {
        // TODO - check the folder defined in mPhotoFolderPath to load all previously saved photos for this app
    }
}
