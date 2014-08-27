package com.example.clemw.checklist;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.location.Location;

/**
 * Defines app-wide constants and utilities
 */
public final class LocationUtils {

    // Debugging tag for the application
    public static final String APPTAG = "Checklist";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();

    // Prefix and suffix for constructing the Places API request
    private static final String urlPrefix = "https://maps.googleapis.com/"
            + "maps/api/place/nearbysearch/json?location=";
    private static final String urlSuffix = "&radius=500&types=food&key="
            + "AIzaSyDSxGRYQXuA7qy3Rzcu1zILt2hAqbNcHaM";


    /**
     * Construct the Places API request using the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return A url for the Places API request, or null if no location is available.
     */
    public static String getPlacesApiRequest(Context context, Location currentLocation) {
        // If the location is valid
        if (currentLocation != null) {

            Double latDouble = currentLocation.getLatitude();
            String latString = latDouble.toString();
            Double lonDouble = currentLocation.getLongitude();
            String lonString = lonDouble.toString();
            String url = urlPrefix + latString + "," + lonString + urlSuffix;
            return url;

        } else {

            // Otherwise, return the empty string
            return EMPTY_STRING;
        }
    }
}
