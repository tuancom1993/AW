package com.amway.lms.backend.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.EmailInfor;
import com.amway.lms.backend.service.CourseService;

/**
 * @author: Hung (Charles) V. PHAM
 * */
@RestController
public class CourseController {
    private static final Logger logger = LoggerFactory
            .getLogger(CourseController.class);

    @Autowired
    CourseService courseService;

    @RequestMapping(value = "/api/v1/courses", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesList(
            @RequestParam(name = "isActive", required = true) Integer isActive,
            @RequestParam(name = "trainingPlanId", required = false) Integer trainingPlanId)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesList");
        return courseService.getCoursesList(isActive, trainingPlanId);
    }

    @RequestMapping(value = "/api/v1/courses/{trainingPlanId}/addToPlan", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesToAddTrainingPlan(
            @PathVariable Integer trainingPlanId)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesToAddTrainingPlan");
        return courseService.getCoursesToAddTrainingPlan(trainingPlanId);
    }

    @RequestMapping(value = "/api/v1/courses/status", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesListByStatus(
            @RequestParam(name = "isActive", required = true) Integer isActive)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesListByStatus");
        return courseService.getCoursesListByStatus(-1, -1, isActive);
    }

    @RequestMapping(value = "/api/v1/courses/{courseId}/sessions", method = RequestMethod.GET)
    public ResponseEntity<?> getSessionsByCourseId(
            @PathVariable Integer courseId) throws ObjectNotFoundException,
            SQLException, Exception {
        logger.info("CourseController********getCoursesListByStatus");
        return courseService.getSessionsByCourseId(courseId);
    }

    // Add new courses
    @RequestMapping(value = "/api/v1/courses", method = RequestMethod.POST)
    public ResponseEntity<?> addNewCourse(@RequestBody final Course course)
            throws SQLException, Exception {
        logger.info("CourseController********addNewCourse: Course Detail: "
                + course);
        return courseService.addCourse(course);
    }

    @RequestMapping(value = "/api/v1/courses/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCoursesStatus(
            @RequestParam(name = "isActive", required = false) int isActive,
            @RequestBody final List<Course> courses) throws SQLException,
            Exception {
        logger.info("Update Courses Status: " + courses);
        return courseService.updateCoursesStatus(courses, isActive);
    }

    @RequestMapping(value = "/api/v1/courses/ajpv", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCoursesAJPV(@RequestBody final Course course)
            throws SQLException, Exception {
        logger.info("Update Courses AJPV: " + course);
        return courseService.updateCoursesAJPV(course);
    }

    @RequestMapping(value = "/api/v1/courses/{isActive}/search", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesByNameOrCode(
            @PathVariable Integer isActive,
            @RequestParam(name = "q", required = false) String searchString)
            throws SQLException, Exception {
        logger.info("getCoursesByNameOrCode: " + searchString);
        return courseService.getCoursesByNameOrCode(searchString, isActive);
    }

    @RequestMapping(value = "/api/v1/courses/user", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesByUser(
            @RequestParam("q") String searchString) throws SQLException,
            Exception {
        logger.info("getCoursesByUser: " + searchString);
        return courseService.getCoursesByUser(searchString);
    }

    @RequestMapping(value = "/api/v1/courses/{courseId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCourseById(@PathVariable int courseId)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCourse");
        return courseService.getCoursesById(courseId);
    }

    @RequestMapping(value = "/api/v1/coursesApproved", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesApprovedList(
            @RequestParam(value = "p", required = false) Integer currentPage)
            throws ObjectNotFoundException, SQLException, Exception {

        logger.info("CourseController********getCoursesApprovedList");
        int firstItem = Common.getFirstItem(currentPage == null ? 1
                : currentPage);
        return courseService.getCoursesApprovedList(firstItem,
                Common.ITEMS_PER_PAGE);
    }

    @RequestMapping(value = "/api/v1/coursesInProgress", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesInProgressList(
            @RequestParam(value = "p", required = false) Integer currentPage)
            throws ObjectNotFoundException, SQLException, Exception {

        logger.info("CourseController********getCoursesInProgressList");
        int firstItem = Common.getFirstItem(currentPage == null ? 1
                : currentPage);
        return courseService.getCoursesInProgressList(firstItem,
                Common.ITEMS_PER_PAGE);
    }

    @RequestMapping(value = "/api/v1/email/{courseId}", method = RequestMethod.PUT)
    public ResponseEntity<?> sendingEmailToPrticipant(
            @PathVariable int courseId, @RequestBody EmailInfor emailInfor)
            throws IOException {
        logger.info("CourseController********getCoursesInProgressList");
        return courseService.sendingEmailToPrticipants(courseId, emailInfor);
    }

    /*
     * Acton
     */
    @RequestMapping(value = "/api/v1/courses/not/quiz", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesListNotHaveQuiz()
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesListNotHaveQuiz");
        return courseService.getCoursesListNotHaveQuiz();
    }

    @RequestMapping(value = "/api/v1/courses/checkIn", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesStartByToday(
            @RequestParam(name = "courseName", required = false) String courseName)
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesList");
        return courseService.getCoursesStartByToday(courseName);
    }

    @RequestMapping(value = "/api/v1/courses/postTrainingSurvey", method = RequestMethod.GET)
    public ResponseEntity<?> getCoursesListForPostTrainingSurvey()
            throws ObjectNotFoundException, SQLException, Exception {
        logger.info("CourseController********getCoursesListForPostTrainingSurvey");
        return courseService.getCoursesListForPostTrainingSurvey();
    }
}
