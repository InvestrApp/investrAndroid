package com.investrapp.investr.utils;

/**
 * Created by michaelsignorotti on 10/28/17.
 */

public class DistanceUtils {
    /**
     * This method is outlined in the following Stack Overflow post. The purpose of this method is to
     * return the distance between two pairs of latitude, longitude coordinates in miles.
     *
     *https://stackoverflow.com/questions/120283/how-can-i-measure-distance-and-create-a-bounding-box-based-on-two-latitudelongi
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist;
    }

}
