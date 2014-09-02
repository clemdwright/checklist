package com.example.clemw.checklist;

import android.app.ListFragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * Created by clemw on 9/1/14.
 */
public class PlacesListFragment extends ListFragment {

    private PlacesAdapter adapter;
    private Communicator communicator;
    private GoogleMap mMap;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        adapter = new PlacesAdapter(getActivity());
        setListAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                adapter.mapPlaces(mMap);
            }
        });
        communicator = (Communicator) getActivity();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        setDetails(position);
    }

    public void setDetails(int position) {
        Place place = (Place) adapter.getItem(position);
        communicator.passPlace(place, position);
    }

    public void setDetailsFromMarker(Marker marker) {
        int position = adapter.getPositionFromMarker(marker);
        setDetails(position);
    }

    public void setPlaces(List<Place> places) {
        adapter.setPlaces(places);
        adapter.notifyDataSetChanged();
    }



}
