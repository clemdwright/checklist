package com.example.clemw.checklist;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by clemw on 8/19/14.
 */
public class Place extends Object {
    private String name;
    private Double price_level;
    private Double rating;
    private String place_id;
    private final String urlPrefix = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private final String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";
    private String imageUrl;
//    private Boolean been = false;

    public Place(JSONObject place) {
        try {
            this.name = place.getString("name");
            this.place_id = place.getString("place_id");
            Log.i("Place", this.name);
        } catch (Exception e) {
            this.name = null;
            this.place_id = null;
            Log.e("Place", "Error parsing place name or id from JSON");
        }

        try {
            this.price_level = place.getDouble("price_level");
        } catch (Exception e) {
            this.price_level = null;
            Log.e("Place", "Error parsing place price level from JSON");
        }

        try {
            this.rating = place.getDouble("rating");
        } catch (Exception e) {
            this.rating = null;
            Log.e("Place", "Error parsing place rating from JSON");
        }

        try {
            JSONArray photos = place.getJSONArray("photos");
            JSONObject firstPhoto = photos.getJSONObject(0);
            String photo_reference = firstPhoto.getString("photo_reference");
            this.imageUrl = urlPrefix + photo_reference + urlSuffix;
        } catch (Exception e) {
            this.imageUrl = null;
            Log.e("Place", "Error parsing place photo from JSON");
        }
    }


    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return place_id;
    }

    public Double getPriceLevel() {
        return price_level;
    }

    public Double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }
//
//    public void setBeen(Boolean been) {
//        this.been = been;
//    }
//
//    public Boolean getBeen() {
//        return this.been;
//    }
}
