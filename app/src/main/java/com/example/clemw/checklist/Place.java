package com.example.clemw.checklist;

/**
 * Created by clemw on 8/19/14.
 */
public class Place extends Object {
    private String name;
    private Double rating;
    private String photoReference;

    public Place(String name, Double rating, String photoReference) {
        this.name = name;
        this.rating = rating;
        this.photoReference = photoReference;
    }

    public String getName() {
        return name;
    }

    public Double getRating() {
        return rating;
    }

    public String getPhotoReference() {
        return photoReference;
    }
}
