package mobiledev.unb.ca.project365;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {

    public static final String PHOTO_PATH = "photo_path"; // keyword for passing extra data in intents

    private String photoPath;
    private String caption;
    private String timestamp;
    private String fileName;
    // might be better to get date, location via metadata?
    //private String date;
    //private String location;

    public Photo(String photoPath, String caption) {
        this.photoPath = photoPath;
        this.caption = caption;
        this.timestamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.fileName = "PROJECT365_" + timestamp + "_";
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String newCaption) {
        this.caption = newCaption;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getFileName() {
        return this.fileName;
    }
}
