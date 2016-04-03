package mobiledev.unb.ca.project365;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {

    public static final String PHOTOS = "photos";
    public static final String PHOTO_PATH = "photo_path"; // keyword for passing extra data in intents
    public static final String PHOTO_POSITION = "photo_position";

    private String photoPath;
    private String timestamp;
    private String fileName;

    public Photo(String photoPath) {
        this.photoPath = photoPath;
        this.timestamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.fileName = "PROJECT365_" + timestamp + "_";
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getFileName() {
        return this.fileName;
    }
}
