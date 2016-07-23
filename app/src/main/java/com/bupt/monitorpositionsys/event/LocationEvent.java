package com.bupt.monitorpositionsys.event;

/**
 * Created by Kerry on 15/12/3.
 */
public class LocationEvent {
    private float speed;

    public LocationEvent(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
