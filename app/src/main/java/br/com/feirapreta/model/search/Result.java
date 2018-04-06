package br.com.feirapreta.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import br.com.feirapreta.model.Person;

public class Result implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("createdDateInstagram")
    @Expose
    private String createdDateInstagram;
    @SerializedName("imageLowResolution")
    @Expose
    private String imageLowResolution;
    @SerializedName("imageThumbnail")
    @Expose
    private String imageThumbnail;
    @SerializedName("imageStandardResolution")
    @Expose
    private String imageStandardResolution;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("isHighlight")
    @Expose
    private Boolean isHighlight;
    @SerializedName("person")
    @Expose
    private Person person;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCreatedDateInstagram() {
        return createdDateInstagram;
    }

    public void setCreatedDateInstagram(String createdDateInstagram) {
        this.createdDateInstagram = createdDateInstagram;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsHighlight() {
        return isHighlight;
    }

    public void setIsHighlight(Boolean isHighlight) {
        this.isHighlight = isHighlight;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
