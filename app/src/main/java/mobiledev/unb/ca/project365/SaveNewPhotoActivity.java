package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveNewPhotoActivity extends Activity {

    private ImageView todaysPhotoView;
    private Button btnSavePhoto;
    private File mStorageDir;
    private String mCurrentPhotoPath;
    private Bitmap currentPhotoBitmap;
    private static final String TAG ="Debug SaveNew";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_new_photo);

        todaysPhotoView = (ImageView) findViewById(R.id.todaysPhotoView);
        btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);

        // Display the picture that was just taken

        String imagePath = getIntent().getStringExtra("imagePath");
        currentPhotoBitmap = BitmapFactory.decodeFile(imagePath);
        todaysPhotoView.setImageBitmap(currentPhotoBitmap);

        // Save the picture to the user's list of photos and redirect them to the home page

        btnSavePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveNewPhotoActivity.this, MainActivity.class);
                startActivity(intent);

                File savedPhotoFile = null;

                try {
                    savedPhotoFile = createSavedPhotoFile();
                } catch (IOException ex) {
                    Log.i(TAG, "Photo file was not created. Error: "+ex.getMessage());
                }

                // Display the 365 folder and photos in the Gallery
                galleryAddPic();
            }
        });;
    }

    private File createSavedPhotoFile() throws IOException {
        // Create an image file name

        String mSavedPhotoFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/365Project";
        String caption = "test caption";

        // TODO: get the caption from a text field in save_new_photo.xml

        Photo savedPhoto = new Photo(mSavedPhotoFolderPath,caption);
        String photoFileName = savedPhoto.getFileName();

        // Create a folder for only 365 Project photos

        mStorageDir = createDirectory(mSavedPhotoFolderPath);

        // Create a new image file. We created mStorageDir earlier to make sure that the directory exists,
        // but we only need to specify the directory path, mSavedPhotoFolderPath, to save the photo in that location.

        File image = new File(mSavedPhotoFolderPath,photoFileName+".jpg");
        savePhotoToFile(image);

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "The photo was saved in the following location: " + mCurrentPhotoPath);
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            mStorageDir = new File(directoryPath);
            mStorageDir.mkdirs();
        }
        return directory;
    }

    private void savePhotoToFile(File image) throws IOException {
        OutputStream output;
        try {
            output = new FileOutputStream(image);
            currentPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
