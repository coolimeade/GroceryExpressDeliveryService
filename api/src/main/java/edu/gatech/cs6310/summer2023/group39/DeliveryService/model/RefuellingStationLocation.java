package edu.gatech.cs6310.summer2023.group39.DeliveryService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefuellingStationLocation extends Location {
    @JsonIgnore
    private RefuellingStation refuellingStation;

    public RefuellingStationLocation(int lat, int lon, RefuellingStation refuellingStation) {
        super(lat, lon);
        this.refuellingStation = refuellingStation;
    }

    public RefuellingStation getRefuellingStation() {
        return refuellingStation;
    }

    public boolean canRefuel() {
        return true;
    }
}
