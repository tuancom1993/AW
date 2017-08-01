package com.amway.lms.backend.repository.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.LocationRepository;

@Repository
public class LocationRepositoryImpl extends
        AbstractRepository<Serializable, Location> implements
        LocationRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationRepositoryImpl.class);

    @Override
    public List<Location> getLocationList(int firstItem, int maxItem) {
        logger.info("LocationRepositoryImpl*********getLocationList");
        Query query = createNamedQuery("getLocationList", firstItem, maxItem);
        return (List<Location>) query.list();
    }

    @Override
    public Location getLocationById(int id) {
        logger.info("LocationRepositoryImpl*********getLocationById");
        return getByKey(id);
    }

    @Override
    public List<Location> searchByName(String name) {
        logger.info("LocationRepositoryImpl*********searchByName");
        String searchStr = "%" + name + "%";
        Query query = createNamedQuery("searchLocationByName", -1, -1, searchStr);
        return (List<Location>) query.list();
    }

    @Override
    public void addLocation(Location location) {
        persist(location);
        logger.info("Location saved successfully, Location Details=" + location);
    }

    @Override
    public void deleteLocation(Location location) {
        delete(location);
        logger.info("Location deleted successfully, Location Details=" + location);
        
    }

    @Override
    public void editLocation(Location location) {
        update(location);
        logger.info("Location edited successfully, Location Details=" + location);
    }

}
