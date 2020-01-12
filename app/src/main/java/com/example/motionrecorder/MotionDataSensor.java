package com.example.motionrecorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

class MotionDataSensor extends LocationCallback implements SensorEventListener {

    private MotionDataSensorListener listener;
    private SensorManager sensorManager;
    private Sensor accelSensor;

    private FusedLocationProviderClient fusedLocationClient;

    private static final int SAMPLING_PERIOD = 100000;

    public interface MotionDataSensorListener {

        void onGpsDataUpdate(GpsData data);
        void onAccelDataUpdate(AccelData data);
        void onError(String message);

    }

    public MotionDataSensor(Context context, MotionDataSensorListener listener, boolean gpsPermission) {

        this.listener = listener;

        //Start the GpsSensor
        if(gpsPermission) {

            //Get the client
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

            //Create a LocationRequest
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(SAMPLING_PERIOD/10000);
            locationRequest.setFastestInterval(SAMPLING_PERIOD/10000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            //Now use the client to set this object to get GPS updates
            fusedLocationClient.requestLocationUpdates(locationRequest, this, Looper.getMainLooper());

        }

        //Start the AccelSensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accelSensor, SAMPLING_PERIOD, SAMPLING_PERIOD);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Accelerometer data updated, create a new object
        AccelData update = new AccelData(event.values[0], event.values[1], event.values[2]);

        //Send this data to the listener
        listener.onAccelDataUpdate(update);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Don't need to do anything
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {

        if(locationResult != null) {
            Location latest = locationResult.getLastLocation();
            if(latest != null) {
                GpsData update = new GpsData(latest);
                listener.onGpsDataUpdate(update);
            }
        }

    }

}
