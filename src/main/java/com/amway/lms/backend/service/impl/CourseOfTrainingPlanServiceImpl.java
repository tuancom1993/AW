package com.amway.lms.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.repository.CourseOfTrainingPlanRepository;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.service.CourseOfTrainingPlanService;

@Service
@Transactional
public class CourseOfTrainingPlanServiceImpl implements
        CourseOfTrainingPlanService {
    private static final Logger logger = LoggerFactory
            .getLogger(CourseOfTrainingPlanServiceImpl.class);

    @Autowired
    private CourseRepository courseDao;

    @Autowired
    private CourseOfTrainingPlanRepository courseOfTrainingPlanDao;

    @Override
    public ResponseEntity<?> addCourseOfTrainingPlan(int trainingPlanId,
            List<Course> courses) {
        logger.info("CourseOfTrainingPlanServiceImpl********addCourseOfTrainingPlan");
        try {
            // get List of Users to return
            List<Course> coursesReturn = new ArrayList<Course>();
            for (int i = 0; i < courses.size(); i++) {
                int courseId = courses.get(i).getId();
                Course course = this.courseDao.getCourseById(courseId);
                if (course == null)
                    throw new ObjectNotFoundException();
                this.courseOfTrainingPlanDao.addCourseOfTrainingPlan(
                        trainingPlanId, courseId);
                coursesReturn.add(course);
            }
            return Utils.generateSuccessResponseEntity(coursesReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - addCourseOfTrainingPlan " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_ADD_OBJECT_EXCEPTION,
                    ErrorCode.MSG_ADD_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> removeCoursesOfTrainingPlan(int trainingPlanId,
            List<Course> courses) {
        logger.info("CourseOfTrainingPlanServiceImpl********removeCoursesOfTrainingPlan");
        try {
            // get List of Users to return
            List<Course> coursesReturn = new ArrayList<Course>();
            for (int i = 0; i < courses.size(); i++) {
                int courseId = courses.get(i).getId();
                Course course = this.courseDao.getCourseById(courseId);
                if (course == null)
                    throw new ObjectNotFoundException();
                this.courseOfTrainingPlanDao.removeCoursesOfTrainingPlan(
                        trainingPlanId, courseId);
                coursesReturn.add(course);
            }
            return Utils.generateSuccessResponseEntity(coursesReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - removeCoursesOfTrainingPlan " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> setMandatoryForCourses(int trainingPlanId,
            List<Course> courses, int isRequired) {
        logger.info("CourseOfTrainingPlanServiceImpl********removeCoursesOfTrainingPlan");
        try {
            // get List of Users to return
            List<Course> coursesReturn = new ArrayList<Course>();
            for (int i = 0; i < courses.size(); i++) {
                int courseId = courses.get(i).getId();
                Course course = this.courseDao.getCourseById(courseId);
                if (course == null)
                    throw new ObjectNotFoundException();
                this.courseOfTrainingPlanDao.setMandatoryForCourse(
                        trainingPlanId, courseId, isRequired);
                coursesReturn.add(course);
            }
            return Utils.generateSuccessResponseEntity(coursesReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setMandatoryForCourses " + e.getMessage());
            return Utils.generateFailureResponseEntity(
                    ErrorCode.CODE_DELETE_OBJECT_EXCEPTION,
                    ErrorCode.MSG_DELETE_OBJECT_EXCEPTION);
        }
    }

}
