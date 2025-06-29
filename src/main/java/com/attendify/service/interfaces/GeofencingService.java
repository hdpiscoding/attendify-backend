package com.attendify.service.interfaces;

public interface GeofencingService {
    boolean isWithinGeofence(double latitude, double longitude);
}
