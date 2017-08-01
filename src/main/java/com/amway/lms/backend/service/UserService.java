package com.amway.lms.backend.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.TrainingNeed;
import com.amway.lms.backend.entity.User;

/**
 * @author acton
 *
 */
public interface UserService {
    
    public ResponseEntity<?> getUsersByCourse(List<Integer> courseIds);
    
    public ResponseEntity<?> getUsersByDepartments(List<Integer> departmentIds);

    public ResponseEntity<?> setActives(List<User> users);

    public ResponseEntity<?> getTraningNeedByUserId(int userId);
    
    public ResponseEntity<?> getTraningNeedById(int id);

    public ResponseEntity<?> createTraningNeedForUser(TrainingNeed trainingNeed);

    public ResponseEntity<?> getUserList();
    
    public ResponseEntity<?> getCoodinator();
    
    public ResponseEntity<?> getHodList();
    
    public ResponseEntity<?> getHodAndAdminList();
    
    public ResponseEntity<?> getApprovalManagerList();

    public ResponseEntity<?> getUserById(int userId);
    
    public ResponseEntity<?> setManagerForUser(User user);

    public ResponseEntity<?> setDepartmentsForUser(List<User> users, int departmentId);
    
    public ResponseEntity<?> setRolesForUser(List<User> users, int departmentId, int roleId);
    
    public ResponseEntity<?> setManagerForUsers(List<User> users, int approvalManagerId);
    
    public ResponseEntity<?> searchByName(String name) throws SQLException, Exception;
    
    public ResponseEntity<?> getUserListParticipants();
    
    public ResponseEntity<?> sendingListUsers(int courseId);
    
    public ResponseEntity<?> getUsersByDepartment(int departmentId);
    
    public ResponseEntity<?> getUserExceptAmAndHodByDepartment(int departmentId);
    
    public ResponseEntity<?> getApprovalManagerByDepartment(int departmentId);
    
    public ResponseEntity<?> getHodByDepartmentId(int departmentId);
    
    public ResponseEntity<?> getDirectsByDepartment(int departmentId);
    
    public ResponseEntity<?> usersWithoutAdminAndHodByDepartment(int departmentId);
    
    public ResponseEntity<?> getTrainingPlanByUserId(int userId);
    
    
    /*
     * Acton: Func for Security
     */
    public User getUserByUserID(String userID);

    public ResponseEntity<?> getUserLoginById(int userId);

}
