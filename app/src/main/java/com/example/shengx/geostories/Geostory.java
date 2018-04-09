package com.example.shengx.geostories;

import android.graphics.Bitmap;

/**
 * Created by SHENG.X on 2018-03-22.
 */

public class Geostory {
    Bitmap profile_image;
    Bitmap story_image;
    String username;
    String datePosted;
    String geostory;

    public Geostory(Bitmap profile_image, Bitmap story_image, String username, String datePosted, String geostory) {
        this.profile_image = profile_image;
        this.story_image = story_image;
        this.username = username;
        this.datePosted = datePosted;
        this.geostory = geostory;
    }

    public Geostory(String username, String datePosted, String geostory) {
        this.username = username;
        this.datePosted = datePosted;
        this.geostory = geostory;
    }

    public Bitmap getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(Bitmap profile_image) {
        this.profile_image = profile_image;
    }

    public Bitmap getStory_image() {
        return story_image;
    }

    public void setStory_image(Bitmap story_image) {
        this.story_image = story_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getGeostory() {
        return geostory;
    }

    public void setGeostory(String geostory) {
        this.geostory = geostory;
    }
}
