package com.amway.lms.backend.model;

import java.sql.Timestamp;

import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.Room;

public class SessionModel {

    private String courseName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String sessionName;
    private String locationName;
    private String roomName;
    private String date;
    private String start;
    private String end;
    private String status;
    private int courseId;
    private int id;
    private String description;
    private String endTimeStr;
    private int locationId;
    private int roomId;
    private String startTimeStr;
    private int trainerId;
    private String trainerFullName;
    private int isInternalTrainer;
    private String coordinatorFullName;
    private int coordinatorId;
    private boolean isAddParticipant;
    private boolean isEndSession;
    private Location location;
    private Room room;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @param room
     *            the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the coordinatorId
     */
    public int getCoordinatorId() {
        return coordinatorId;
    }

    /**
     * @param coordinatorId
     *            the coordinatorId to set
     */
    public void setCoordinatorId(int coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the courseId
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * @param courseId
     *            the courseId to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the endTimeStr
     */
    public String getEndTimeStr() {
        return endTimeStr;
    }

    /**
     * @param endTimeStr
     *            the endTimeStr to set
     */
    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    /**
     * @return the locationId
     */
    public int getLocationId() {
        return locationId;
    }

    /**
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName
     *            the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName
     *            the roomName to set
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * @return the roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * @param roomId
     *            the roomId to set
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * @return the startTimeStr
     */
    public String getStartTimeStr() {
        return startTimeStr;
    }

    /**
     * @param startTimeStr
     *            the startTimeStr to set
     */
    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    /**
     * @return the trainerId
     */
    public int getTrainerId() {
        return trainerId;
    }

    /**
     * @param trainerId
     *            the trainerId to set
     */
    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    /**
     * @return the trainerFullName
     */
    public String getTrainerFullName() {
        return trainerFullName;
    }

    /**
     * @param trainerFullName
     *            the trainerFullName to set
     */
    public void setTrainerFullName(String trainerFullName) {
        this.trainerFullName = trainerFullName;
    }

    /**
     * @return the coordinatorFullName
     */
    public String getCoordinatorFullName() {
        return coordinatorFullName;
    }

    /**
     * @param coordinatorFullName
     *            the coordinatorFullName to set
     */
    public void setCoordinatorFullName(String coordinatorFullName) {
        this.coordinatorFullName = coordinatorFullName;
    }

    /**
     * @return the isAddParticipant
     */
    public boolean isAddParticipant() {
        return isAddParticipant;
    }

    /**
     * @return the isEndSession
     */
    public boolean isEndSession() {
        return isEndSession;
    }

    /**
     * @param isEndSession
     *            the isEndSession to set
     */
    public void setEndSession(boolean isEndSession) {
        this.isEndSession = isEndSession;
    }

    /**
     * @param isAddParticipant
     *            the isAddParticipant to set
     */
    public void setAddParticipant(boolean isAddParticipant) {
        this.isAddParticipant = isAddParticipant;
    }

    /**
     * @return the isInternalTrainer
     */
    public int getIsInternalTrainer() {
        return isInternalTrainer;
    }

    /**
     * @param isInternalTrainer
     *            the isInternalTrainer to set
     */
    public void setIsInternalTrainer(int isInternalTrainer) {
        this.isInternalTrainer = isInternalTrainer;
    }

    /**
     * @return the startTime
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the start
     */
    public String getStart() {
        return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * @param end
     *            the end to set
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
