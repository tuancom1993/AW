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

import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.exception.AddObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.RoomService;

@RestController
@RequestMapping("/api/v1")
public class RoomController {
    private static final Logger logger = LoggerFactory
            .getLogger(RoomController.class);
    
    @Autowired
    private RoomService roomService;
    
    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public ResponseEntity<?> getRoomList()
            throws SQLException, Exception, ObjectNotFoundException {
        logger.info("RoomController********getRoomList");
        return roomService.getRoomList(-1, -1);
    }
    
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getRoomById(@PathVariable("id") Integer id){
        logger.info("RoomController********getRoomById: id = " + id);
        return roomService.getRoomById(id);
    }
    
    @RequestMapping(value = "/rooms/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchByName(@RequestParam("name") String name)
            throws SQLException, ObjectNotFoundException, Exception {
        logger.info("RoomController********searchByName: name = " + name);
        return roomService.searchByName(name);
    }
    
    @RequestMapping(value = "/rooms", method = RequestMethod.POST)
    public ResponseEntity<?> addRoom(@RequestBody Room room)
            throws SQLException, AddObjectException, Exception {
        logger.info("CategoryController********addCategory: " + room);
        return this.roomService.addRoom(room);
    }

    @RequestMapping(value = "/rooms", method = RequestMethod.PUT)
    public ResponseEntity<?> editRoom(@RequestBody Room room)
            throws SQLException, Exception {
        return this.roomService.editRoom(room);
    }

    @RequestMapping(value = "/rooms", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRooms(@RequestBody List<Room> rooms)
            throws SQLException, Exception {
        return this.roomService.deleteRooms(rooms);
    }
    
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Integer id)
            throws SQLException, Exception {
        return this.roomService.deleteRoom(id);
    }

}
