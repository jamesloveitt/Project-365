package mobiledev.unb.ca.project365;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewFullSizePhotoActivity extends Activity {
    private static final String TAG ="Debug SaveNew";

    private ArrayList<String> photoPaths;
    private int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_full_size_photo_container);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        // Set up the ViewPager adapter to be able to swipe through photos
        photoPaths = getIntent().getStringArrayListExtra(Photo.PHOTO_PATH);
        position = getIntent().getIntExtra(Photo.PHOTO_POSITION, 0);

        ViewFullSizePhotoAdapter adapter = new ViewFullSizePhotoAdapter(this, photoPaths);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);
    }

}
