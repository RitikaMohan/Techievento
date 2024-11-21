package com.example.myapplication;

import java.util.List;

public class Event {
    private String image_url;
    private String title;
    private String date;
    private String time;
    private String location;
    private int participants;
    private String category;
    private List<String> activities;
    private String winner1;
    private String winner2;
    private String winner3;
    private String host;


    // No-argument constructor (needed for Firebase)
    public Event() {
    }

    public Event(String image_url, String title, String date, String time, String location, int participants, String category, List<String> activities, String winner1, String winner2, String winner3, String host) {
        this.image_url = image_url;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.participants = participants;
        this.category = category;
        this.activities = activities;
        this.winner1 = winner1;
        this.winner2 = winner2;
        this.winner3 = winner3;
        this.host = host;
    }

    public Event(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public  String getWinner1() {
        return winner1;
    }
    public void setWinner1(String winner1) {
        this.winner1 = winner1;
    }

    public  String getWinner2() {
        return winner2;
    }
    public void setWinner2(String winner2) {
        this.winner2 = winner2;
    }

    public  String getWinner3() {
        return winner3;
    }
    public void setWinner3(String winner3) {
        this.winner3 = winner3;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}

