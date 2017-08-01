package com.amway.lms.backend.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.service.LocationService;

@Service
@Transactional
@EnableTransactionManagement
public class LocationServiceImpl implements LocationService {
    private static final Logger logger = LoggerFactory
            .getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationRepository locationDao;

    @Autowired
    private RoomRepository roomDao;

    @Override
    public ResponseEntity<?> getLocationList(int firstItem, int maxItem)
            throws ObjectNotFoundException {
        logger.info("LocationServiceImpl*****getLocationList");
        List<Location> locations = locationDao.getLocationList(firstItem,
                maxItem);
        if (locations == null || locations.size() == 0) {
            logger.info("getLocationList*****The list is empty");
            throw new ObjectNotFoundException("The list is empty");
        } else {
            return Utils.generateSuccessResponseEntity(locations);
        }
    }

    @Override
    public ResponseEntity<?> getLocationById(int id) {
        try {
            Location location = this.locationDao.getLocationById(id);
            if (location == null)
                throw new ObjectNotFoundException();
            return Utils.generateSuccessResponseEntity(location);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getLocationById " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getRoomsByLocationId(int locationId) {
        try {
            List<Room> rooms = new ArrayList<Room>(); 
            rooms = roomDao.getRoomsByLocationId(locationId);
            
            return Utils.generateSuccessResponseEntity(rooms);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getRoomsByLocationId " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> searchByName(String name) throws SQLException,
            Exception {
        List<Location> locations = locationDao.searchByName(name);
        if (locations == null || locations.size() == 0) {
            throw new ObjectNotFoundException("The list is empty");
        } else {
            return Utils.generateSuccessResponseEntity(locations);
        }
    }

    @Override
    public ResponseEntity<?> addLocation(Location location) {
        try {
            this.locationDao.addLocation(location);
            return Utils.generateSuccessResponseEntity(location);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addLocation " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteLocations(List<Location> locations) {
        try {
            for (int i = 0; i < locations.size(); i++) {
                int locationId = locations.get(i).getId();
                Location location = this.locationDao
                        .getLocationById(locationId);
                // Check if location doesn't exist in DB then throw Exception
                if (location == null) {
                    logger.debug("deleteLocations***LocationId=" + locationId);
                    throw new DeleteObjectException();
                }
                // Delete rooms by locationId
                List<Room> rooms = this.roomDao
                        .getRoomsByLocationId(locationId);
                if (rooms != null && rooms.size() > 0) {
                    for (int j = 0; j < rooms.size(); j++) {
                        this.roomDao.deleteRoom(rooms.get(j));
                    }
                }
                this.locationDao.deleteLocation(location);

            }
            return Utils.generateSuccessResponseEntity(locations);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - deleteLocation " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteLocation(int id) {
        try {
            Location c = this.locationDao.getLocationById(id);
            // Check if location doesn't exist in DB then throw Exception
            if (c == null) {
                logger.debug("deleteLocation***LocationId=" + id);
                throw new DeleteObjectException();
            }
            this.locationDao.deleteLocation(c);
            return Utils.generateSuccessResponseEntity(c);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - deleteLocation " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> editLocation(Location location) {
        try {
            Location l = this.locationDao.getLocationById(location.getId());
            // Check if location doesn't exist in DB then throw Exception
            if (l == null) {
                logger.debug("editLocation***LocationId=" + location.getId());
                throw new EditObjectException();
            }
            l.setName(location.getName());
            this.locationDao.editLocation(l);
            return Utils.generateSuccessResponseEntity(location);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addLocation " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

}
