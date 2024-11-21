package com.example.myapplication;

import java.util.List;

public class Community {
    private String title;
    private String image_url;
    private String people;



    // No-argument constructor (needed for Firebase)
    public Community() {
    }

    public Community(String title, String image_url, String people) {
        this.title = title;
        this.image_url = image_url;
        this.people = people;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

}
