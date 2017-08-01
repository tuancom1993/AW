package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the locations database table.
 * 
 */
@Entity
@Table(name = "locations")
@NamedQueries({ 
    @NamedQuery(name = "getLocationList", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "searchLocationByName", query = "SELECT l FROM Location l WHERE l.name LIKE ?")})
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Lob
    private String name;

    public Location() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}