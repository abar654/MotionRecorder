package com.example.motionrecorder;

import android.content.Context;
import android.location.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

class DashboardInteractor implements MotionDataSensor.MotionDataSensorListener {

    public interface MotionDataUpdatedListener {

        void onGpsDataUpdate(GpsData data);
        void onAccelDataUpdate(AccelData data);
        void onError(String message);

    }

    private Context context;
    private ArrayList<MotionDataUpdatedListener> listeners;
    private Recorder recorder;
    private MotionDataSensor sensor;
    private AccelData currentAccelData;
    private GpsData currentGpsData;

    public DashboardInteractor(Context context, boolean gpsPermission) {

        //Create the listeners list
        listeners = new ArrayList<MotionDataUpdatedListener>();

        this.context = context;

        recorder = null;

        //Set initial values for Gps and Accel Data
        currentAccelData = new AccelData(0, 0, 0);
        currentGpsData = new GpsData();

        //Create a sensor object
        sensor = new MotionDataSensor(context, this, gpsPermission);

    }

    public void addMotionDataUpdatedListener(MotionDataUpdatedListener listener) {
        listeners.add(listener);
    }

    public void startRecording() {

        //Create a Recorder object, if not already existing
        if(recorder == null) {

            //Create the File object to pass to the recorder
            //Filename is the name set in strings resource file + current time in millis
            File outputFile = new File(context.getExternalFilesDir(null),
                    context.getString(R.string.data_filename) + Calendar.getInstance().getTimeInMillis() + ".csv");

            recorder = new Recorder(currentAccelData, currentGpsData, outputFile);

        }

        //Add Recorder as a listener
        listeners.add(recorder);

        //Start recording
        recorder.start();

    }

    public int stopRecording() {

        //Number of seconds recorded
        int seconds = 0;

        //If recorder object exists stop it recording
        //Must check because this may be called without recording starting
        if(recorder != null) {

            //Stop the recording
            seconds = recorder.stop();

            //Remove from the listeners list
            listeners.remove(recorder);

            //Set recorder to null
            recorder = null;

        }

        return seconds;

    }

    @Override
    public void onGpsDataUpdate(GpsData data) {
        currentGpsData = data;
        for(MotionDataUpdatedListener listener: listeners) {
            listener.onGpsDataUpdate(data);
        }
    }

    @Override
    public void onAccelDataUpdate(AccelData data) {
        currentAccelData = data;
        for(MotionDataUpdatedListener listener: listeners) {
            listener.onAccelDataUpdate(data);
        }
    }

    @Override
    public void onError(String message) {
        for(MotionDataUpdatedListener listener: listeners) {
            listener.onError(message);
        }
    }


}
