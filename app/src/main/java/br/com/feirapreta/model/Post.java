package br.com.feirapreta.model;

/**
 * Created by WEB on 28/02/2018.
 */

public class Post {

    private int id;
    private String link;
    private boolean isHighlight;
    private String imageLowResolution;
    private String imageThumbnail;
    private String imageStandardResolution;
    private String subtitle;
    private Person person;

    public Post() {
    }

    public Post(int id, String link, boolean isHighlight, String imageLowResolution, String imageThumbnail, String imageStandardResolution, String subtitle, Person person) {
        this.id = id;
        this.link = link;
        this.isHighlight = isHighlight;
        this.imageLowResolution = imageLowResolution;
        this.imageThumbnail = imageThumbnail;
        this.imageStandardResolution = imageStandardResolution;
        this.subtitle = subtitle;
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public String getImageLowResolution() {
        return imageLowResolution;
    }

    public void setImageLowResolution(String imageLowResolution) {
        this.imageLowResolution = imageLowResolution;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getImageStandardResolution() {
        return imageStandardResolution;
    }

    public void setImageStandardResolution(String imageStandardResolution) {
        this.imageStandardResolution = imageStandardResolution;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
