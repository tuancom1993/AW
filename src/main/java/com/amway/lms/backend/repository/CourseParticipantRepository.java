package com.amway.lms.backend.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.amway.lms.backend.entity.CourseParticipant;
import com.amway.lms.backend.entity.User;

public interface CourseParticipantRepository {
    public void addCourseParticipant(int courseId, int userId);

    public void delCourseParticipant(int courseId, int userId);

    public void updateCheckIn(CourseParticipant courseParticipant);

    public void updateCheckOut(CourseParticipant courseParticipant);

    public void addCourseParticipant(CourseParticipant courseParticipant);

    public CourseParticipant getCourseParticipant(int courseId, int userId);
    

    public List<User> getUsersByCourseId(int courseId,
            Integer confirmationStatus, Integer departmentId);
    
    public List<CourseParticipant> getCourseParticipantsByCourseId(int courseId);

    public List<CourseParticipant> getListCourseParticipantByUserId(int userId)
            throws DataAccessException;

    public CourseParticipant getCourseParticipantById(int id)
            throws DataAccessException;

    public void updateCourseParticipant(CourseParticipant courseParticipant);

    public CourseParticipant getCourseParticipantCourseIdandUserId(
            int courseId, int userId);
}
