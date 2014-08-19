package com.example.clemw.checklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends Activity {

    private TextView nameView;
    private TextView ratingView;
    private TextView photoReferenceView;

    private String urlPrefix = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        nameView = (TextView) findViewById(R.id.name);
        ratingView = (TextView) findViewById(R.id.rating);
        photoReferenceView = (TextView) findViewById(R.id.photo_reference);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent != null) {
            intent.getExtras();
            String placeId = intent.getExtras().getString("placeName");
            String url = urlPrefix + placeId + urlSuffix;
//            new DownloadDetailsAsyncTask().execute(url);

            DetailsJsonParser atomParser = new DetailsJsonParser(url);
            atomParser.parse(new DetailsJsonParser.ParseCompleteCallback() {
            @Override
            public void onParseComplete(Place place) {
                   //do something
                nameView.setText(place.getName());
                ratingView.setText(place.getRating().toString());
                photoReferenceView.setText(place.getPhotoReference());
            }
        });
        }
    }
}


//    @Override
//    protected void onStart() {
//        super.onStart();
//        DetailsJsonParser atomParser = new DetailsJsonParser();
//        atomParser.parse(new JsonParser.ParseCompleteCallback() {
//            @Override
//            public void onParseComplete(List<String> placeDetails) {
//                   //do something
//            }
//        });
//    }
//}
