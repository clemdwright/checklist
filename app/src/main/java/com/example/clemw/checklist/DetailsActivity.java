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

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DetailsActivity extends Activity {

    private TextView nameView;
    private TextView ratingView;
    private TextView priceLevelView;
    private ImageView photoView;

    private final String urlPrefix = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private final String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        nameView = (TextView) findViewById(R.id.name);
        ratingView = (TextView) findViewById(R.id.rating);
        priceLevelView = (TextView) findViewById(R.id.price_level);
        photoView = (ImageView) findViewById(R.id.photo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent != null) {
            intent.getExtras();
            String placeId = intent.getExtras().getString("place_id");
            String url = urlPrefix + placeId + urlSuffix;

            JsonParser atomParser = new JsonParser(url);
            atomParser.parse(new JsonParser.ParseCompleteCallback() {

                @Override
                public void onParseComplete(JSONObject jsonObject) {
                    Place place = parsePlace(jsonObject);
                    populateViews(place);
                }
            });
        }
    }

    private Place parsePlace(JSONObject jsonObject) {
        JSONObject jsonPlace;
        try {
            jsonPlace = jsonObject.getJSONObject("result");
        } catch (Exception e) {
            jsonPlace = null;
            Log.e("DetailsActivity", "Error parsing place from JSON");
        }
        Place place = new Place(jsonPlace);
        return place;
    }

    private void populateViews(Place place) {
        String name = place.getName();
        setTitle(name);
        nameView.setText(name);
        Double rating = place.getRating();
        Double price_level = place.getPriceLevel();
        if (rating != null) ratingView.setText(rating.toString());
        if (price_level != null) priceLevelView.setText(price_level.toString());
        new DownloadImageAsyncTask().execute(place.getImageUrl());
    }

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