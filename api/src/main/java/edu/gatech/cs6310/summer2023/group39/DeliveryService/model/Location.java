package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    static final int MIN_LAT = -9000000;
    static final int MAX_LAT = 9000000;
    static final int MIN_LON = -18000000;
    static final int MAX_LON = 18000000;
    private int lat;
    private int lon;

    public Location (int lat, int lon) {
        if (lat < MIN_LAT || lat > MAX_LAT)
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        if (lon < MIN_LON || lon > MAX_LON)
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        this.lat = lat;
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public int getLon() {
        return lon;
    }

    public boolean canRefuel() {
        return false;
    }

    public long distanceTo(Location other) {

        double lat1 = this.lat / 100000.0;
        double lon1 = this.lon / 100000.0;
        double lat2 = other.lat / 100000.0;
        double lon2 = other.lon / 100000.0;

        // Earth Radius in meters
        double R = 6371000;

        // Convert degrees to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Calculation
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return Math.round(distance);
    }

    public double bearingTo(Location other) {
        double lat1 = Math.toRadians(this.getLat() / 100000.0);
        double lon1 = Math.toRadians(this.getLon() / 100000.0);
        double lat2 = Math.toRadians(other.getLat() / 100000.0);
        double lon2 = Math.toRadians(other.getLon() / 100000.0);

        // Calculate the bearing
        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        return Math.atan2(y, x);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return lat == other.lat && lon == other.lon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", lat, lon);
    }
}