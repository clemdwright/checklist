package com.example.clemw.checklist;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * This the app's main Activity. It displays a list of nearby places.
 */
public class MainActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener,
        Communicator {

    protected static final String TAG = "MainActivity";

    /*
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    //    private LocationClient mLocationClient;
    private ProgressBar mActivityIndicator;
    Communicator communicator;

    /*
     * Initialize the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        communicator = this;

        // Get a handle for the UI objects
        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);

        buildGoogleApiClient();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /*
     * Handle results returned to the FragmentActivity by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    /*
                     * Try the request again
                     */
                        break;
                }
        }
    }

    /*
    * Called by Location Services when the request to connect the
    * client finishes successfully. At this point, you can
    * request the current location or start periodic updates
    */
    @Override
    public void onConnected(Bundle dataBundle) {

        // Get the current location
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (currentLocation != null) {

            // Get the current lat/lng
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            // Zoom map to current location
            communicator.passCurrentLocation(currentLatLng);

            // Construct the Places API request URL
            String url = LocationUtils.getPlacesApiRequest(this, currentLatLng);

            // Get nearby places
            getNearbyPlaces(url);

        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void passCameraPosition(LatLng location, float zoom) {
        // Construct the Places API request URL
        String url = LocationUtils.getPlacesApiRequest(this, location);

        // Get nearby places
        getNearbyPlaces(url);
    }

    /*
     * Called by onConnected when there is a current location.
     */
    private void getNearbyPlaces(String url) {
        JsonParser atomParser = new JsonParser(url);
        atomParser.parse(new JsonParser.ParseCompleteCallback() {

            public void onParseComplete(JSONObject jsonObject) {
                List<Place> places = parsePlacesList(jsonObject);
                // Turn the indefinite activity indicator off
                mActivityIndicator.setVisibility(View.GONE);

                communicator.passPlaces(places);
            }
        });
    }

    @Override
    public void passPlaces(List<Place> places) {
        FragmentManager fragmentManager = getFragmentManager();
        PlacesListFragment placesListFragment = (PlacesListFragment) fragmentManager.findFragmentById(R.id.list);
        placesListFragment.setPlaces(places);
    }

    /*
     * Called by getNearbyPlaces to parse the results of the Places API request.
     */
    private List<Place> parsePlacesList(JSONObject jsonObject) {
        List<Place> list = new ArrayList<Place>();
        try {
            JSONArray places = jsonObject.getJSONArray("results");
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                Place item = new Place(place);
                list.add(item);
            }
            return list;
        } catch (Exception e) {
            Log.e("JsonParser", "Error creating list from parsed JSON");
            return Collections.emptyList();
        }
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends android.support.v4.app.DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    @Override
    public void passBeenMarkerState(Boolean isChecked, int placeIndex) {
        FragmentManager fragmentManager = getFragmentManager();
        CustomMap customMap = (CustomMap) fragmentManager.findFragmentById(R.id.map);
//        customMap.updateMarker(isChecked, placeIndex);

//
//
//        Place place = (Place) adapter.getItem(placeIndex);
//
//        place.setBeen(isChecked);
//
//        //  get the marker for the current place from the hashmap
//        Marker oldMarker = adapter.getMarker(placeIndex);
//        //  get the position for the current marker
//        LatLng position = oldMarker.getPosition();
//        //  remove the old marker
//        oldMarker.remove();
//        //  add the new marker
//
//        if (isChecked == true) {
//            adapter.addNewMarker(mMap, position, R.drawable.ic_been_marker, placeIndex);
//        } else {
//            adapter.addNewMarker(mMap, position, R.drawable.ic_unrated_marker, placeIndex);
//        }
//
//        focusedMarker.remove();
//        addFocusedMarker(position);
//    }
    }

    @Override
    public void passPlaceIndex(int placeIndex) {
//        FragmentManager fragmentManager = getFragmentManager();
//        DetailsFragment detailsFragment = (DetailsFragment) fragmentManager.findFragmentById(R.id.details);
//        Place place = (Place) adapter.getItem(placeIndex);
//        detailsFragment.setPlace(placeIndex, place);
    }

    @Override
    public void passPlace(Place place, int placeIndex) {
        FragmentManager fragmentManager = getFragmentManager();
        DetailsFragment detailsFragment = (DetailsFragment) fragmentManager.findFragmentById(R.id.details);
        detailsFragment.setPlace(place, placeIndex);

        LatLng location = place.getPosition();
        CustomMap customMap = (CustomMap) fragmentManager.findFragmentById(R.id.map);
        customMap.addFocusedMarker(location);
        customMap.animateCamera(location);
    }

    @Override
    public void passCurrentLocation(LatLng currentLocation) {
        FragmentManager fragmentManager = getFragmentManager();
        CustomMap customMap = (CustomMap) fragmentManager.findFragmentById(R.id.map);
        customMap.setLocation(currentLocation);
    }

    @Override
    public void passMarker(Marker marker) {
        FragmentManager fragmentManager = getFragmentManager();
        PlacesListFragment placesListFragment = (PlacesListFragment) fragmentManager.findFragmentById(R.id.list);
        placesListFragment.setDetailsFromMarker(marker);
    }

}


//    /**
//     * Verify that Google Play services is available before making a request.
//     *
//     * @return true if Google Play services is available, otherwise false
//     */
//    private boolean servicesConnected() {
//
//        // Check that Google Play services is available
//        int resultCode =
//                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//            // In debug mode, log the status
//            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));
//
//            // Continue
//            return true;
//            // Google Play services was not available for some reason
//        } else {
//            // Display an error dialog
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
//            if (dialog != null) {
//                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
//                errorFragment.setDialog(dialog);
//                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
//            }
//            return false;
//        }
//    }
//}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}