package com.amway.lms.backend.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.exception.ObjectNotFoundException;

public interface LocationService {
    public ResponseEntity<?> getLocationList(int firstItem, int maxItem)
            throws ObjectNotFoundException;

    public ResponseEntity<?> getLocationById(int id);
    
    public ResponseEntity<?> getRoomsByLocationId(int locationId);

    public ResponseEntity<?> searchByName(String name) throws SQLException,
            Exception;
    
    public ResponseEntity<?> addLocation(Location location);

    public ResponseEntity<?> deleteLocations(List<Location> locations);

    public ResponseEntity<?> deleteLocation(int id);

    public ResponseEntity<?> editLocation(Location location);

}
