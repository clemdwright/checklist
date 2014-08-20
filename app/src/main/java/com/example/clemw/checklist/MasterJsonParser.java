package com.example.clemw.checklist;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


/*
 *  Tried building this to reduce duplication in json parsers
 *  Unfortunately, once i passed the json object to the other classes,
 *  I got an unhandled json exception when trying to getString etc.
 *  For some reason it's necessary to do the parsing... wait... maybe if i add a try?
 *  Yeah, adding a try got past some off the errors, but still had issues...
 */


public class MasterJsonParser {

    private String url;

    public MasterJsonParser(String url) {
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