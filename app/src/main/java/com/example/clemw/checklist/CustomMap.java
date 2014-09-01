package com.example.clemw.checklist;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by clemw on 9/1/14.
 */
public class CustomMap extends MapFragment {
    private GoogleMap mMap;
    private Marker focusedMarker;
    private Communicator communicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
//                        int index = adapter.getIndex(marker);
////                        Place place = (Place) adapter.getItem(index);
//
//                        communicator.passPlaceIndex(index);
//
////                        populatePlaceSummary(place);
//                        //        openItemDetails(place.getPlaceId());
//
//                        // Replace the icon of a certain place (test function)
////                        changeClickedMarker(place);
//
                        // Add the teardrop marker to show the place is focused
                        addFocusedMarker(marker.getPosition());
                        return true; // We've consumed the event, don't show the info window
                    }
                }
        );
    }

    public void setLocation(LatLng currentLatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, MapUtils.ZOOM));
    }

/*
 * Add the teardrop marker to show the place is focused
 */
    private void addFocusedMarker(LatLng position) {
        if (focusedMarker != null) {
            focusedMarker.remove();
        }
        focusedMarker = mMap.addMarker(new MarkerOptions()
                .position(position));
    }
}
