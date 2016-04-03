package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    This class displays all the user's saved photos in a GridView layout,
    organized by month.
 */

public class ViewCalendarActivity extends Activity {

    private File mStorageDir;
    private String mSavedPhotoBasePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/365Project";
    private static final String TAG ="Debug ViewCalendar";
    private ArrayList<String> mAllPhotoPaths = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_all_months);

        mStorageDir = createDirectory(mSavedPhotoBasePath);

        File baseDirectory = new File(mSavedPhotoBasePath);
        if(!baseDirectory.exists()) {
            Log.i(TAG, "No directory for saved photos exists");
        }

        File[] folders = baseDirectory.listFiles();
        Arrays.sort(folders);

        for (File file : folders) {
            if(file.isDirectory() && folderIsMonth(file.getName())) {
                File currDirectory = file;
                List<Photo> currMonthPhotos = loadSavedPhotos(currDirectory);

                // for the current month directory, create a calendar header (containing the month and year)
                // and a GridView that contains the photos in that directory.

                // inflate the view for an individual month
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View individualMonthView = inflater.inflate(R.layout.calendar_single_month, null);

                // fill in any details dynamically here
                GridView currentMonthGridView = (GridView) individualMonthView.findViewById(R.id.month_grid);
                TextView monthHeader = (TextView) individualMonthView.findViewById(R.id.month_header); //(TextView) findViewById(R.id.month_header);

                String currentMonth = parseIntoMonthYear(file.getName());
                monthHeader.setText(currentMonth);
                populateGridView(currentMonthGridView, currMonthPhotos);

                // add the newly modified views from calendar_single_month.xmlonth.xml to calendar_all_months.xml's layout
                LinearLayout allMonthsContainer = (LinearLayout) findViewById(R.id.all_months_container);
                allMonthsContainer.addView(individualMonthView);
            }
        }
    }

    /*
        This method handles populating the GridView for an individual month
        with photos from the corresponding folder. It also assigns an onClickListener
        so that tapping a photo brings up a full-size view.
     */

    private void populateGridView(GridView gridView, List<Photo> photos) {
        PhotoGridAdapter adapter = new PhotoGridAdapter(this, photos);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent fullSizePhotoIntent = new Intent(ViewCalendarActivity.this, ViewFullSizePhotoActivity.class);
                fullSizePhotoIntent.putStringArrayListExtra(Photo.PHOTO_PATH, mAllPhotoPaths);
                fullSizePhotoIntent.putExtra(Photo.PHOTO_POSITION, position);
                startActivity(fullSizePhotoIntent);
            }
        });
    }

    /*
        Checks if the folder was created by the app.
     */

    private boolean folderIsMonth(String folderName) {
        // Check if the folder name is in the format "2016_01"
        Pattern r = Pattern.compile("20\\d\\d_\\d\\d$");
        Matcher m = r.matcher(folderName);

        return m.find();
    }

    /*
        This method will return a String for the calender header
        (e.g. "2016_01" will be parsed into "January 2016").
     */

    private String parseIntoMonthYear(String inputString) {
        String[] tokens = inputString.split("_");

        if(tokens.length < 2) {
            return inputString; //folder name not formatted correctly.
        }

        String year = tokens[0];
        String month = tokens[1];

        switch(month) {
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
        }
        return month+ " " + year;
    }

    private File createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            mStorageDir = new File(directoryPath);
            mStorageDir.mkdirs();
        }
        return directory;
    }

    /*
        Populate collections for the current month's photos as well as a list of
        all photo paths to be used for swiping through all photos.
     */

    private List<Photo> loadSavedPhotos(File directory) {
        List<Photo> photos = new ArrayList<Photo>();

        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                // Add only photos, not folders
                if(file.isFile()) {
                    photos.add(new Photo(file.getAbsolutePath()));
                    mAllPhotoPaths.add(file.getAbsolutePath());
                }
            }
        }
        return photos;
    }
}
