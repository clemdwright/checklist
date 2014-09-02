package com.example.clemw.checklist;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
    private Double latitude;
    private Double longitude;
    private final String urlPrefix = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private final String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";
    private String imageUrl;
    private LatLng position;
    private Boolean been = false;

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
            Log.e("Place", "Error parsing place ratingView from JSON");
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

        try {
            JSONObject geometry = place.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            this.latitude = location.getDouble("lat");
            this.longitude = location.getDouble("lng");
        } catch (Exception e) {
            this.latitude = null;
            this.longitude = null;
            Log.e("Place", "Error parsing place latLng from JSON");
        }

        this.position = new LatLng(this.latitude, this.longitude);

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

    public LatLng getPosition() {
        return this.position;
    }

//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }

    public String getImageUrl() {
        return imageUrl;
    }
//
    public void setBeen(Boolean been) {
        this.been = been;
    }

    public Boolean getBeen() {
        return this.been;
    }
}
