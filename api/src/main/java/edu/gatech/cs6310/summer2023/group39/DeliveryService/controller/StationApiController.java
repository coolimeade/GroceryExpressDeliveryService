package edu.gatech.cs6310.summer2023.group39.DeliveryService.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.gatech.cs6310.summer2023.group39.DeliveryService.service.StationService;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StationExistsException;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.model.RefuellingStation;
import edu.gatech.cs6310.summer2023.group39.DeliveryService.exception.StationNotFoundException;

@CrossOrigin(origins = "*")
@RestController
public class StationApiController {

    private StationService stationService;

    @Autowired
    public StationApiController(StationService stationService) {
        this.stationService = stationService;
    }

    public static class NewRefuellingStationRequest {
        private String id;
        private int lat;
        private int lon;
        public String getId() { return id; }
        public int getLat() { return lat; }
        public int getLon() { return lon; }
    }

    @PostMapping("/stations")
    public void makeRefuellingStation(@RequestBody NewRefuellingStationRequest request) throws StationExistsException {
        stationService.makeRefuellingStation(request.getId(), request.getLat(), request.getLon());
    }

    @GetMapping("/stations")
    public ArrayList<RefuellingStation> getAllRefuellingStations() {
        ArrayList<RefuellingStation> stations = new ArrayList<RefuellingStation>();
        stationService.forEachRefuellingStation((station) -> {
            stations.add(station);
        });
        return stations;
    }

    public static class MoveRequest {
        private int lat;
        private int lon;
        public int getLat() { return lat; }
        public int getLon() { return lon; }
    }

    @PostMapping("/stations/{stationId}/move")
    public void moveRefuellingStation(@PathVariable String stationId, @RequestBody MoveRequest request)
            throws StationNotFoundException
    {
        stationService.moveRefuellingStation(stationId, request.getLat(), request.getLon());
    }
}