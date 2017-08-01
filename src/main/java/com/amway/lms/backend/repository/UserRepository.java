package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.User;

/**
 * @author acton
 *
 */
public interface UserRepository {
    public List<User> getListUserByDepartmentId(int departmentId);
    
    public List<User> getListUserExceptAmAndHodByDepartmentId(int departmentId);

    public List<User> getListUsersWithoutAdminAndHodByDepartment(int departmentId);
    
    public void updateDepartmentIdForUsers(int userId, int departmentId);

    public List<User> getUsersByCourseId(int courseId);

    public boolean exists(int id);

    public List<User> getUserList();

    public List<User> getUsersCheckInOutBySessionId(int sessionId, String name, int completionStatus);

    public List<User> getUsersCheckinCheckout(int sessionId, String name);

    public void addUser(User user);

    public User getUserById(int userId);

    public void editUser(User user);

    public User searchByUserIdandStaffCode(int UserId, String staffCode);

    public List<User> searchByName(String name);

    public List<Integer> getUserByApprovalMangerId(int managerId);

    public List<Integer> getUserByHodId(int hodId);

    public List<Integer> getUserByHodIdAndDepartment(int hodId, int departmentId);

    public List<Integer> getTraineeByApprovalManager(int approvalManagerId);

    public List<Integer> getListUserIdOfAmByDepartment(int approvalManagerId, int departmentId);

    public List<User> getListUserByApprovalMangerId(int managerId);

    public List<User> getListOfParticipants();

    public List<User> getUsersOfTrainingPlan(int trainingPlanId, int existed, Integer departmentId, User userLogin);
    
    public List<User> sendingListUsers(int courseId);

    public List<Integer> getDirectIds(int departmentId);

    public List<User> getHodList();

    public List<User> getCoodinator();

    public List<User> getApprovalManager();
    
    public List<User> getHodAndAdminList();

    /*
     * Acton
     */
    public User getUserByUserID(String userID);

    /**
     * Dino
     */

    public List<User> getListUserAddParticipant(Integer sessionId);

    public List<User> getListUserAddParticipantByDepartmentId(Integer sessionId, Integer departmentId);

    public List<User> getSubmmittedListUser(Integer sessionId);

    public User getUserByEmail(String email);

    public User getHodOfUserByUserId(int userId);

    public User getAdminOfUserByUserId(int userId);

    public User getHodByDepartmentId(int departmentId);

    public User getAdminByDepartmentId(int departmentId);

    public List<User> getListApprovalManagerByHODId(int managerId);

    public List<User> getListApprovalManagerByDepartmentId(int departmentId);

    public List<User> getListUserAddParticipantBySessionIdAndAdminId(int sessionId, int userId);
    
    public List<Integer> getListApprovalManagerIdByHodId(int hodId);

}