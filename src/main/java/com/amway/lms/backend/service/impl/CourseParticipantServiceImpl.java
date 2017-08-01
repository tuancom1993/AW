package com.amway.lms.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.CourseParticipant;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.repository.CourseParticipantRepository;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.CourseParticipantService;

@Service
@Transactional
public class CourseParticipantServiceImpl implements CourseParticipantService {
    private static final Logger logger = LoggerFactory
            .getLogger(CourseParticipantServiceImpl.class);

    @Autowired
    CourseParticipantRepository courseParticipantDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    DepartmentRepository departmentDao;

    @Override
    public ResponseEntity<?> getCourseParticipantsByCourseId(int courseId) {
        try {
            List<CourseParticipant> courseParticipants = this.courseParticipantDao
                    .getCourseParticipantsByCourseId(courseId);
            if (courseParticipants == null || courseParticipants.size() == 0)
                throw new ObjectNotFoundException();
            for (int i = 0; i < courseParticipants.size(); i++){
                int userId = courseParticipants.get(i).getUserId();
                User user = this.userDao.getUserById(userId);
                int departmentId = user.getDepartmentId();
                Department department = this.departmentDao
                        .getDepartmentById(departmentId);
                if(department == null) {
                    department = new Department();
                    department.setName("");
                }
                user.setDepartment(department);
                courseParticipants.get(i).setUser(user);
            }
            
            return Utils.generateSuccessResponseEntity(courseParticipants);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addCourseParticipant" + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addCourseParticipant(
            CourseParticipant courseParticipant) {
        try {
            this.courseParticipantDao.addCourseParticipant(courseParticipant);
            return new ResponseEntity<>(courseParticipant, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("EXCEPTION - addCourseParticipant" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> addCourseParticipantByIds(int courseId,
            List<Integer> userIdList) {
        try {
            for (int i = 0; i < userIdList.size(); i++) {
                CourseParticipant courseParticipant = new CourseParticipant();
                courseParticipant.setUserId(userIdList.get(i));
                courseParticipant.setCourseId(courseId);
                // Set default for confirmation status is 0
                byte confirmStatus = 0;
                courseParticipant.setConfirmationStatus(confirmStatus);
                this.courseParticipantDao
                        .addCourseParticipant(courseParticipant);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("EXCEPTION - addCourseParticipantByIds"
                    + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getUsersByCourseId(int courseId,
            Integer confirmationStatus, Integer departmentId) {
        try {

            List<User> users = this.courseParticipantDao.getUsersByCourseId(
                    courseId, confirmationStatus, departmentId);
            if (users == null || users.size() == 0)
                throw new ObjectNotFoundException();

            for (int i = 0; i < users.size(); i++) {
                if (departmentId == null)
                    departmentId = users.get(i).getDepartmentId();
                Department department = this.departmentDao
                        .getDepartmentById(departmentId);
                users.get(i).setDepartment(department);
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getUsersByCourseId" + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> updateCheckIn(CourseParticipant courseParticipant) {
        try {
            CourseParticipant cp = this.courseParticipantDao
                    .getCourseParticipant(courseParticipant.getCourseId(),
                            courseParticipant.getUserId());
            if (cp == null)
                throw new ObjectNotFoundException();
            courseParticipant.setCheckinAt(Utils.getCurrentTime());
            this.courseParticipantDao.updateCheckIn(courseParticipant);
            return Utils.generateSuccessResponseEntity(courseParticipant);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - updateCheckIn" + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> updateCheckOut(CourseParticipant courseParticipant) {
        try {
            CourseParticipant cp = this.courseParticipantDao
                    .getCourseParticipant(courseParticipant.getCourseId(),
                            courseParticipant.getUserId());
            if (cp == null)
                throw new ObjectNotFoundException();
            courseParticipant.setCheckoutAt(Utils.getCurrentTime());
            this.courseParticipantDao.updateCheckOut(courseParticipant);
            return Utils.generateSuccessResponseEntity(courseParticipant);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - updateCheckIn" + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> addCoursePaticipant(int courseId, List<User> users) {
        try {
            // get List of Users to return
            List<User> usersReturn = new ArrayList<User>();
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).getId();
                User user = this.userDao.getUserById(userId);
                if (user == null)
                    throw new ObjectNotFoundException();
                this.courseParticipantDao
                        .addCourseParticipant(courseId, userId);
                usersReturn.add(user);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addCoursePaticipant " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> delCoursePaticipant(int courseId, List<User> users) {
        try {
            // get List of Users to return
            List<User> usersReturn = new ArrayList<User>();
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).getId();
                User user = this.userDao.getUserById(userId);
                // If user is not found, throw exception
                if (user == null)
                    throw new ObjectNotFoundException();

                // If CourseParticipant is not found, throw exception
                CourseParticipant cp = this.courseParticipantDao
                        .getCourseParticipant(courseId, userId);
                if (cp == null)
                    throw new ObjectNotFoundException();
                this.courseParticipantDao.delCourseParticipant(courseId, users
                        .get(i).getId());
                usersReturn.add(user);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - delCourseParticipant " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

}
