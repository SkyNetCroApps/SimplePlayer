package com.example.aleksandar.skynet;

/**
 * Class {@link VideoItem} contains data for a specific video
 *
 * @author SkyNet team
 */

public class VideoItem {
    private String title;
    private String duration;
    private String thumbnailURL;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

}
