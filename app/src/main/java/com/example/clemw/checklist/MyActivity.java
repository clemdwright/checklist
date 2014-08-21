package com.example.clemw.checklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyActivity extends Activity {

    private ListAdapter adapter;
    private static final String url = "https://maps.googleapis.com/"
            + "maps/api/place/nearbysearch/json?location=37.760107,-122.425908"
            + "&radius=500&types=food&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(MyActivity.this, DetailsActivity.class);
                Place place = (Place) adapter.getItem(position);
                String place_id = place.getPlaceId();
                intent.putExtra("place_id", place_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        JsonParser atomParser = new JsonParser(url);
        atomParser.parse(new JsonParser.ParseCompleteCallback() {

            public void onParseComplete(JSONObject jsonObject) {
                List<Place> places = parsePlacesList(jsonObject);
                adapter.setPlaceNames(places);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<Place> parsePlacesList(JSONObject jsonObject) {
        List<Place> list = new ArrayList<Place>();
        try {
            JSONArray places = jsonObject.getJSONArray("results");
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                Place item = new Place(place);
                list.add(item);
            }
            return list;
        } catch (Exception e) {
            Log.e("JsonParser", "Error creating list from parsed JSON");
            return Collections.emptyList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
