package com.example.motionrecorder;

/**
 * A presenter to connect the Dashboard View with the Dashboard Model (i.e. Interactor).
 */
class DashboardPresenter implements DashboardInteractor.MotionDataUpdatedListener {

    private DashboardView view;
    private DashboardInteractor interactor;

    public DashboardPresenter(DashboardView view, DashboardInteractor dashboardInteractor) {

        this.view = view;
        this.interactor = dashboardInteractor;

        //Add the presenter as a listener to receive MotionData updates
        dashboardInteractor.addMotionDataUpdatedListener(this);

    }

    public void stopRecording() {

        int seconds = interactor.stopRecording();

        //Notify the user that recording has stopped, if activity is still running.
        if(view != null) {
            view.showMessage("Recorded " + seconds + "s.");
        }
    }

    public void startRecording() {
        interactor.startRecording();
    }


    @Override
    public void onGpsDataUpdate(GpsData data) {
        if(view != null) {
            view.setGps(data);
        }
    }

    @Override
    public void onAccelDataUpdate(AccelData data) {
        if(view != null) {
            view.setAccel(data);
        }
    }

    @Override
    public void onError(String message) {
        if(view != null) {
            view.showMessage(message);
        }
    }

    public void onDestroy() {
        //Set to null and always check for null so that we do not try
        //to access the view activity if it has been destroyed.
        view = null;
        interactor.stopRecording();
    }

}
