package br.com.feirapreta.model;

/**
 * Created by Antonio Ribeiro on 28/02/2018.
 */

public class Person {

    private String id;
    private String usernameInstagram;
    private String fullNameInstagram;
    private String profilePictureInstagram;
    private String phoneNumber;

    public Person() {
    }

    public Person(String id, String usernameInstagram, String fullNameInstagram, String profilePictureInstagram, String phoneNumber) {
        this.id = id;
        this.usernameInstagram = usernameInstagram;
        this.fullNameInstagram = fullNameInstagram;
        this.profilePictureInstagram = profilePictureInstagram;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsernameInstagram() {
        return usernameInstagram;
    }

    public void setUsernameInstagram(String usernameInstagram) {
        this.usernameInstagram = usernameInstagram;
    }

    public String getFullNameInstagram() {
        return fullNameInstagram;
    }

    public void setFullNameInstagram(String fullNameInstagram) {
        this.fullNameInstagram = fullNameInstagram;
    }

    public String getProfilePictureInstagram() {
        return profilePictureInstagram;
    }

    public void setProfilePictureInstagram(String profilePictureInstagram) {
        this.profilePictureInstagram = profilePictureInstagram;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
