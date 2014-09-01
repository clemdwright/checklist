package com.example.clemw.checklist;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by clemw on 8/31/14.
 */
public interface Communicator {
    public void passPlaceIndex(int place_index);


    public void passBeenMarkerState(Boolean isChecked, int placeIndex);

    public void passCurrentLocation(LatLng currentLocation);

    public void passPlaces(List<Place> places);
}
