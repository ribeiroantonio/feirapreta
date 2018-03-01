package br.com.feirapreta.model;

/**
 * Created by WEB on 28/02/2018.
 */

public class Post {

    private int id;
    private String link;
    private String imageLowResolution;
    private String imageThumbnail;
    private String imageStandardResolution;
    private Person person;

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
