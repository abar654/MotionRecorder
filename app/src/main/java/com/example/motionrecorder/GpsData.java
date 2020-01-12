package com.example.motionrecorder;

import android.location.Location;

//A read only class to store GPS data
class GpsData {

    private double latitude; //In degrees
    private double longitude; //In degrees
    private double altitude; //In m
    private float bearing; //In degrees
    private float speed; //In km/h
    private float accuracy; //In m

    public GpsData(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        bearing = location.getBearing();
        speed = (float) (3.6 * location.getSpeed()); //convert from m/s to km/h
        accuracy = location.getAccuracy();

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getBearing() {
        return bearing;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAccuracy() {
        return accuracy;
    }

}
