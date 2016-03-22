package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ViewFullSizePhotoActivity extends Activity {
    private static final String TAG ="Debug SaveNew";

    private ImageView fullSizePhotoView;
    private String mCurrentPhotoPath;
    private Bitmap currentPhotoBitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_full_size_photo);

        fullSizePhotoView = (ImageView) findViewById(R.id.fullSizePhotoView);

        // Retrieve and display the full size photo

        mCurrentPhotoPath = getIntent().getStringExtra(Photo.PHOTO_PATH);
        currentPhotoBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

        // Some sample code for rotation we could use later on
        Matrix matrix = new Matrix();
        matrix.postRotate(90F);
        Bitmap rotatedBitmap = Bitmap.createBitmap(currentPhotoBitmap, 0, 0, currentPhotoBitmap.getWidth(), currentPhotoBitmap.getHeight(), matrix, true);

        fullSizePhotoView.setImageBitmap(rotatedBitmap);
    }

}
