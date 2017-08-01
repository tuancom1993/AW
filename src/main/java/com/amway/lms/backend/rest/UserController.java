package com.amway.lms.backend.rest;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.TrainingNeed;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.service.UserService;

/**
 * @author acton
 *
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    UserService userService;

    /*
     * API get all Users
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser() throws SQLException, Exception {
        return userService.getUserList();
    }
    /*
     * API get all Users Coodinators
     */
    @RequestMapping(value = "/users/coodinators", method = RequestMethod.GET)
    public ResponseEntity<?> getCoodinator() throws SQLException, Exception {
        return userService.getCoodinator();
    }
    /*
     * API get all HOD
     */
    @RequestMapping(value = "/users/hod", method = RequestMethod.GET)
    public ResponseEntity<?> getHodUsers() throws SQLException, Exception {
        return userService.getHodList();
    }
    /*
     * API get all Approval Manager
     */
    @RequestMapping(value = "/users/approvalManager", method = RequestMethod.GET)
    public ResponseEntity<?> getApprovalUsers() throws SQLException, Exception {
        return userService.getApprovalManagerList();
    }
    
    /*
     * API get all Hod and Admin List
     */
    @RequestMapping(value = "/users/hodAndAdmin", method = RequestMethod.GET)
    public ResponseEntity<?> getHodAndAdminList() throws SQLException, Exception {
        return userService.getHodAndAdminList();
    }
    /*
     * API get all Users of Participants
     */
    @RequestMapping(value = "/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getAllParticipants() throws SQLException, Exception {
        return userService.getUserListParticipants();
    }
    /*
     * API get all Users by course and courseParticipants
     */
    @RequestMapping(value = "/sendingListUsers/{courseId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendingListUsers(@PathVariable("courseId") int courseId) throws SQLException, Exception {
        return userService.sendingListUsers(courseId);
    }
    /*
     * API get all Users By departmentId
     */
    @RequestMapping(value = "/users/departments/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> users(@PathVariable("departmentId") int departmentId) throws SQLException, Exception {
        return userService.getUsersByDepartment(departmentId);
    }
    /*
     * API get all Users without Admin and HOD By departmentId 
     */
    @RequestMapping(value = "/users/usersWithoutAdminAndHod/departments/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> usersWithoutAdminAndHod(@PathVariable("departmentId") int departmentId) throws SQLException, Exception {
        return userService.usersWithoutAdminAndHodByDepartment(departmentId);
    }
    /*
     * API get all Users Except AM and HOD By departmentId
     */
    @RequestMapping(value = "/users/departmentsEx/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> usersExceptAmAndHod(@PathVariable("departmentId") int departmentId) throws SQLException, Exception {
        return userService.getUserExceptAmAndHodByDepartment(departmentId);
    }
    /*
     * API get Approval Manager By DepartmentId
     */
    @RequestMapping(value = "/users/ApprovalManager/departments/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> approvalManagerList(@PathVariable("departmentId") int departmentId) throws SQLException, Exception {
        return userService.getApprovalManagerByDepartment(departmentId);
    }
    /*
     * API HOD By DepartmentId
     */
    @RequestMapping(value = "/users/Hod/departments/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> hod(@PathVariable("departmentId") int departmentId) throws SQLException, Exception {
        return userService.getHodByDepartmentId(departmentId);
    }
    /*
     * API get all DirectLineManager By departmentId
     */
    @RequestMapping(value = "/directLineManagers/departments/{departmentId}", method = RequestMethod.GET)
    public ResponseEntity<?> directLineManagers(@PathVariable("departmentId") int departmentId) throws SQLException, Exception {
        return userService.getDirectsByDepartment(departmentId);
    }
    /*
     * API get all Users By courseId
     */
    @RequestMapping(value = "/users/courses/{courseIds}", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersByCourseId(@PathVariable List<Integer> courseIds) throws SQLException, Exception {
        return userService.getUsersByCourse(courseIds);
    }
    /*
     * API get all Users By departmentId list
     */
    @RequestMapping(value = "/users/departs/{departmentIds}", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersByDepartment(@PathVariable List<Integer> departmentIds) throws SQLException, Exception {
        return userService.getUsersByDepartments(departmentIds);
    }
    /*
     * API update Users
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.PUT)
    public ResponseEntity<?> setManagerForUser(
            @RequestBody User user) throws SQLException, Exception {
        return userService.setManagerForUser(user);
    }
    /*
     * API get User by userId
     */
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByUserId(@PathVariable("userId") int userId)
            throws SQLException, Exception {
        return userService.getUserById(userId);
    }
    /*
     * API set Department for List User
     */
    @RequestMapping(value = "/users/departments/{departmentId}", method = RequestMethod.PUT)
    public ResponseEntity<?> setDepartmentsForUsers(
            @PathVariable("departmentId") int departmentId,
            @RequestBody List<User> users) throws SQLException, Exception {
        return userService.setDepartmentsForUser(users, departmentId);
    }
    /*
     * API set Role for List User
     */
    @PutMapping(value = "/users/setRolesForUser/{departmentId}/{roleId}")
    public ResponseEntity<?> setRolesForUsers(
            @PathVariable("departmentId") int departmentId, @PathVariable("roleId") int roleId,
            @RequestBody List<User> users) throws SQLException, Exception {
        return userService.setRolesForUser(users, departmentId, roleId);
    }
    /*
     * API set Manager for List User
     */
    @PutMapping(value = "/users/setManagerForUsers/{approvalManagerId}")
    public ResponseEntity<?> setManagerForUser(
            @PathVariable("approvalManagerId") int approvalManagerId,
            @RequestBody List<User> users) throws SQLException, Exception {
        return userService.setManagerForUsers(users, approvalManagerId);
    }
    /*
     * API set Active for List User
     */
    @RequestMapping(value = "/users/active", method = RequestMethod.PUT)
    public ResponseEntity<?> setStatusActiveForUsers(
            @RequestBody List<User> users) throws SQLException, Exception {
        return userService.setActives(users);
    }
    /*
     * API get Training Needs
     */
    @RequestMapping(value = "/users/{userId}/traningneeds", method = RequestMethod.GET)
    public ResponseEntity<?> getTraningNeedsByUser(
            @PathVariable("userId") int userId) throws SQLException, Exception {
        return userService.getTraningNeedByUserId(userId);
    }
    /*
     * API get Training Needs detail
     */
    @RequestMapping(value = "/traningneeds/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTraningNeedsById(
            @PathVariable("id") int id) throws SQLException, Exception {
        return userService.getTraningNeedById(id);
    }
    /*
     * API Create Training Needs
     * */
    @RequestMapping(value = "/users/{userId}/traningneeds", method = RequestMethod.POST)
    public ResponseEntity<?> createTraningNeedsByUser(
            @PathVariable("userId") int userId,
            @RequestBody TrainingNeed trainingNeed) throws SQLException,
            Exception {
        trainingNeed.setUserId(userId);
        return userService.createTraningNeedForUser(trainingNeed);
    }
    /*
     * API Search users
     * */
    @RequestMapping(value = "/users/search", method = RequestMethod.GET)
    public ResponseEntity<?> searchByName(@RequestParam("name") String name)
            throws SQLException, ObjectNotFoundException, Exception {
        return userService.searchByName(name);
    }
    
    @RequestMapping(value = "/users/{userId}/userLogin", method = RequestMethod.GET)
    public ResponseEntity<?> getUserLoginById(@PathVariable("userId") int userId)
            throws SQLException, ObjectNotFoundException, Exception {
        return userService.getUserLoginById(userId);
    }
    
    @RequestMapping(value = "/users/{userId}/trainingPlans", method = RequestMethod.GET)
    public ResponseEntity<?> getTrainingPlanByUserId(
            @PathVariable int userId) throws ObjectNotFoundException,
            SQLException, Exception {
        logger.info("TrainingPlanController***getTrainingPlanByUserId:"
                + userId);
        return userService.getTrainingPlanByUserId(userId);
    }
}
