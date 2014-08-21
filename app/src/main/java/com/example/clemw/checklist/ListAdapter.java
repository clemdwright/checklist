package com.example.clemw.checklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
        // Get the data item for this position
        Place place = (Place) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        LinearLayout toggleButtons = (LinearLayout) convertView.findViewById(R.id.toggle_buttons);
        final ToggleButton been = (ToggleButton) toggleButtons.findViewById(R.id.been);
        final ToggleButton save = (ToggleButton) toggleButtons.findViewById(R.id.save);
        final ToggleButton love = (ToggleButton) toggleButtons.findViewById(R.id.love);

        been.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    save.setVisibility(View.GONE);
                    love.setVisibility(View.VISIBLE);
                    save.setChecked(false);
                } else {
                    // The toggle is disabled
                    save.setVisibility(View.VISIBLE);
                    love.setVisibility(View.GONE);
                }
            }
        });

        love.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    been.setVisibility(View.GONE);
                } else {
                    // The toggle is disabled
                    been.setVisibility(View.VISIBLE);
                }
            }
        });

        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    been.setChecked(false);
                } else {
                    // The toggle is disabled
                }
            }
        });


        // Lookup view for data population
        TextView placeName = (TextView) convertView.findViewById(R.id.place_name);
        // Populate the data into the template view using the data object
        placeName.setText(place.getName());
        // Return the completed view to render on screen
        return convertView;
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