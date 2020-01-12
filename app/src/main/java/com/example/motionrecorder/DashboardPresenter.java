package com.example.motionrecorder;

class DashboardPresenter implements DashboardInteractor.MotionDataUpdatedListener {

    private DashboardView view;
    private DashboardInteractor interactor;

    public DashboardPresenter(DashboardView view, DashboardInteractor dashboardInteractor) {

        this.view = view;
        this.interactor = dashboardInteractor;
        dashboardInteractor.addMotionDataUpdatedListener(this);

    }

    public void stopRecording() {

        int seconds = interactor.stopRecording();
        view.showMessage("Recorded " + seconds + "s.");

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
