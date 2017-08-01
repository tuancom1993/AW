package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.common.AmwayEnum;
import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.UserRepository;

/**
 * @author acton
 *
 */
@Repository("userDao")
public class UserRepositoryImpl extends AbstractRepository<Integer, User> implements UserRepository {
    @Override
    public List<User> getUserList() {
        Query query = createNamedQuery("getUserList", -1, -1);
        return (List<User>) query.list();
    }

    @Override
    public List<User> getCoodinator() {
        Query query = createNamedQuery("getCoodinatorList", -1, -1);
        return (List<User>) query.list();
    }

    @Override
    public List<User> getListOfParticipants() {
        Query query = createNamedQuery("getParticipantsList", -1, -1);
        return (List<User>) query.list();
    }

    @Override
    public List<User> sendingListUsers(int courseId) {
        // Query query = createNamedQuery("getUserList", -1, -1);
        String queryString = "SELECT u FROM User u where u.id in (select cp.userId from CourseParticipant cp where cp.courseId in (select c.id from Course c where c.id=:courseId))";
        Query query = getSession().createQuery(queryString);
        query.setParameter("courseId", courseId);
        return (List<User>) query.list();
    }

    @Override
    public void addUser(User user) {
        getSession().saveOrUpdate(user);

    }

    @Override
    public User getUserById(int userId) {
        return getByKey(userId);
    }

    @Override
    public List<User> getHodList() {
        Query query = createNamedQuery("getHodList", -1, -1);
        return (List<User>) query.list();
    }

    @Override
    public List<User> getApprovalManager() {
        Query query = createNamedQuery("getApprovalManagerList", -1, -1);
        return (List<User>) query.list();
    }

