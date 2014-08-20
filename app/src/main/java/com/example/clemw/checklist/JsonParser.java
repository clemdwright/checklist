package com.example.clemw.checklist;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonParser {
    public interface ParseCompleteCallback {
        void onParseComplete(List<Place> places);
    }

    //    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.760107,-122.425908&radius=500&types=food&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM
    private static final String PLACE_FEED_URL = "https://maps.googleapis.com/"
            + "maps/api/place/nearbysearch/json?location=37.760107,-122.425908"
            + "&radius=500&types=food&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    protected String getJsonAsString() throws IOException {
        InputStream stream = new URL(PLACE_FEED_URL).openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public void parse(final ParseCompleteCallback parseCompleteCallback) {
        new AsyncTask<Void, Void, List<Place>>() {

            @Override
            protected List<Place> doInBackground(Void... params) {
                try {
                    String jsonString = getJsonAsString();

                    JSONObject feed = new JSONObject(jsonString);
                    JSONArray places = feed.getJSONArray("results");
                    List<Place> list = new ArrayList<Place>();

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
            protected void onPostExecute(List<Place> result) {
                parseCompleteCallback.onParseComplete(result);
            }
        }.execute();
    }
}