package com.example.clemw.checklist;

import android.graphics.Bitmap;

/**
 * Created by clemw on 8/19/14.
 */
public class Place extends Object {
    private String name;
    private Double rating;
    private String photoReference;
    private final String urlPrefix = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private final String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";
    private String imageUrl;
    private Bitmap image;

    public Place(String name, Double rating, String photoReference) {
        this.name = name;
        this.rating = rating;
        this.photoReference = photoReference;
        this.imageUrl = urlPrefix + photoReference +urlSuffix;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
