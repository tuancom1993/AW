package com.amway.lms.backend.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the sessions database table.
 * 
 */
@Entity
@Table(name = "sessions")
@NamedQueries({
        @NamedQuery(name = "countSessionInPass", query = "SELECT count(s.id) FROM Session s WHERE s.courseId = ? and s.startTime < now()"),
        @NamedQuery(name = "getSessionById", query = "FROM Session s WHERE s.id = ?"), 
        @NamedQuery(name = "getListCourseSuggest", query = "select s FROM Session s WHERE s.id not in (select b.sessionId from SessionParticipant b where b.userId=?)"),
        @NamedQuery(name = "getSessionsNotStarted", query = "FROM Session s WHERE s.courseId = ? and s.startTime > now()"),
        @NamedQuery(name = "getSessionsInProgress", query = "FROM Session s WHERE s.courseId = ? and s.startTime < now() and s.endTime > now()"),
        @NamedQuery(name = "getSessionsCompleted", query = "FROM Session s WHERE s.courseId = ? and s.endTime < now()"),
        @NamedQuery(name = "getSessionsInactive", query = "FROM Session s WHERE s.courseId IN (SELECT c.id FROM Course c WHERE c.id=? AND c.isActive=0)"),
        @NamedQuery(name = "getSessionsStartToday", query = "FROM Session s WHERE s.courseId = ? AND DATE(s.startTime) = DATE(now())"),
        @NamedQuery(name = "getSessionsNotSentEmailToTrainer", query = "FROM Session s WHERE s.isSentEmail = 0 AND s.isInternalTrainer = 1"),
        @NamedQuery(name = "getSessionsByCourseId", query = "FROM Session s WHERE s.courseId = ?"),
        @NamedQuery(name = "getSessionAndCourseById", query = "select s,c FROM Session s,Course c WHERE s.id=? AND s.courseId = c.id AND c.id=?") 

        
})
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "course_id")
    private int courseId;

    private String description;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "location_id")
    private int locationId;
    
    @Transient
    private Location location;

    private String name;

    @Column(name = "room_id")
    private int roomId;
    
    @Transient
    private Room room;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "trainer_id")
    private Integer trainerId;

    @Column(name = "trainer_full_name")
    private String trainerFullName;

    @Column(name = "is_internal_trainer")
    private int isInternalTrainer;
    
    @Column(name = "coordinator_full_name")
    private String coordinatorFullName;
    
    @Column(name = "coordinator_id")
    private int coordinatorId;
    
    @Column(name = "is_sent_email")
    private int isSentEmail;
    
    @Transient
    private String status;
    
    @Transient
    private String courseName;
    
    @Transient
    private String startTimeStr;
    
    @Transient
    private String endTimeStr;

    public Session() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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

    public int getRoomId() {
        return this.roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerFullName() {
        return trainerFullName;
    }

    public void setTrainerFullName(String trainerFullName) {
        this.trainerFullName = trainerFullName;
    }

    public int getIsInternalTrainer() {
        return isInternalTrainer;
    }

    public void setIsInternalTrainer(int isInternalTrainer) {
        this.isInternalTrainer = isInternalTrainer;
    }

    

    public String getCoordinatorFullName() {
        return coordinatorFullName;
    }

    public void setCoordinatorFullName(String coordinatorFullName) {
        this.coordinatorFullName = coordinatorFullName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(int coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public int getIsSentEmail() {
        return isSentEmail;
    }

    public void setIsSentEmail(int isSentEmail) {
        this.isSentEmail = isSentEmail;
    }
    
}