package com.amway.lms.backend.rest;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.LocationService;

@RestController
@RequestMapping("/api/v1/")
public class LocationController {
    private static final Logger logger = LoggerFactory
            .getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;
    
    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public ResponseEntity<?> getLocationList()
            throws SQLException, Exception, ObjectNotFoundException {
        logger.info("LocationController********getLocationList");
        return locationService.getLocationList(-1, -1);
    }
    
    @RequestMapping(value = "/locations/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getLocationById(@PathVariable("id") Integer id){
        logger.info("LocationController********getLocationById: id = " + id);
        return locationService.getLocationById(id);
    }
    
    @RequestMapping(value = "/locations/{locationId}/rooms", method = RequestMethod.GET)
    public ResponseEntity<?> getRoomsByLocationId(@PathVariable("locationId") Integer locationId){
        logger.info("LocationController********getLocationById: id = " + locationId);
        return locationService.getRoomsByLocationId(locationId);
    }
    
    @RequestMapping(value = "/locations/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchByName(@RequestParam("name") String name)
            throws SQLException, ObjectNotFoundException, Exception {
        logger.info("LocationController********searchByName: name = " + name);
        return locationService.searchByName(name);
    }
    
    
    @RequestMapping(value = "/locations", method = RequestMethod.POST)
    public ResponseEntity<?> addLocation(@RequestBody Location location)
            throws SQLException, AddObjectException, Exception {
        logger.info("CategoryController********addCategory: " + location);
        return this.locationService.addLocation(location);
    }

    @RequestMapping(value = "/locations", method = RequestMethod.PUT)
    public ResponseEntity<?> editLocation(@RequestBody Location location)
            throws SQLException, Exception {
        return this.locationService.editLocation(location);
    }

    @RequestMapping(value = "/locations", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLocations(@RequestBody List<Location> locations)
            throws SQLException, Exception {
        return this.locationService.deleteLocations(locations);
    }
    
    @RequestMapping(value = "/locations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Integer id)
            throws SQLException, Exception {
        return this.locationService.deleteLocation(id);
    }


}
