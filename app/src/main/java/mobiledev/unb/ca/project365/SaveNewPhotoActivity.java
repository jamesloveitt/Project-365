package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class SaveNewPhotoActivity extends Activity {

    private ImageView todaysPhotoView;
    private Button btnSavePhoto;
    private Button btnAddCaption;
    private ShareButton btnShareWithFacebook;
    private EditText captionText;
    private File mStorageDir;
    private String mCurrentPhotoPath;
    private Bitmap currentPhotoBitmap;
    private Bitmap mutableBitmap;
    private CallbackManager callbackManager;
    private String mSavedPhotoBasePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/365Project";
    private static final String TAG ="Debug SaveNew";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_new_photo);
        FacebookSdk.sdkInitialize(getApplicationContext());

        // This view contains the image to save

        todaysPhotoView = (ImageView) findViewById(R.id.todaysPhotoView);
        btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);
        btnAddCaption = (Button) findViewById(R.id.btnAddCaption);
        captionText = (EditText) findViewById(R.id.captionText);
        btnShareWithFacebook = (ShareButton) findViewById(R.id.shareButton);
        btnShareWithFacebook.setEnabled(true);

        // Retrieve and display the picture that was just taken

        String imagePath = getIntent().getStringExtra(Photo.PHOTO_PATH);
        currentPhotoBitmap = BitmapFactory.decodeFile(imagePath);


        //Bitmap must be mutable to be edited by the canvas

        mutableBitmap = currentPhotoBitmap.copy(Bitmap.Config.ARGB_8888, true);

        // Some sample code for rotation we could use later on
        Matrix matrix = new Matrix();
        matrix.postRotate(90F);
        Bitmap rotatedBitmap = Bitmap.createBitmap(currentPhotoBitmap, 0, 0, currentPhotoBitmap.getWidth(), currentPhotoBitmap.getHeight(), matrix, true);
        todaysPhotoView.setImageBitmap(rotatedBitmap);


        //Save picture as with Button below but with a caption over the top of the image

        btnAddCaption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveNewPhotoActivity.this, MainActivity.class);
                startActivity(intent);

                Canvas canvas = new Canvas(mutableBitmap);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(25);

                //location on the photo to place the text and what text to write
                canvas.drawText(captionText.getText().toString(), 20, 40, paint);

                File savedPhotoFile = null;

                try {
                    savedPhotoFile = createSavedPhotoFile();
                } catch (IOException ex) {
                    Log.i(TAG, "Photo file was not created. Error: " + ex.getMessage());
                }

                // Display the 365 folder and photos in the Gallery
                galleryAddPic();


            }
        });


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
                    Log.i(TAG, "Photo file was not created. Error: " + ex.getMessage());
                }

                // Display the 365 folder and photos in the Gallery
                galleryAddPic();
            }
        });


        btnShareWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPicture();


            }
        });
    }



    public void postPicture() {
            //share dialog
            AlertDialog.Builder shareDialog = new AlertDialog.Builder(this);
            shareDialog.setTitle("Share Todays Photo");
            shareDialog.setMessage("Share image to Facebook?");
            shareDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //share the image to Facebook
                    SharePhoto photo = new SharePhoto.Builder().setBitmap(mutableBitmap).build();
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                    btnShareWithFacebook.setShareContent(content);
                    btnShareWithFacebook.performClick();
                }
            });
            shareDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            shareDialog.show();
    }



    private File createSavedPhotoFile() throws IOException {
        // Create a folder for only 365 Project photos

        String savedPhotoFolder = mSavedPhotoBasePath + "/" + createDirectoryName();

        mStorageDir = createDirectory(savedPhotoFolder);

        // Create an image file name

        Photo savedPhoto = new Photo(savedPhotoFolder);
        String photoFileName = savedPhoto.getFileName();

        // Create a new image file. We created mStorageDir earlier to make sure that the directory exists,
        // but we only need to specify the directory path, mSavedPhotoFolderPath, to save the photo in that location.

        File image = new File(savedPhotoFolder,photoFileName+".jpg");
        savePhotoToFile(image);

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "The photo was saved in the following location: " + mCurrentPhotoPath);
        return image;
    }

    /*
        Returns the directory name for the current year/month.
        Used so that photos are saved in the correct directory based on
        when the photo was taken.
     */

    private String createDirectoryName() {
        Calendar calendar = Calendar.getInstance();

        String directoryName = "";

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH) + 1;
        String monthString = ""+month;
        if(month < 10){
            monthString = "0"+monthString;
        }

        directoryName += year+ "_" + monthString;

        return directoryName;
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
            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
