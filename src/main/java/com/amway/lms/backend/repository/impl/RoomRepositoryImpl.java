package com.amway.lms.backend.repository.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.RoomRepository;

@Repository
public class RoomRepositoryImpl extends AbstractRepository<Serializable, Room>
        implements RoomRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomRepositoryImpl.class);

    @Override
    public List<Room> getRoomList(int firstItem, int maxItem) {
        logger.info("RoomRepositoryImpl*********getRoomList");
        Query query = createNamedQuery("getRoomList", firstItem, maxItem);
        return (List<Room>) query.list();
    }
    
    @Override
    public List<Room> getRoomsByLocationId(int locationId) {
        logger.info("RoomRepositoryImpl*********getRoomsByLocationId");
        Query query = createNamedQuery("getRoomsByLocationId", -1, -1, locationId);
        return (List<Room>) query.list();
    }

    @Override
    public Room getRoomById(int id) {
        logger.info("RoomRepositoryImpl*********getRoomById");
        return getByKey(id);
    }

    @Override
    public List<Room> searchByName(String name) {
        logger.info("RoomRepositoryImpl*********searchByName");
        String searchStr = "%" + name + "%";
        Query query = createNamedQuery("searchRoomByName", -1, -1, searchStr);
        return (List<Room>) query.list();
    }

    @Override
    public void addRoom(Room room) {
        persist(room);
        logger.info("Room saved successfully, Room Details=" + room);
    }

    @Override
    public void deleteRoom(Room room) {
        delete(room);
        logger.info("Room deleted successfully, Room Details=" + room);

    }

    @Override
    public void editRoom(Room room) {
        update(room);
        logger.info("Room edited successfully, Room Details=" + room);
    }
    
    @Override
	public Room findRoombyId(int roomId) throws DataAccessException {
		Query query=createNamedQuery("findRoombyId", -1, -1, roomId);
		return (Room) query.uniqueResult();
	}
}
