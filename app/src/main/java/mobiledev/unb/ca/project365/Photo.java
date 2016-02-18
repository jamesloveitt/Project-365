package mobiledev.unb.ca.project365;

public class Photo {

    private String photoPath;
    private String caption;
    // might be better to get date, location via metadata?
    //private String date;
    //private String location;

    public Photo(String photoPath, String caption) {
        this.photoPath = photoPath;
        this.caption = caption;
    }

    private String getPhotoPath() {
        return photoPath;
    }

    private String getCaption() {
        return caption;
    }

    private void setCaption(String newCaption) {
        this.caption = newCaption;
    }

}
