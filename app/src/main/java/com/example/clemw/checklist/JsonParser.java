package com.example.clemw.checklist;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class JsonParser {

    private String url;

    public JsonParser(String url) {
        this.url = url;
    }

    public interface ParseCompleteCallback {
        void onParseComplete(JSONObject result);
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
        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    String jsonString = getJsonAsString();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    return jsonObject;
                } catch (Exception e) {
                    return null; //probably need to return an empty place object somehow
                }
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                parseCompleteCallback.onParseComplete(result);
            }
        }.execute();
    }
}