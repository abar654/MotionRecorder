package com.example.motionrecorder;

//A read only data class that holds acceleration data
class AccelData {

    private float x;
    private float y;
    private float z;

    public AccelData(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

}
