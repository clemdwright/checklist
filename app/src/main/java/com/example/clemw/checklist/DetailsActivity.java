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
        photoReferenceView = (TextView) findViewById(R.id.photo_reference);
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