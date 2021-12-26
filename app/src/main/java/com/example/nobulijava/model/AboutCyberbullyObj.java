package com.example.nobulijava.model;

public class AboutCyberbullyObj {
    private String aboutCyberbully;
    private String contact;

    public AboutCyberbullyObj(String aboutCyberbully, String contact) {
        this.aboutCyberbully = aboutCyberbully;
        this.contact = contact;
    }

    public AboutCyberbullyObj() {
    }

    public String getAboutCyberbully() {
        return aboutCyberbully;
    }

    public void setAboutCyberbully(String aboutCyberbully) {
        this.aboutCyberbully = aboutCyberbully;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "aboutCyberbullyObj{" +
                "aboutCyberbully='" + aboutCyberbully + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
