package com.example.motionrecorder;

/**
 * The interface which the Activity (i.e. the view in MVP architecture) must implement
 */
public interface DashboardView {

    void setGps(GpsData data);

    void setAccel(AccelData data);

    void showMessage(String message);

}
