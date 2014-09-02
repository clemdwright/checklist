package com.example.clemw.checklist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesAdapter extends BaseAdapter {

    private final Context context;
    private List<Place> places = new ArrayList<Place>();
    // Maps markers to their corresponding index in the places list
    private HashMap<Marker, Integer> mMarkers = new HashMap();
    private GoogleMap mMap;

    private HashMap<Integer, Marker> mIndexToMarkerMap = new HashMap();

    public PlacesAdapter(Context c) {
        context = c;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public int getIndex(Marker marker) {
        return mMarkers.get(marker);
    }

    public Marker getMarker(int index) { return mIndexToMarkerMap.get(index); }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int index) {
        if (index >= places.size()) {
            return null;
        }
        return places.get(index);
    }

    @Override
    public long getItemId(int index) {
        return 0;
    }



    // Maybe some variable in the adapter keeps track of which place is currently selected, if any


//    //https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
//    //The simpler approach to this (without view caching) is the following:
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        Place place = places.get(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
//        }
//        // Lookup view for data population
//        TextView placeNameView = (TextView) convertView.findViewById(R.id.place_name);
//        // Populate the data into the template view using the data object
//        placeNameView.setText(place.getName());
//        // Return the completed view to render on screen
//        return convertView;
//    }
//}

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) { // create a new view if no recycling available
            textView = new TextView(context);
        } else {
            textView = (TextView) convertView;
        }
        if (index >= places.size()) { // data not yet downloaded!
            return textView;
        }
        Place place = places.get(index);
//        Bitmap bitmap = imageCache.get(imageUrl);
        if (place != null) {
            textView.setText(place.getName());
        } else {
//            if (!downloadingImageUrls.contains(imageUrl)) {
//                Log.i("ImageAdapter", "!downloadingImageUrls.contains(imageUrl). downloading image. index: " + index);
//                downloadingImageUrls.add(imageUrl);
//                new DownloadImageAsyncTask(imageUrl).execute();
//            }
            notifyDataSetChanged();
        }
        return textView;
    }

    // Maybe this should not be in the adapter? Though I guess it means the places are all the same, drawn from the same as the arraylist
    // I just really don't like passing the map around
    // Maybe there's a way to have the map in a fragment or something... murr
    public void mapPlaces(GoogleMap map) {
        map.clear();
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);

            // Get latLng
            LatLng position = place.getPosition();
            // Add each marker to map, ugly
            addNewMarker(map, position, R.drawable.ic_unrated_marker, i);
        }
    }

    public void addNewMarker(GoogleMap map, LatLng position, int drawableId, int index) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(position)
                .anchor(MapUtils.u, MapUtils.v)
                .icon(BitmapDescriptorFactory.fromResource(drawableId)));

        mMarkers.put(marker, index);
        mIndexToMarkerMap.put(index, marker);
    }

    public int getPositionFromMarker(Marker marker) {
        return mMarkers.get(marker);
    }
}