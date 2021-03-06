package mobiledev.unb.ca.project365;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/*
    Fills the GridView with thumbnails of images saved in the 365 Project folder.
    Based on http://www.androidbegin.com/tutorial/android-display-images-from-sd-card-tutorial/.
 */

public class PhotoGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Photo> photos;

    public PhotoGridAdapter(Activity activity, List<Photo> photos) {
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.photos = photos;
    }

    public int getCount() {
        return photos.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return photos.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.grid_photo_item, null);
        }

        ImageView imageThumbnail = (ImageView) convertView.findViewById(R.id.image_thumbnail);
        Bitmap bitmap = BitmapFactory.decodeFile(photos.get(position).getPhotoPath());

        // Displays photos in portrait orientation.

        if(bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90F);
            int scaledWidth = bitmap.getWidth()/10;
            int scaledHeight = bitmap.getHeight()/10;

            bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            imageThumbnail.setImageBitmap(rotatedBitmap);
            imageThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageThumbnail.setLayoutParams(new GridView.LayoutParams(150, 150));
        }
        return convertView;
    }
}
