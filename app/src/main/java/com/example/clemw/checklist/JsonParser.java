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
        void onParseComplete(List<String> places);
    }

    protected String getJsonAsString() throws IOException {
        Log.i("jsonParser", "getJsonAsString fires");

        InputStream stream = new URL(PLACE_FEED_URL).openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line;



        while ((line = reader.readLine()) != null) {
            Log.i("jsonParser", line);
            result.append(line);
        }
        return result.toString();
    }

//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.760107,-122.425908&radius=500&types=food&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM
    private static final String PLACE_FEED_URL = "https://maps.googleapis.com/"
            + "maps/api/place/nearbysearch/json?location=37.760107,-122.425908"
            + "&radius=500&types=food&key=AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";

    public void parse(final ParseCompleteCallback parseCompleteCallback) {

        Log.i("jsonParser", "parse fires");

        new AsyncTask<Void, Void, List<String>>() {



            @Override
            protected List<String> doInBackground(Void... params) {

                Log.i("jsonParser", "do in background fires");

                try {

                    Log.i("jsonParser", "try fires");


                    String jsonString = getJsonAsString();

                    Log.i("jsonString size", Integer.toString(jsonString.length())); //this is currently zero

                    Log.i("jsonString", jsonString); //logging string
                    JSONObject feed = new JSONObject(jsonString);
                    JSONArray places = feed.getJSONArray("results");
                    List<String> result = new ArrayList<String>();

                    for (int i = 0; i < places.length(); i++) {
                        JSONObject place = places.getJSONObject(i);
                        String name = place.getString("place_id");
                        Log.i("placeName", name); //logging place name
                        result.add(name);
                    }

                    return result;
                } catch (Exception e) {
                    return Collections.emptyList();
                }
            }

            @Override
            protected void onPostExecute(List<String> result) {
                parseCompleteCallback.onParseComplete(result);
            }
        }.execute();
    }
}