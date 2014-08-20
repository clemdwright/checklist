package com.example.clemw.checklist;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class DetailsJsonParser {

    private String url;

    public DetailsJsonParser(String url) {
        this.url = url;
    }

    public interface ParseCompleteCallback {
        void onParseComplete(Place result);
    }

    //This code is duplicated between both JSON parsers
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

    //This code is duplicated too... maybe find way for it to return abstract type?
    public void parse(final ParseCompleteCallback parseCompleteCallback) {
        new AsyncTask<Void, Void, Place>() {

            @Override
            protected Place doInBackground(Void... params) {
                try {
                    String jsonString = getJsonAsString();
                    JSONObject feed = new JSONObject(jsonString);
                    JSONObject place = feed.getJSONObject("result");
                    Place result = new Place(place);
                    return result;
                } catch (Exception e) {
                    return null; //probably need to return an empty place object somehow
                }
            }

            @Override
            protected void onPostExecute(Place result) {
                parseCompleteCallback.onParseComplete(result);
            }
        }.execute();
    }
}