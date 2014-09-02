package com.example.clemw.checklist;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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

        communicator = (Communicator) getActivity();

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
//                        int index = adapter.getIndex(marker);
////                        Place place = (Place) adapter.getItem(index);
//
                        communicator.passMarker(marker);

                        // Add the teardrop marker to show the place is focused
                        addFocusedMarker(marker.getPosition());
                        return true; // We've consumed the event, don't show the info window
                    }
                }
        );


        //This updates too frequently, mid-pan
        //This article claims to solve it: http://dimitar.me/how-to-detect-a-user-pantouchdrag-on-android-map-v2/
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                LatLng target = position.target;
                float zoom = position.zoom;

                Log.d("CustomMap", target.toString());
                Log.d("CustomMap", Float.toString(zoom));

                communicator.passCameraPosition(target, zoom);


            }
        });
    }

    public void setLocation(LatLng currentLatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, MapUtils.ZOOM));
    }


/*
 * Add the teardrop marker to show the place is focused
 */
    public void addFocusedMarker(LatLng position) {
        if (focusedMarker != null) {
            focusedMarker.remove();
        }
        focusedMarker = mMap.addMarker(new MarkerOptions()
                .position(position));
    }
}
