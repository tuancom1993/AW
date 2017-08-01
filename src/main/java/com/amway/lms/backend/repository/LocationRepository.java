package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.Location;

public interface LocationRepository {

    public List<Location> getLocationList(int firstItem, int maxItem);
    public Location getLocationById(int id);
    public List<Location> searchByName(String name);
    public void addLocation(Location location);
    public void deleteLocation(Location location);
    public void editLocation(Location location);

}
