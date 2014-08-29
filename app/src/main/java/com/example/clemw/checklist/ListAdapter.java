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

public class ListAdapter extends BaseAdapter {

    private final Context context;
    private List<Place> places = new ArrayList<Place>();
    // Maps markers to their corresponding position in the places list
    private HashMap<Marker, Integer> mMarkers = new HashMap();
    // Stores the map
//    private GoogleMap mMap;

    public ListAdapter(Context c) {
        context = c;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public int getPosition(Marker marker) {
        return mMarkers.get(marker);
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
//        TextView placeName = (TextView) convertView.findViewById(R.id.place_name);
//        // Populate the data into the template view using the data object
//        placeName.setText(place.getName());
//        // Return the completed view to render on screen
//        return convertView;
//    }
//}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) { // create a new view if no recycling available
            textView = new TextView(context);
        } else {
            textView = (TextView) convertView;
        }
        if (position >= places.size()) { // data not yet downloaded!
            return textView;
        }
        Place place = places.get(position);
//        Bitmap bitmap = imageCache.get(imageUrl);
        if (place != null) {
            textView.setText(place.getName());
        } else {
//            if (!downloadingImageUrls.contains(imageUrl)) {
//                Log.i("ImageAdapter", "!downloadingImageUrls.contains(imageUrl). downloading image. position: " + position);
//                downloadingImageUrls.add(imageUrl);
//                new DownloadImageAsyncTask(imageUrl).execute();
//            }
            notifyDataSetChanged();
        }
        return textView;
    }

    public void mapPlaces(GoogleMap map) {
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);

            // Get latLng
            LatLng position = place.getPosition();

            // Add marker to the map
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(position)
                    .anchor(MapUtils.u, MapUtils.v)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_save_marker)));

            mMarkers.put(marker, i);
        }
    }


}
//
//    private void mapNearbyPlaces(List<Place> places) {
//        for (Place place : places) {
//            // Get placeId
//            String placeId = place.getPlaceId();
//
//            // Get latLng
//            Double latitude = place.getLatitude();
//            Double longitude = place.getLongitude();
//
//            // Add marker to the map
//            Marker marker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(latitude, longitude))
//                    .anchor(MapUtils.u, MapUtils.v)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_save_marker)));
//
//            // Add marker and place to hashmaps for retrieving later
//            mMarkers.put(marker, place);
////            mPlaceIdMarkerMap.put(placeId, marker);
//
//        }
//    }
//}