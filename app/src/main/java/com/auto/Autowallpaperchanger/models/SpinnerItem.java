package com.auto.Autowallpaperchanger.models;

public class SpinnerItem {
    private int profile;
    private String title;

    public SpinnerItem(int profile2, String title2) {
        this.profile = profile2;
        this.title = title2;
    }

    public int getProfile() {
        return this.profile;
    }

    public void setProfile(int profile2) {
        this.profile = profile2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String toString() {
        return this.title;
    }
}
