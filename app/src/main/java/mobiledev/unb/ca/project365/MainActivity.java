package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private Button mTodaysPhotoBtn;
    private Button mViewCalendarBtn;
    private String mCurrentPhotoPath;
    private String mPhotoFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/365Project";
    private File mStorageDir;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG ="Debug MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTodaysPhotoBtn = (Button) findViewById(R.id.btnTodaysPhoto);
        mTodaysPhotoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mViewCalendarBtn = (Button) findViewById(R.id.btnViewCalendar);
        mViewCalendarBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewCalendarActivity.class);
                Log.i(TAG, "mCurrentPhotoPath ");

                startActivity(intent);
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i(TAG, "Photo file was not created. Error: "+ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PROJECT365_" + timeStamp + "_";

        // Create a folder for only 365 Project photos

        File folder = new File(mPhotoFolderPath);
        if (!folder.exists()) {
            mStorageDir = new File(mPhotoFolderPath);
            mStorageDir.mkdirs();
        }

        // Create a new file

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                mStorageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "The photo was saved in the following location: " + mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i(TAG, "Switching to new activity");

            Intent intent = new Intent(MainActivity.this, SaveNewPhotoActivity.class);

            // Send the photo that was just taken to the SaveNewPhoto activity
            intent.putExtra("imagePath",mCurrentPhotoPath);
            startActivity(intent);
        }
    }
}
