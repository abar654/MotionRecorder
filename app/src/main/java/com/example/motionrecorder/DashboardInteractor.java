package com.example.motionrecorder;

import android.content.Context;

import java.util.ArrayList;

class DashboardInteractor implements MotionDataSensor.MotionDataSensorListener {

    public interface MotionDataUpdatedListener {

        void onGpsDataUpdate(GpsData data);
        void onAccelDataUpdate(AccelData data);
        void onError(String message);

    }

    private ArrayList<MotionDataUpdatedListener> listeners;
    private Recorder recorder;
    private MotionDataSensor sensor;

    public DashboardInteractor(Context context, boolean gpsPermission) {

        //Create the listeners list
        listeners = new ArrayList<MotionDataUpdatedListener>();

        recorder = null;

        sensor = new MotionDataSensor(context, this, gpsPermission);

    }

    public void addMotionDataUpdatedListener(MotionDataUpdatedListener listener) {
        listeners.add(listener);
    }

    public void startRecording() {

        //Create a Recorder object, if not already existing
        //TODO

        //Start recording

    }

    public void stopRecording() {

        //If recorder object exists stop it recording
        //Must check because this may be called without recording starting

        //Set recorder to null

    }

    @Override
    public void onGpsDataUpdate(GpsData data) {
        for(MotionDataUpdatedListener listener: listeners) {
            listener.onGpsDataUpdate(data);
        }
    }

    @Override
    public void onAccelDataUpdate(AccelData data) {
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
