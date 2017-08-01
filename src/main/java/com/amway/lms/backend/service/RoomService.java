package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Room;
public interface RoomService {
    
    public ResponseEntity<?> getRoomList(int firstItem, int maxItem);

    public ResponseEntity<?> getRoomById(int id);

    public ResponseEntity<?> searchByName(String name);
    
    public ResponseEntity<?> addRoom(Room room);

    public ResponseEntity<?> deleteRooms(List<Room> rooms);

    public ResponseEntity<?> deleteRoom(int id);

    public ResponseEntity<?> editRoom(Room room);

}
