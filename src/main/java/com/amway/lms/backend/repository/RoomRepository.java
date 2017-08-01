package com.amway.lms.backend.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.amway.lms.backend.entity.Room;

public interface RoomRepository {
    public List<Room> getRoomList(int firstItem, int maxItem);
    public List<Room> getRoomsByLocationId(int locationId);
    public Room getRoomById(int id);
    public List<Room> searchByName(String name);
    public void addRoom(Room room);
    public void deleteRoom(Room room);
    public void editRoom(Room room);
    public Room findRoombyId(int roomId) throws DataAccessException;
}
