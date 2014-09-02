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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PlacesAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        showDetails(position);
//        Place place = (Place) adapter.getItem(index);
//        String placeId = place.getPlaceId();
        // open place summary?
    }

    public void setPlaces(List<Place> places) {
        adapter.setPlaces(places);
        adapter.notifyDataSetChanged();
    }

}
