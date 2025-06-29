package com.attendify.service.implemetations;

import com.attendify.service.interfaces.GeofencingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.attendify.utils.constants.GeofencingConstants.EARTH_RADIUS;

@Service
@RequiredArgsConstructor
public class GeofencingServiceImpl implements GeofencingService {
    private static final double OFFICE_LAT = 10.757796668746575; // Example: Ho Chi Minh City
    private static final double OFFICE_LON = 106.64116568100889;
    private static final double ALLOWED_RADIUS = 200; // 100 meters

    @Override
    public boolean isWithinGeofence(double lat, double lon) {
        double dLat = Math.toRadians(lat - OFFICE_LAT);
        double dLon = Math.toRadians(lon - OFFICE_LON);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(OFFICE_LAT)) * Math.cos(Math.toRadians(lat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        return distance <= ALLOWED_RADIUS;
    }
}
