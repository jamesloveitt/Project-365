package mobiledev.unb.ca.project365;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by lmeng on 03/04/16.
 */
public class ViewFullSizePhotoAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<String> photoPaths;
    private LayoutInflater inflater;

    public ViewFullSizePhotoAdapter(Activity activity, ArrayList<String> photoPaths) {
        this.activity = activity;
        this.photoPaths = photoPaths;
    }

    @Override
    public int getCount() {
        return this.photoPaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.view_full_size_photo, container, false);

        ImageView fullSizePhotoView = (ImageView) viewLayout.findViewById(R.id.fullSizePhotoView);

        Bitmap currentPhotoBitmap = BitmapFactory.decodeFile(photoPaths.get(position));
        fullSizePhotoView.setImageBitmap(currentPhotoBitmap);

        // Some sample code for rotation we could use later on
        Matrix matrix = new Matrix();
        matrix.postRotate(90F);
        Bitmap rotatedBitmap = Bitmap.createBitmap(currentPhotoBitmap, 0, 0, currentPhotoBitmap.getWidth(), currentPhotoBitmap.getHeight(), matrix, true);

        fullSizePhotoView.setImageBitmap(rotatedBitmap);

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }

}