package com.example.clemw.checklist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DetailsActivity extends Activity {

    private Place place;

    private TextView nameView;
    private TextView ratingView;
    private TextView photoReferenceView;
    private ImageView photoView;

    private final String urlPrefix = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private final String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        nameView = (TextView) findViewById(R.id.name);
        ratingView = (TextView) findViewById(R.id.rating);
        photoView = (ImageView) findViewById(R.id.photo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent != null) {
            intent.getExtras();
            String placeId = intent.getExtras().getString("placeName");
            String url = urlPrefix + placeId + urlSuffix;

            DetailsJsonParser atomParser = new DetailsJsonParser(url);
            atomParser.parse(new DetailsJsonParser.ParseCompleteCallback() {

                @Override
                public void onParseComplete(Place place) {
                    nameView.setText(place.getName());
                    ratingView.setText(place.getRating().toString());
//                    photoReferenceView.setText(place.getPhotoReference());
                    Log.i("imageUrl", place.getImageUrl());
                    new DownloadImageAsyncTask().execute(place.getImageUrl());
                }
            });
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        Intent intent = getIntent();
//        if (intent != null) {
//            intent.getExtras();
//            String placeId = intent.getExtras().getString("placeName");
//            String url = urlPrefix + placeId + urlSuffix;
//
//            MasterJsonParser atomParser = new MasterJsonParser(url);
//            atomParser.parse(new MasterJsonParser.ParseCompleteCallback() {
//
//                @Override
//                public void onParseComplete(JSONObject jsonObject) {
//
//                    try {
//                        String name = jsonObject.getString("name");
//                        Double rating = jsonObject.getDouble("rating");
//                        JSONArray photos = jsonObject.getJSONArray("photos");
//                        JSONObject firstPhoto = photos.getJSONObject(0);
//                        String photoReference = firstPhoto.getString("photo_reference");
//
//                        place = new Place(name, rating, photoReference);
//                    } catch (Exception e) {
//                        Log.e("DetailsActivity", "error parsing json", e);
//                    }
//
//                    nameView.setText(place.getName());
//                    ratingView.setText(place.getRating().toString());
//                    Log.i("imageUrl", place.getImageUrl());
//                    new DownloadImageAsyncTask().execute(place.getImageUrl());
//                }
//            });
//        }
//    }

    class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                return BitmapFactory.decodeStream(
                        (InputStream) new URL(url).getContent());
            } catch (IOException e) {
                Log.e("DownloadImageAsyncTask", "Error reading bitmap", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                photoView.setImageBitmap(result);
            }
        }
    }
}