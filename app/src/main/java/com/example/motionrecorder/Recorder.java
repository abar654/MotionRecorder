package com.example.motionrecorder;

import android.util.Log;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

class Recorder implements DashboardInteractor.MotionDataUpdatedListener{

    private AccelData currentAccelData;
    private GpsData currentGpsData;
    private File outputFile;
    private FileOutputStream outputStream;
    private PrintWriter output;
    private Handler handler;
    private Runnable recordTask;
    private int numRecordings;
    private boolean isRecording;

    private static final int SAMPLING_PERIOD = 100;
    private static final String HEADER = "Timestamp, latitude, longitude, bearing, altitude, speed, accuracy, accel_x, accel_y, accel_z\n";


    public Recorder(AccelData accelData, GpsData gpsData, File outputFile) {

        currentAccelData = accelData;
        currentGpsData = gpsData;
        this.outputFile = outputFile;
        handler = new Handler();
        isRecording = false;

    }

    public void start() {

        //Set numRecording back to 0
        numRecordings = 0;

        try {

            //Open the file
            outputStream = new FileOutputStream(outputFile);
            output = new PrintWriter(outputStream);

            //Write the header file
            output.write(HEADER);

            //Create the task
            recordTask = new Runnable() {
                @Override
                public void run() {

                    //Save the current data
                    output.write(Calendar.getInstance().getTimeInMillis() + ",");
                    output.write(currentGpsData.getLatitude() + ",");
                    output.write(currentGpsData.getLongitude() + ",");
                    output.write(currentGpsData.getBearing() + ",");
                    output.write(currentGpsData.getAltitude() + ",");
                    output.write(currentGpsData.getSpeed() + ",");
                    output.write(currentGpsData.getAccuracy() + ",");
                    output.write(currentAccelData.getX() + ",");
                    output.write(currentAccelData.getY() + ",");
                    output.write(currentAccelData.getZ() + "\n");

                    numRecordings++;

                    //Run again in 0.1s
                    handler.postDelayed(this, SAMPLING_PERIOD);
                }
            };

            //Run the task
            handler.post(recordTask);

            isRecording = true;

        } catch(IOException e) {
            Log.e("Error: ", e.getMessage());
        }

    }

    public int stop() {

        if(isRecording) {

            //Stop the task
            handler.removeCallbacks(recordTask);

            //Close the file
            try {
                output.close();
                outputStream.close();
            } catch(IOException e) {
                Log.e("Error: ", e.getMessage());
            }

            isRecording = false;

        }

        //Return the number of seconds, 10 recordings per second
        return numRecordings/10;

    }

    @Override
    public void onGpsDataUpdate(GpsData data) {
        currentGpsData = data;
    }

    @Override
    public void onAccelDataUpdate(AccelData data) {
        currentAccelData = data;
    }

    @Override
    public void onError(String message) {
        //Don't need to do anything
    }

}