    @Override
    public List<User> getHodAndAdminList() {
        Query query = createNamedQuery("getHodAndAdminList", -1, -1);
        return (List<User>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getDirectIds(int departmentId) {
        String queryString = "select u.id from User u where departmentId = :departmentId and u.roleId=2";
        Query query = getSession().createQuery(queryString);
        query.setParameter("departmentId", departmentId);
        return (List<Integer>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getUsersByCourseId(int courseId) {
        // String queryString = "from User where id in (select userId from
        // CourseParticipant where courseId = :courseId)";
        // Query query = getSession().createQuery(queryString);
        // query.setParameter("courseId", courseId);
        // return (List<User>) query.list();
        String sql = "Select * from users u " + "Inner Join session_participants sp On u.id = sp.user_id "
                + "Inner Join sessions ss On ss.id = sp.session_id " + "Inner Join courses c On c.id = ss.course_id "
                + "Where c.id = :courseId";
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        sqlQuery.setParameter("courseId", courseId);
        sqlQuery.addEntity(User.class);
        return sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getListUserByDepartmentId(int departmentId) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("departmentId", departmentId));
        return criteria.list();
    }

    @Override
    public void updateDepartmentIdForUsers(int userId, int departmentId) {
        String hql = "Update User Set departmentId = :_departmentId Where id = :_userId";
        Query query = getSession().createQuery(hql);
        query.setInteger("_departmentId", departmentId);
        query.setInteger("_userId", userId);
        query.executeUpdate();
    }

    @Override
    public boolean exists(int id) {
        try {
            return getByKey(id) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void editUser(User user) {
        update(user);
    }

    @Override
    public User searchByUserIdandStaffCode(int userId, String staffCode) {
        Query query = createNamedQuery("searchByUserIdandStaffCode", -1, -1, userId, staffCode);
        return (User) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> searchByName(String name) {
        String searchStr = "%" + name + "%";
        Query query = createNamedQuery("searchUser", -1, -1, searchStr, searchStr);
        return (List<User>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getUserByApprovalMangerId(int managerId) {
        Query query = createNamedQuery("getUserByApprovalMangerId", -1, -1, managerId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getUserByHodId(int hodId) {
        Query query = createNamedQuery("getUserByHodId", -1, -1, hodId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getUserByHodIdAndDepartment(int hodId, int departmentId) {
        Query query = createNamedQuery("getUserByHodIdAndDepartmentId", -1, -1, hodId, departmentId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getTraineeByApprovalManager(int approvalManagerId) {
        Query query = createNamedQuery("getTraineeByApprovalManager", -1, -1, approvalManagerId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> getListUserIdOfAmByDepartment(int approvalManagerId, int departmentId) {
        Query query = createNamedQuery("getUserByAmIdAndDepartmentId", -1, -1, approvalManagerId, departmentId);
        return query.list();
    }

    @Override
    public User getUserByUserID(String userID) {
        Query query = createNamedQuery("getUserByUserID", -1, -1, userID);
        return (User) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getListUserByApprovalMangerId(int managerId) {
        Query query = createNamedQuery("getListUserByApprovalMangerId", -1, -1, managerId);
        return query.list();
    }

    @Override
    public List<User> getUsersOfTrainingPlan(int trainingPlanId, int existed, Integer departmentId, User userLogin) {
        Query query;
        if (existed == 0) {
            if (departmentId == null || departmentId == -1) {
                if (userLogin.getRoleId() == Roles.HR.getIntValue()) {
                    query = createNamedQuery("getAllUsersOfTrainingPlan", -1, -1, trainingPlanId);
                } else {
                    query = createNamedQuery("getUsersOfTrainingPlan", -1, -1, trainingPlanId, userLogin.getId());
                }
            } else {
                query = createNamedQuery("getUsersOfTrainingPlanByDepartment", -1, -1, trainingPlanId, departmentId);
            }
        } else {
            if (departmentId == null || departmentId == -1) {
                if (userLogin.getRoleId() == Roles.HR.getIntValue()) {
                    query = createNamedQuery("getExistedAllUsersOfTrainingPlan", -1, -1, trainingPlanId);
                } else {
                    query = createNamedQuery("getExistedUsersOfTrainingPlan", -1, -1, trainingPlanId,
                            userLogin.getId());
                }
            } else {
                query = createNamedQuery("getExistedUsersOfTrainingPlanByDepartment", -1, -1, trainingPlanId,
                        departmentId);
            }
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getListUserAddParticipant(Integer sessionId) {
        Query query = createNamedQuery("getListUserAddParticipant", -1, -1, sessionId, sessionId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getListUserAddParticipantByDepartmentId(Integer sessionId, Integer departmentId) {
        Query query = createNamedQuery("getListUserAddParticipantByDepartmentId", -1, -1, sessionId, sessionId,
                departmentId);
        return query.list();
    }

    @Override
    public List<User> getSubmmittedListUser(Integer sessionId) {
        Query query = createNamedQuery("getSubmmittedListUser", -1, -1, sessionId);
        return query.list();
    }

    @Override
    public List<User> getUsersCheckInOutBySessionId(int sessionId, String name, int completionStatus) {
        Query query;
        if (name == null) {
            if (completionStatus == AmwayEnum.CompletionStatus.ACCEPTED.getValue()) {
                // Get user for checkin
                query = createNamedQuery("getUsersCheckInBySessionId", -1, -1, sessionId,
                        AmwayEnum.CompletionStatus.ACCEPTED.getValue(),
                        AmwayEnum.CompletionStatus.PARTICIPATED.getValue());
            } else {
                // Get user for checkout
                query = createNamedQuery("getUsersCheckOutBySessionId", -1, -1, sessionId,
                        AmwayEnum.CompletionStatus.PARTICIPATED.getValue(),
                        AmwayEnum.CompletionStatus.ENDED_SESSION.getValue(),
                        AmwayEnum.CompletionStatus.COMPLETED.getValue());
            }
        } else {
            name = "%" + name + "%";
            if (completionStatus == AmwayEnum.CompletionStatus.ACCEPTED.getValue()) {
                query = createNamedQuery("getUsersCheckInBySessionIdAndName", -1, -1, sessionId,
                        AmwayEnum.CompletionStatus.ACCEPTED.getValue(),
                        AmwayEnum.CompletionStatus.PARTICIPATED.getValue(), name, name);
            } else {
                query = createNamedQuery("getUsersCheckOutBySessionIdAndName", -1, -1, sessionId,
                        AmwayEnum.CompletionStatus.PARTICIPATED.getValue(),
                        AmwayEnum.CompletionStatus.ENDED_SESSION.getValue(),
                        AmwayEnum.CompletionStatus.COMPLETED.getValue(), name, name);

            }
        }
        return (List<User>) query.list();
    }

    @Override
    public List<User> getUsersCheckinCheckout(int sessionId, String name) {
        Query query;
        if (name == null) {
            query = createNamedQuery("getUsersCheckinCheckout", -1, -1, sessionId,
                    AmwayEnum.CompletionStatus.ACCEPTED.getValue(), AmwayEnum.CompletionStatus.PARTICIPATED.getValue(),
                    AmwayEnum.CompletionStatus.ENDED_SESSION.getValue(),
                    AmwayEnum.CompletionStatus.COMPLETED.getValue());
        } else {
            name = "%" + name + "%";
            query = createNamedQuery("getUsersCheckinCheckoutByName", -1, -1, sessionId,
                    AmwayEnum.CompletionStatus.ACCEPTED.getValue(), AmwayEnum.CompletionStatus.PARTICIPATED.getValue(),
                    AmwayEnum.CompletionStatus.ENDED_SESSION.getValue(),
                    AmwayEnum.CompletionStatus.COMPLETED.getValue(), name, name);
        }
        return (List<User>) query.list();
    }

    @Override
    public User getUserByEmail(String email) {
        Query query = createNamedQuery("getUserByEmail", 0, 1, email);
        return (User) query.uniqueResult();
    }

    @Override
    public User getHodOfUserByUserId(int userId) {
        Query query = createNamedQuery("getHodOfUserByUserId", 0, 1, userId);
        return (User) query.uniqueResult();
    }

    @Override
    public User getAdminOfUserByUserId(int userId) {
        Query query = createNamedQuery("getAdminOfUserByUserId", 0, 1, userId);
        return (User) query.uniqueResult();
    }

    @Override
    public User getHodByDepartmentId(int departmentId) {
        Query query = createNamedQuery("getHodByDepartmentId", -1, -1, departmentId);
        return (User) query.uniqueResult();
    }

    @Override
    public User getAdminByDepartmentId(int departmentId) {
        Query query = createNamedQuery("getAdminByDepartmentId", 0, 1, departmentId);
        return (User) query.uniqueResult();
    }

    @Override
    public List<User> getListApprovalManagerByHODId(int managerId) {
        Query query = createNamedQuery("getListApprovalManagerByHODId", -1, -1, managerId);
        return query.list();
    }

    @Override
    public List<User> getListApprovalManagerByDepartmentId(int departmentId) {
        Query query = createNamedQuery("getListApprovalManagerByDepartmentId", -1, -1, departmentId);
        return query.list();
    }

    @Override
    public List<User> getListUserExceptAmAndHodByDepartmentId(int departmentId) {
        Query query = createNamedQuery("getListExceptAmAndHodByDepartmentId", -1, -1, departmentId);
        return query.list();
    }

    @Override
    public List<User> getListUsersWithoutAdminAndHodByDepartment(int departmentId) {
        Query query = createNamedQuery("getListUsersWithoutAdminAndHodByDepartment", -1, -1, departmentId);
        return query.list();
    }

    @Override
    public List<User> getListUserAddParticipantBySessionIdAndAdminId(int sessionId, int userId) {
        Query query = createNamedQuery("getListUserAddParticipantBySessionIdAndAdminId", -1, -1, sessionId, sessionId,
                userId);
        return query.list();
    }

    @Override
    public List<Integer> getListApprovalManagerIdByHodId(int hodId) {
        Query query = createNamedQuery("getListApprovalManagerIdByHodId", -1, -1, hodId);
        return query.list();
    }
}
