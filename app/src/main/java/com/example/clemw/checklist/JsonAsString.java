//package com.example.clemw.checklist;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
//
///**
// * Created by clemw on 8/19/14.
// */
//public class JsonAsString {
//
//    private final String url;
//
//    public JsonAsString(String url) {
//        this.url = url;
//    }
//
//    protected String getJsonAsString() throws IOException {
//        InputStream stream = new URL(url).openConnection().getInputStream();
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(stream, "UTF-8"));
//        StringBuilder result = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            result.append(line);
//        }
//        return result.toString();
//    }
//}