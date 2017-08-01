package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the rooms database table.
 * 
 */
@Entity
@Table(name = "rooms")
@NamedQueries({
        @NamedQuery(name = "getRoomList", query = "SELECT r FROM Room r"),
        @NamedQuery(name = "getRoomsByLocationId", query = "SELECT r FROM Room r WHERE r.locationId = ?"),
        @NamedQuery(name = "searchRoomByName", query = "SELECT r FROM Room r WHERE r.name LIKE ?"),
    	@NamedQuery(name="findRoombyId", query="SELECT r FROM Room r where r.id=?")
})
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "location_id")
    private int locationId;
    
    @Transient
    private Location location;

    @Lob
    private String name;

    private int status;

    public Room() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return this.locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}