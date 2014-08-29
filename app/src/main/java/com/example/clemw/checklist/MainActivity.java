package com.example.clemw.checklist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
 * This the app's main Activity. It displays a list of nearby places.
 */
public class MainActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    // Adapter for the list of nearby places
    private ListAdapter adapter;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Stores the progress bar
    private ProgressBar mActivityIndicator;

    // Stores the map
    private GoogleMap mMap;

    // Stores the focused marker
    private Marker focusedMarker;

    // Maps markers to their corresponding position in the places list
    private HashMap<Marker, Integer> mMarkers = new HashMap();

    // Maps markers to their corresponding place objects
//    private HashMap<Marker, Place> mMarkerPlaceMap = new HashMap();

    // Maps place ids to their corresponding markers
//    private HashMap<String, Marker> mPlaceIdMarkerMap = new HashMap();

    // Store the place name and summary view
    private TextView placeName;
    RelativeLayout summary;

    /*
     * Initialize the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a handle for the UI objects
        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        ListView listView = (ListView) findViewById(R.id.listView);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        // Testing for place summary
        summary = (RelativeLayout) findViewById(R.id.summary);
        placeName = (TextView) findViewById(R.id.place_name);


        // Tweak map settings
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

//        mMap.setOnMarkerClickListener(
//                new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        // Get place id from marker hashmap
//                        Place place = mMarkerPlaceMap.get(marker);
//                        populatePlaceSummary(place);
//                        //        openItemDetails(place.getPlaceId());
//
//                        // Replace the icon of a certain place (test function)
//                        changeClickedMarker(place);
//                        // Add the teardrop marker to show the place is focused
//                        addFocusedMarker(marker);
//                        return true; // We've consumed the event, don't show the info window
//                    }
//                }
//        );

        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Get place id from marker hashmap
                        Integer listPosition = mMarkers.get(marker);
                        Place place = (Place) adapter.getItem(listPosition);
                        populatePlaceSummary(place);
                        //        openItemDetails(place.getPlaceId());

                        // Replace the icon of a certain place (test function)
//                        changeClickedMarker(place);

                        // Add the teardrop marker to show the place is focused
                        addFocusedMarker(marker);
                        return true; // We've consumed the event, don't show the info window
                    }
                }
        );

        // Create a new adapter and set it as the adapter for the ListView
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);

        // Set a click listener for the list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Place place = (Place) adapter.getItem(position);
                String placeId = place.getPlaceId();
                openItemDetails(placeId);
            }
        });

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
    }

    /*
     * Replace the icon of a certain place (test function)
     */
//    private void changeClickedMarker(Place place) {
//        String placeId = place.getPlaceId();
//        Marker oldMarker = mPlaceIdMarkerMap.get(placeId);
//        LatLng position = oldMarker.getPosition();
//        oldMarker.remove();
//        Marker newMarker = mMap.addMarker(new MarkerOptions()
//                .position(position)
//                .anchor(MapUtils.u, MapUtils.v)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_been_marker)));
//
//        mPlaceIdMarkerMap.put(placeId, newMarker);
//        mMarkerPlaceMap.put(newMarker, place);
//    }

    /*
     * Add the teardrop marker to show the place is focused
     */
    private void addFocusedMarker(Marker marker) {
        if (focusedMarker != null) {
            focusedMarker.remove();
        }
        focusedMarker = mMap.addMarker(new MarkerOptions()
                .position(marker.getPosition()));
    }

    /*
     * Fill in the summary view with the appropriate data from the place. Maybe should have an adapter or converter
     */
    private void populatePlaceSummary(Place place) {
        placeName.setText(place.getName());
        summary.setVisibility(View.VISIBLE);
    }

    /*
     * Opens up a place details activity via an intent.
     */
    private void openItemDetails(String placeId) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("place_id", placeId);
        startActivity(intent);
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();

        /*
         * Connect the location client.
         */
        mLocationClient.connect();

    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
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
        Location currentLocation = mLocationClient.getLastLocation();

        // Get the current lat/lng
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        // Zoom map to current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, MapUtils.ZOOM));

        // Construct the Places API request URL
        String url = LocationUtils.getPlacesApiRequest(this, currentLocation);

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

                listNearbyPlaces(places);
                mapNearbyPlaces(places);

            }
        });
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
     * Uses an adapter to populate a ListView with nearby places.
     */
    private void listNearbyPlaces(List<Place> places) {
        adapter.setPlaces(places);
        adapter.notifyDataSetChanged();
    }

    /*
     * Creates a marker for each nearby place and adds it to the map.
     *
     * This might need an adapter or something to move it off the main thread.
     */
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

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
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
