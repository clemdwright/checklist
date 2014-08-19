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
import java.util.Collections;
import java.util.List;

public class DetailsJsonParser {

    private String url;

    public DetailsJsonParser(String url) {
        this.url = url;
    }

    public interface ParseCompleteCallback {
        void onParseComplete(List<String> places);
    }

    protected String getJsonAsString() throws IOException {
        InputStream stream = new URL(url).openConnection().getInputStream();
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
        new AsyncTask<Void, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    String jsonString = getJsonAsString();
                    JSONObject feed = new JSONObject(jsonString);
                    JSONObject place = feed.getJSONObject("result");

                    String name = place.getString("name");
                    Double rating = place.getDouble("rating");
                    JSONArray photos = place.getJSONArray("photos");
                    JSONObject firstPhoto = photos.getJSONObject(0);
                    String photoReference = firstPhoto.getString("photo_reference");

                    Log.i("DetailsJsonParser", name);

                    Place result = new Place(name);

                    Log.i("DetailsJsonParser", result.getName());

                    //add this stuff to the result?

                    return Collections.emptyList();
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