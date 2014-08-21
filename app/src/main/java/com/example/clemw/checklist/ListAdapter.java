package com.example.clemw.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter {



    private final Context context;
    private static List<Place> places = new ArrayList<Place>();

//    public ListAdapter(Context c) {
//        context = c;
//    }


    public void setPlaceNames(List<Place> places) {
        this.places = places;
    }

    public ListAdapter(Context context) {
        super(context, R.layout.list_item, places);
        this.context = context;
    }


    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= places.size()) {
            return null;
        }
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    //https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
    //The simpler approach to this (without view caching) is the following:
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Place place = places.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView placeName = (TextView) convertView.findViewById(R.id.place_name);
        // Populate the data into the template view using the data object
        placeName.setText(place.getName());
        // Return the completed view to render on screen
        return convertView;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView textView;
//        if (convertView == null) { // create a new view if no recycling available
//            textView = new TextView(context);
//        } else {
//            textView = (TextView) convertView;
//        }
//        if (position >= places.size()) { // data not yet downloaded!
//            return textView;
//        }
//        Place place = places.get(position);
////        Bitmap bitmap = imageCache.get(imageUrl);
//        if (place != null) {
//            textView.setText(place.getName());
//        } else {
////            if (!downloadingImageUrls.contains(imageUrl)) {
////                Log.i("ImageAdapter", "!downloadingImageUrls.contains(imageUrl). downloading image. position: " + position);
////                downloadingImageUrls.add(imageUrl);
////                new DownloadImageAsyncTask(imageUrl).execute();
////            }
//            notifyDataSetChanged();
//        }
//        return textView;
//    }



}
