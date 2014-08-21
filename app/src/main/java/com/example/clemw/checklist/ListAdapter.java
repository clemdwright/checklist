package com.example.clemw.checklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private static List<Place> places = new ArrayList<Place>();

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public ListAdapter(Context context) {
        super(context, R.layout.list_item, places);
        Log.i("ListAdapter", context.toString());
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
        // Declare list item layout
        RelativeLayout listItem;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            listItem = (RelativeLayout) layoutInflater.inflate(R.layout.list_item, parent, false);
        } else {
            listItem = (RelativeLayout) convertView;
        }

        // Lookup view for data population
        TextView placeName = (TextView) listItem.findViewById(R.id.place_name);
        // Get the data item for this position
        final Place place = (Place) getItem(position);
        // Populate the data into the template view using the data object
        placeName.setText(place.getName());


        LinearLayout toggleButtons = (LinearLayout) listItem.findViewById(R.id.toggle_buttons);
        final ToggleButton been = (ToggleButton) toggleButtons.findViewById(R.id.been);
//
//        if (place.getBeen()) {
//            been.setChecked(true);
//        } else {
//            been.setChecked(false);
//        }
//
//        been.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    // The toggle is enabled
//                    place.setBeen(true);
//                } else {
//                    // The toggle is disabled
//                    place.setBeen(false);
//                }
//            }
//        });


        // Return the completed view to render on screen
        return listItem;
    }
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