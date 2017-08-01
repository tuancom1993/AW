package com.amway.lms.backend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Message;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.exception.DeleteObjectException;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.service.RoomService;

@Service
@Transactional
@EnableTransactionManagement
public class RoomServiceImpl implements RoomService {
    private static final Logger logger = LoggerFactory
            .getLogger(RoomServiceImpl.class);

    @Autowired
    private RoomRepository roomDao;

    @Autowired
    private LocationRepository locationDao;

    @Override
    public ResponseEntity<?> getRoomList(int firstItem, int maxItem){
        try {
            logger.info("RoomServiceImpl*****getRoomList");
            List<Room> rooms = roomDao.getRoomList(firstItem, maxItem);
            if (rooms == null || rooms.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_LIST_IS_EMPTY);

            return Utils
                    .generateSuccessResponseEntity(addLocationToRoomList(rooms));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getRoomList " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> getRoomById(int id) {
        try {
            Room room = this.roomDao.getRoomById(id);
            if (room == null)
                throw new ObjectNotFoundException(Message.MSG_ROOM_NOT_FOUND
                        + " roomId = " + id);
            Location location = locationDao.getLocationById(room
                    .getLocationId());
            room.setLocation(location);
            return Utils.generateSuccessResponseEntity(room);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getRoomById " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> searchByName(String name){
        try {
            List<Room> rooms = roomDao.searchByName(name);
            if (rooms == null || rooms.size() == 0)
                throw new ObjectNotFoundException(Message.MSG_LIST_IS_EMPTY);
            return Utils
                    .generateSuccessResponseEntity(addLocationToRoomList(rooms));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - searchByName " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addRoom(Room room) {
        try {
            this.roomDao.addRoom(room);
            return Utils.generateSuccessResponseEntity(room);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addRoom " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> deleteRooms(List<Room> rooms) {
        try {
            for (Room room : rooms) {
                Room r = this.roomDao.getRoomById(room.getId());
                // Check if room doesn't exist in DB then throw Exception
                if (r == null)
                    throw new DeleteObjectException(Message.MSG_ROOM_NOT_FOUND
                            + " RoomId=" + room.getId());
                this.roomDao.deleteRoom(r);
            }
            return Utils.generateSuccessResponseEntity(rooms);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - deleteRoom " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deleteRoom(int id) {
        try {
            Room r = this.roomDao.getRoomById(id);
            // Check if room doesn't exist in DB then throw Exception
            if (r == null) 
                throw new DeleteObjectException(Message.MSG_ROOM_NOT_FOUND
                        + " RoomId=" + id);
            this.roomDao.deleteRoom(r);
            return Utils.generateSuccessResponseEntity(r);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - deleteRoom " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> editRoom(Room room) {
        try {
            Room r = this.roomDao.getRoomById(room.getId());
            // Check if room doesn't exist in DB then throw Exception
            if (r == null) 
                throw new EditObjectException(Message.MSG_ROOM_NOT_FOUND
                        + " RoomId=" + room.getId());
            r.setName(room.getName());
            r.setLocationId(room.getLocationId());
            r.setStatus(room.getStatus());
            this.roomDao.editRoom(r);
            return Utils.generateSuccessResponseEntity(room);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addRoom " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    e.getMessage());
        }
    }

    private List<Room> addLocationToRoomList(List<Room> rooms) {
        for (int i = 0; i < rooms.size(); i++) {
            int locationId = rooms.get(i).getLocationId();
            Location location = locationDao.getLocationById(locationId);
            logger.debug("getRoomList, Location" + location);
            rooms.get(i).setLocation(location);
        }
        return rooms;
    }

}
