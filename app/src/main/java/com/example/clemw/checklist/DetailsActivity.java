package com.example.clemw.checklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class DetailsActivity extends Activity {

    private TextView textView;

    private String urlPrefix = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private String urlSuffix = "&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        textView = (TextView) findViewById(R.id.place_name);
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
            public void onParseComplete(List<String> placeDetails) {
                   //do something
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
