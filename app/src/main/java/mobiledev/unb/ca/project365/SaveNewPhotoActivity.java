package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SaveNewPhotoActivity extends Activity {

    private ImageView todaysPhotoView;
    private Button btnSavePhoto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_new_photo);

        todaysPhotoView = (ImageView) findViewById(R.id.todaysPhotoView);
        btnSavePhoto = (Button) findViewById(R.id.btnSavePhoto);

        // Display the picture that was just taken
        String imagePath = getIntent().getStringExtra("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        todaysPhotoView.setImageBitmap(bitmap);

        // Save the picture to the user's list of photos and redirect them to the calendar
        btnSavePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveNewPhotoActivity.this, ViewCalendarActivity.class);
                startActivity(intent);

                // TODO - create a Photo object for the newly saved photo
            }
        });;
    }
}
