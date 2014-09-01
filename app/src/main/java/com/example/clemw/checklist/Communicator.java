package com.example.clemw.checklist;

/**
 * Created by clemw on 8/31/14.
 */
public interface Communicator {
    public void passPlace(int place_index);


    public void passBeenMarkerState(Boolean isChecked, int placeIndex);
}
