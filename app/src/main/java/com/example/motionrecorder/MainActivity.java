package com.example.motionrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DashboardView {

    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private TextView bearing;
    private TextView speed;
    private TextView accuracy;
    private TextView accelX;
    private TextView accelY;
    private TextView accelZ;
    private Button startStop;
    private DashboardPresenter presenter;

    private boolean isStarted;
    private boolean gpsPermission;

    private final static int FINE_LOCATION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get all the textviews
        latitude = findViewById(R.id.latitudeValue);
        longitude = findViewById(R.id.longitudeValue);
        altitude = findViewById(R.id.altitudeValue);
        bearing = findViewById(R.id.bearingValue);
        speed = findViewById(R.id.speedValue);
        accuracy = findViewById(R.id.accuracyValue);
        accelX = findViewById(R.id.xValue);
        accelY = findViewById(R.id.yValue);
        accelZ = findViewById(R.id.zValue);
        startStop = findViewById(R.id.startStopButton);

        isStarted = false;
        gpsPermission = false;

        //Create a presenter
        presenter = new DashboardPresenter(this, new DashboardInteractor(this, gpsPermission));

        //Connect an onclick listener to the button
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.onButtonClick();
            }
        });

    }

    private void getPermissions() {

        //Only need runtime permissions for Marshmallow or later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Check if permissions are already granted
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //If not granted request the permission
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST);
            }

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case FINE_LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted, we can use GPS
                    gpsPermission = true;
                } else {
                    //Permission denied, update GPS title
                    TextView title = findViewById(R.id.gpsTitle);
                    title.setText("GPS: No Permission");
                }
                return;
            }
        }

    }

    private void onButtonClick() {

        if(isStarted) {

            //Stop the recording and set the button to say "start"
            presenter.stopRecording();
            isStarted = false;
            startStop.setText(getString(R.string.button_label_start));

        } else {

            //Start the recording and set the button to say "stop"
            presenter.startRecording();
            isStarted = true;
            startStop.setText(getString(R.string.button_label_stop));

        }

    }

    @Override
    public void setGps(GpsData data) {

        //TODO

    }

    @Override
    public void setAccel(AccelData data) {

        accelX.setText(String.valueOf(data.getX()));
        accelY.setText(String.valueOf(data.getY()));
        accelZ.setText(String.valueOf(data.getZ()));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
