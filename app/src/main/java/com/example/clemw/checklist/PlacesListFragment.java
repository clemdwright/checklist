package com.example.clemw.checklist;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

/**
 * Created by clemw on 9/1/14.
 */
public class PlacesListFragment extends ListFragment {

    private PlacesAdapter adapter;
    private Communicator communicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PlacesAdapter(getActivity());
        setListAdapter(adapter);

        communicator = (Communicator) getActivity();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Place place = (Place) adapter.getItem(position);
//        String placeId = place.getPlaceId();
        communicator.passPlace(place, position);
    }

    public void setPlaces(List<Place> places) {
        adapter.setPlaces(places);
        adapter.notifyDataSetChanged();
    }

}
