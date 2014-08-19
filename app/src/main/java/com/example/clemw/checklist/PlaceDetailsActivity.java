package com.example.clemw.checklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceDetailsActivity extends Activity {
    private TextView textView;

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
            String placeName = intent.getExtras().getString("placeName");
            textView.setText(placeName);
        }
    }
}
