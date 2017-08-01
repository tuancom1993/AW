package com.amway.lms.backend.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.Role;
import com.amway.lms.backend.entity.TrainingNeed;
import com.amway.lms.backend.entity.TrainingPlan;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.UsersModel;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.RoleRepository;
import com.amway.lms.backend.repository.TrainingNeedRepository;
import com.amway.lms.backend.repository.TrainingPlanRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.UserService;

/**
 * @author acton
 *
 */
@Service
@Transactional
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository dao;

    @Autowired
    private TrainingNeedRepository trainingNeedDao;

    @Autowired
    private RoleRepository roleDao;

    @Autowired
    private DepartmentRepository departmentDao;

    @Autowired
    private TrainingPlanRepository trainingPlanDao;

    // Set Department for list Users
    @Override
    public ResponseEntity<?> setDepartmentsForUser(List<User> users, int departmentId) {
        try {
            List<User> usersReturn = new ArrayList<>();
            for (User user : users) {
                User userForUpdate = dao.getUserById(user.getId());
                userForUpdate.setDepartmentId(departmentId);
                dao.editUser(userForUpdate);
                usersReturn.add(userForUpdate);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setDepartmentsForUser " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    // Set Roles for list Users
    @Override
    public ResponseEntity<?> setRolesForUser(List<User> users, int departmentId, int roleId) {
        // TODO
        logger.debug("----setRolesForUser()");
        try {
            if (users == null || users.size() == 0)
                throw new Exception("List users is null or empty");

            Department department = departmentDao.getDepartmentById(departmentId);
            if (department == null)
                throw new ObjectNotFoundException("Cannot find dep by id = " + departmentId);

            List<User> usersReturn = new ArrayList<>();

            if (roleId == Roles.HOD.getIntValue()) {
                User user = dao.getUserById(users.get(0).getId());
                setRoleHODForUser(user, department);
                usersReturn.add(user);
            } else if (roleId == Roles.ADMIN.getIntValue()) {
                User user = dao.getUserById(users.get(0).getId());
                setRoleAdminForUser(user, department);
                usersReturn.add(user);
            } else {
                setAnotherRolesForUser(users, roleId, department, usersReturn);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setRolesForUser " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    private void setAnotherRolesForUser(List<User> users, int newRoleId, Department department,
            List<User> usersReturn) {
        for (User user : users) {
            user = dao.getUserById(user.getId());
            if (user.getRoleId() == Roles.ADMIN.getIntValue() || user.getRoleId() == Roles.HOD.getIntValue()) {
                logger.info("User is HOD or ADMIN. Cannot Set Role");
                continue;
            }

            if (user.getRoleId() == newRoleId) {
                logger.info("New role is old role of User");
                continue;
            }
            checkAMChangeRoleToRemoveApprovalManagerId(user);
            if (newRoleId == Roles.APPROVAL_MANAGER.getIntValue()) {
                user.setApprovalManagerId(department.getHodId());
            }
            user.setRoleId(newRoleId);
            dao.editUser(user);
            usersReturn.add(user);
        }
    }

    private void setRoleHODForUser(User user, Department department) throws Exception {
        if (user.getRoleId() == Roles.ADMIN.getIntValue() || user.getRoleId() == Roles.HOD.getIntValue())
            throw new Exception("User is HOD or ADMIN. Cannot Set Role");

        User hodCurrent = dao.getHodByDepartmentId(department.getId());
        /*
         * if (hodCurrent == null) { logger.info("This dept don't have HOD");
         * 
         * department.setHodId(user.getId());
         * departmentDao.updateDepartment(department);
         * 
         * user.setRoleId(Roles.HOD.getIntValue()); dao.editUser(user);
         * 
         * checkAMChangeRoleToRemoveApprovalManagerId(user, Roles.HOD.getIntValue());
         * 
         * List<User> approvalManagers =
         * dao.getListApprovalManagerByDepartmentId(department.getId()); for (User
         * approvalManager : approvalManagers) {
         * approvalManager.setApprovalManagerId(user.getId());
         * dao.editUser(approvalManager); } return; }
         */

        /*
         * if (user.getId() == hodCurrent.getId()) { logger.info(
         * "The user is still HOD in this dept"); throw new Exception(
         * "The user is still HOD in this dept"); }
         */

        if (user.getDepartmentId() == department.getId()) {
            logger.info("User belong to this Department...");

            if (hodCurrent == null) {
                logger.info("This dept don't have HOD");

                department.setHodId(user.getId());
                departmentDao.updateDepartment(department);

                checkAMChangeRoleToRemoveApprovalManagerId(user);
                user.setRoleId(Roles.HOD.getIntValue());
                dao.editUser(user);

                List<User> approvalManagers = dao.getListApprovalManagerByDepartmentId(department.getId());
                for (User approvalManager : approvalManagers) {
                    approvalManager.setApprovalManagerId(user.getId());
                    dao.editUser(approvalManager);
                }
            } else if (user.getId() == hodCurrent.getId()) {
                logger.info("The user is still HOD in this dept");
            } else if (hodCurrent.getDepartmentId() == user.getDepartmentId()) {
                logger.info(
                        "User and hodCurrent are belong same dept. (The current HOD is belong and manage this dept)");

                checkAMChangeRoleToRemoveApprovalManagerId(user);
                user.setRoleId(Roles.HOD.getIntValue());
                dao.editUser(user);

                hodCurrent.setRoleId(Roles.TRAINEE.getIntValue());
                dao.editUser(hodCurrent);

                List<Department> departments = departmentDao.getDepartmentListByHodId(hodCurrent.getId());
                for (Department department2 : departments) {
                    department2.setHodId(user.getId());
                    departmentDao.updateDepartment(department2);

                    List<User> approvalManagers = dao.getListApprovalManagerByDepartmentId(department2.getId());
                    for (User approvalManager : approvalManagers) {
                        approvalManager.setApprovalManagerId(user.getId());
                        dao.editUser(approvalManager);
                    }
                }
            } else if (hodCurrent.getDepartmentId() != user.getDepartmentId()) {
                logger.info("The current HOD is just manage this dept (Not belong in this dept");
                department.setHodId(user.getId());
                departmentDao.updateDepartment(department);

                checkAMChangeRoleToRemoveApprovalManagerId(user);
                user.setRoleId(Roles.HOD.getIntValue());
                dao.editUser(user);

                List<User> approvalManagers = dao.getListApprovalManagerByDepartmentId(department.getId());
                for (User approvalManager : approvalManagers) {
                    approvalManager.setApprovalManagerId(user.getId());
                    dao.editUser(approvalManager);
                }
            } else {
                logger.info("return!");
                return;
            }
        } else if (user.getDepartmentId() != department.getId()) {
            logger.info("User not belong this Department");
            if (user.getRoleId() == Roles.HOD.getIntValue()) {
                logger.info("User is Hod...(Just want manage). But the Dept have HOD. Cannot set HOD for this Dept");
                throw new Exception("Cannot set HOD for this Dept");
            } else {
                logger.info("User is not Hod...Cannot set HOD for this Dept");
                throw new Exception("User is not Hod and not belong this Department...Cannot set HOD for this Dept");
            }
        }
    }

    private void setRoleAdminForUser(User user, Department department) throws Exception {
        if (user.getRoleId() == Roles.ADMIN.getIntValue() || user.getRoleId() == Roles.HOD.getIntValue())
            throw new Exception("User is HOD or ADMIN. Cannot Set Role");

        User currentAdmin = dao.getAdminByDepartmentId(department.getId());
        if (user.getDepartmentId() == department.getId()) {
            logger.info("User is belong this Dept. Can set role");
            if (currentAdmin == null || currentAdmin.getDepartmentId() != department.getId()) {
                logger.info("This Dept don't have Admin or current Admin not belong this Dept (Just Manage)");
                department.setAdminId(user.getId());
                departmentDao.updateDepartment(department);

                checkAMChangeRoleToRemoveApprovalManagerId(user);
                user.setRoleId(Roles.ADMIN.getIntValue());
                dao.editUser(user);
            } else {
                logger.info("This Admin belong this Dept and Manage this Dept");

                currentAdmin.setRoleId(Roles.TRAINEE.getIntValue());
                dao.editUser(currentAdmin);

                checkAMChangeRoleToRemoveApprovalManagerId(user);
                user.setRoleId(Roles.ADMIN.getIntValue());
                dao.editUser(user);

                List<Department> departments = departmentDao.getDepartmentListByAdminId(currentAdmin.getId());
                for (Department department2 : departments) {
                    department2.setAdminId(user.getId());
                }
            }
        }
    }

    private void checkAMChangeRoleToRemoveApprovalManagerId(User user) {
        if (user.getRoleId() == Roles.APPROVAL_MANAGER.getIntValue()) {
            logger.info("This user is Aprroval Manager will change another Role");
            user.setApprovalManagerId(0);
            List<User> usersApproveByAM = dao.getListUserByApprovalMangerId(user.getId());
            for (User employee : usersApproveByAM) {
                employee.setApprovalManagerId(0);
                dao.editUser(employee);
            }
        }
    }

    // Set Roles for list Users
    @Override
    public ResponseEntity<?> setManagerForUsers(List<User> users, int approvalManagerId) {
        
        logger.debug("----setManagerForUser()");
        try {
            List<User> usersReturn = new ArrayList<>();
            if (users == null || users.size() == 0)
                throw new Exception("List users is null or empty");
            for (User user : users) {
                User userForUpdate = dao.getUserById(user.getId());
                userForUpdate.setApprovalManagerId(approvalManagerId);
                dao.editUser(userForUpdate);
                usersReturn.add(userForUpdate);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setManagerForUser " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    // Set Active/Deactive for list users
    @Override
    public ResponseEntity<?> setActives(List<User> users) {
        try {
            List<User> usersReturn = new ArrayList<>();
            for (User user : users) {
                User userForUpdate = dao.getUserById(user.getId());
                if (user.getActive() == 0) {
                    userForUpdate.setActive(Byte.valueOf("1"));
                } else {
                    userForUpdate.setActive(Byte.valueOf("0"));
                }
                dao.editUser(userForUpdate);
                usersReturn.add(userForUpdate);
            }
            return Utils.generateSuccessResponseEntity(usersReturn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setActives " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    /*
     * Get User Lists
     * 
     * @see com.amway.lms.backend.service.EntityService#getList()
     */
    @Override
    public ResponseEntity<?> getUserList() {
        List<UsersModel> users = new LinkedList<UsersModel>();
        List<User> listUser = dao.getUserList();
        for (User u : listUser) {
            UsersModel user = new UsersModel();
            Department department = departmentDao.getDepartmentById(u.getDepartmentId());
            if (department == null) {
                department = Common.getEmptyDepartment();
            }
            Role role = roleDao.getRoleById(u.getRoleId());

            User approvalManager = dao.getUserById(u.getApprovalManagerId());
            if (approvalManager == null)
                approvalManager = Common.getEmptyUser();

            User manager = dao.getUserById(u.getManagerId());
            if (manager == null)
                manager = Common.getEmptyUser();

            User hod = dao.getHodOfUserByUserId(u.getId());
            if (hod == null)
                hod = Common.getEmptyUser();

            if (role != null) {
                // Set values for Users Model
                user.setUserID(u.getUserID());
                user.setDepartment(department);
                user.setRole(role);
                user.setId(u.getId());
                user.setFirstName(u.getFirstName());
                user.setLastName(u.getLastName());
                user.setFullName(u.getFirstName() + " " + u.getLastName());
                user.setEmail(u.getEmail());
                user.setJobTitle(u.getJobTitle());
                user.setDepartmentId(u.getDepartmentId());
                user.setApprovalManagerId(approvalManager.getId());
                user.setApprovalManagerName(approvalManager.getFirstName() + " " + approvalManager.getLastName());
                user.setHodId(hod.getId());
                user.setHodName(hod.getFirstName() + " " + hod.getLastName());
                user.setManagerId(manager.getId());
                user.setManagerName(manager.getFirstName() + " " + manager.getLastName());
                user.setIsDeleted(u.getIsDeleted());
                user.setNote(u.getNote());
                user.setCreatedAt(u.getCreatedAt());
                user.setUpdatedAt(u.getUpdatedAt());
                user.setOfficeLocation(u.getOfficeLocation());
                user.setRoleId(u.getRoleId());
                user.setActive(u.getActive());
                users.add(user);
            }
        }
        return Utils.generateSuccessResponseEntity(users);
    }

    @Override
    public ResponseEntity<?> getHodList() {
        List<User> hodList = dao.getHodList();
        return Utils.generateSuccessResponseEntity(hodList);
    }

    @Override
    public ResponseEntity<?> getHodAndAdminList() {
        List<User> hodAndAdminList = dao.getHodAndAdminList();
        return Utils.generateSuccessResponseEntity(hodAndAdminList);
    }

    @Override
    public ResponseEntity<?> getCoodinator() {
        List<User> listCoodinator = dao.getCoodinator();
        return Utils.generateSuccessResponseEntity(listCoodinator);
    }

    @Override
    public ResponseEntity<?> getApprovalManagerList() {
        List<User> listApprovalManager = dao.getApprovalManager();
        return Utils.generateSuccessResponseEntity(listApprovalManager);
    }

    /*
     * Get Direct Line Manager Lists by departmentId
     * 
     * @see com.amway.lms.backend.service.EntityService#getList()
     */
    @Override
    public ResponseEntity<?> getDirectsByDepartment(int departmentId) {
        List<Integer> directIds = dao.getDirectIds(departmentId);
        List<User> userDirects = new ArrayList<>();
        if (directIds != null) {
            for (Integer directId : directIds) {
                User directManager = dao.getUserById(directId.intValue());
                if (directManager != null)
                    userDirects.add(directManager);
            }
        }
        return Utils.generateSuccessResponseEntity(userDirects);
    }

    /*
     * Get User Lists of Participants
     * 
     * @see com.amway.lms.backend.service.EntityService#getList()
     */
    @Override
    public ResponseEntity<?> getUserListParticipants() {
        List<UsersModel> users = new LinkedList<UsersModel>();
        List<User> listUser = dao.getListOfParticipants();
        for (User u : listUser) {
            UsersModel user = new UsersModel();
            Department department = departmentDao.getDepartmentById(u.getDepartmentId());
            if (department == null) {
                department = Common.getEmptyDepartment();
            }
            if (department != null) {
                // Set values for Users Model
                user.setDepartment(department);
                user.setJobTitle(u.getJobTitle());
                user.setId(u.getId());
                user.setFirstName(u.getFirstName());
                user.setLastName(u.getLastName());
                user.setEmail(u.getEmail());
                user.setStaffCode(u.getStaffCode());
                user.setDepartmentId(u.getDepartmentId());
                user.setIsDeleted(u.getIsDeleted());
                user.setNote(u.getNote());
                user.setCreatedAt(u.getCreatedAt());
                user.setUpdatedAt(u.getUpdatedAt());
                user.setOfficeLocation(u.getOfficeLocation());
                user.setRoleId(u.getRoleId());
                user.setActive(u.getActive());
                users.add(user);
            }
        }
        return Utils.generateSuccessResponseEntity(users);
    }
    /*
     * Get User Lists getUsersByCourseParticipant
     * 
     * @see com.amway.lms.backend.service.EntityService#getList()
     */

    @Override
    public ResponseEntity<?> sendingListUsers(int courseId) {
        List<UsersModel> users = new LinkedList<UsersModel>();
        List<User> listUser = dao.sendingListUsers(courseId);
        for (User u : listUser) {
            UsersModel user = new UsersModel();
            Department department = departmentDao.getDepartmentById(u.getDepartmentId());
            if (department == null) {
                department = new Department();
                department.setName("");
            }
            if (department != null) {
                // Set values for Users Model
                user.setDepartment(department);
                user.setJobTitle(u.getJobTitle());
                user.setId(u.getId());
                user.setFirstName(u.getFirstName());
                user.setLastName(u.getLastName());
                user.setEmail(u.getEmail());
                user.setStaffCode(u.getStaffCode());
                user.setDepartmentId(u.getDepartmentId());
                user.setIsDeleted(u.getIsDeleted());
                user.setNote(u.getNote());
                user.setCreatedAt(u.getCreatedAt());
                user.setUpdatedAt(u.getUpdatedAt());
                user.setOfficeLocation(u.getOfficeLocation());
                user.setRoleId(u.getRoleId());
                user.setActive(u.getActive());
                users.add(user);
            }
        }
        return Utils.generateSuccessResponseEntity(users);
    }

    /*
     * Get user by id
     * 
     * @see com.amway.lms.backend.service.EntityService#getEntityById(int)
     */
    @Override
    public ResponseEntity<?> getUserById(int userId) {
        try {
            UsersModel user = new UsersModel();
            List<Role> roles = roleDao.getRoleList();
            List<Department> departments = departmentDao.getDepartmentList();
            List<User> users = dao.getUserList();
            User u = dao.getUserById(userId);

            User approvalManager = dao.getUserById(u.getApprovalManagerId());
            if (approvalManager == null)
                approvalManager = Common.getEmptyUser();

            User manager = dao.getUserById(u.getManagerId());
            if (manager == null)
                manager = Common.getEmptyUser();

            User hod = dao.getHodOfUserByUserId(u.getId());
            if (hod == null)
                hod = Common.getEmptyUser();

            // Set value into Model
            user.setId(u.getId());
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
            user.setEmail(u.getEmail());
            user.setJobTitle(u.getJobTitle());
            user.setDepartmentId(u.getDepartmentId());
            user.setApprovalManagerId(u.getApprovalManagerId());
            user.setApprovalManagerName(approvalManager.getFirstName() + " " + approvalManager.getLastName());
            user.setHodId(hod.getId());
            user.setHodName(hod.getFirstName() + " " + hod.getLastName());
            user.setManagerId(manager.getId());
            user.setManagerName(manager.getFirstName() + " " + manager.getLastName());
            user.setIsDeleted(u.getIsDeleted());
            user.setNote(u.getNote());
            user.setCreatedAt(u.getCreatedAt());
            user.setUpdatedAt(u.getUpdatedAt());
            user.setOfficeLocation(u.getOfficeLocation());
            user.setRoleId(u.getRoleId());
            user.setActive(u.getActive());
            user.setRoles(roles);
            user.setDepartments(departments);
            user.setUsers(users);
            return Utils.generateSuccessResponseEntity(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getUserById " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Get user by department
     * 
     * @see com.amway.lms.backend.service.EntityService#getEntityById(int)
     */
    @Override
    public ResponseEntity<?> getUsersByDepartment(int departmentId) {
        try {
            List<User> users = dao.getListUserByDepartmentId(departmentId);
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getUsersByDepartment " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getUserExceptAmAndHodByDepartment(int departmentId) {
        try {
            List<User> users = dao.getListUserExceptAmAndHodByDepartmentId(departmentId);
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getListUserExceptAmAndHodByDepartmentId " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> usersWithoutAdminAndHodByDepartment(int departmentId) {
        try {
            List<User> users = dao.getListUsersWithoutAdminAndHodByDepartment(departmentId);
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getListUserExceptAmAndHodByDepartmentId " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getApprovalManagerByDepartment(int departmentId) {
        try {
            List<User> users = dao.getListApprovalManagerByDepartmentId(departmentId);
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getListUserExceptAmAndHodByDepartmentId " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getHodByDepartmentId(int departmentId) {
        try {
            User user = dao.getHodByDepartmentId(departmentId);
            return Utils.generateSuccessResponseEntity(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getListUserExceptAmAndHodByDepartmentId " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Update user
     */
    @Override
    public ResponseEntity<?> setManagerForUser(User user) {
        try {
            if (!dao.exists(user.getId()))
                throw new Exception("User " + user.getId() + " doesn't exixts");
            User currentUser = dao.getUserById(user.getId());
            if (currentUser != null) {
                User hod = dao.getHodOfUserByUserId(currentUser.getId());
                if (hod == null)
                    hod = Common.getEmptyUser();

                currentUser.setApprovalManagerId(user.getApprovalManagerId());
                // TODO Warring currentUser.setHodId(user.getHodId());
                currentUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                dao.editUser(currentUser);
            }
            return Utils.generateSuccessResponseEntity(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - updateUser " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getUsersByCourse(List<Integer> courseIds) {
        try {
            List<User> users = new ArrayList<>();
            for (Integer param : courseIds) {
                List<User> usersByCourse = dao.getUsersByCourseId(param);
                for (User u : usersByCourse) {
                    users.add(u);
                }
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getUserByCourse " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getUsersByDepartments(List<Integer> departmentIds) {
        try {
            List<User> users = new ArrayList<>();
            for (Integer param : departmentIds) {
                List<User> usersByDepartment = dao.getListUserByDepartmentId(param);
                for (User u : usersByDepartment) {
                    users.add(u);
                }
            }
            return Utils.generateSuccessResponseEntity(users);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getUserByDepartment " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * @see com.amway.lms.backend.service.UserService#getTraningNeedByUserId(int)
     */
    @Override
    public ResponseEntity<?> getTraningNeedByUserId(int userId) {
        try {
            return new ResponseEntity<>(trainingNeedDao.getListTraningNeedByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTraningNeedByUserId " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getTraningNeedById(int id) {
        try {
            return new ResponseEntity<>(trainingNeedDao.getTrainingNeedById(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTraningNeedById " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * @see com.amway.lms.backend.service.UserService#createTraningNeedForUser(com
     * .amway.lms.backend.model.TrainingNeed)
     */
    @Override
    public ResponseEntity<?> createTraningNeedForUser(TrainingNeed trainingNeed) {
        try {
            if (!dao.exists(trainingNeed.getUserId()))
                throw new Exception("User " + trainingNeed.getUserId() + " doesn't exixts");
            trainingNeedDao.addTrainingNeed(trainingNeed);
            return new ResponseEntity<>(trainingNeed, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - createTraningNeedForUser " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> searchByName(String name) throws ObjectNotFoundException {
        List<UsersModel> users = new LinkedList<UsersModel>();
        List<User> listUser = dao.searchByName(name);
        if (listUser == null || listUser.size() == 0)
            throw new ObjectNotFoundException();
        for (User u : listUser) {
            UsersModel user = new UsersModel();
            Department department = departmentDao.getDepartmentById(u.getDepartmentId());
            if (department == null) {
                department = Common.getEmptyDepartment();
            }
            Role role = roleDao.getRoleById(u.getRoleId());

            User approvalManager = dao.getUserById(u.getApprovalManagerId());
            if (approvalManager == null)
                approvalManager = Common.getEmptyUser();

            User manager = dao.getUserById(u.getManagerId());
            if (manager == null)
                manager = Common.getEmptyUser();

            User hod = dao.getHodOfUserByUserId(u.getId());
            if (hod == null)
                hod = Common.getEmptyUser();

            if (department != null && role != null) {
                // Set values for Users Model
                user.setDepartment(department);
                user.setRole(role);
                user.setId(u.getId());
                user.setFirstName(u.getFirstName());
                user.setLastName(u.getLastName());
                user.setEmail(u.getEmail());
                user.setJobTitle(u.getJobTitle());
                user.setDepartmentId(u.getDepartmentId());
                user.setApprovalManagerId(u.getApprovalManagerId());
                user.setApprovalManagerName(approvalManager.getFirstName() + " " + approvalManager.getLastName());
                user.setHodId(hod.getId());
                user.setHodName(hod.getFirstName() + " " + hod.getLastName());
                user.setManagerId(u.getManagerId());
                user.setManagerName(manager.getFirstName() + " " + manager.getLastName());
                user.setIsDeleted(u.getIsDeleted());
                user.setNote(u.getNote());
                user.setCreatedAt(u.getCreatedAt());
                user.setUpdatedAt(u.getUpdatedAt());
                user.setOfficeLocation(u.getOfficeLocation());
                user.setRoleId(u.getRoleId());
                user.setActive(u.getActive());
                user.setUserID(u.getUserID());
                users.add(user);

            } else {
                logger.error("EXCEPTION");
            }
        }
        return Utils.generateSuccessResponseEntity(users);
    }

    @Override
    public User getUserByUserID(String userID) {
        User user = dao.getUserByUserID(userID);
        if (user == null) {
            return null;
        }
        List<Role> roles = new ArrayList<>();
        Role role = roleDao.getRoleById(user.getRoleId());
        roles.add(role);
        user.setRoles(roles);
        Department department = departmentDao.getDepartmentById(user.getId());
        user.setDepartment(department == null ? Common.getEmptyDepartment() : department);
        return user;
    }

    @Override
    public ResponseEntity<?> getUserLoginById(int userId) {
        try {
            User user = dao.getUserById(userId);
            if (user == null) {
                return null;
            }
            List<Role> roles = new ArrayList<>();
            Role role = roleDao.getRoleById(user.getRoleId());
            roles.add(role);
            user.setRoles(roles);
            Department department = departmentDao.getDepartmentById(user.getId());
            user.setDepartment(department == null ? Common.getEmptyDepartment() : department);
            return Utils.generateSuccessResponseEntity(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getTrainingPlanByUserId(int userId) {
        try {
            List<TrainingPlan> trainingPlans = new ArrayList<>();
            trainingPlans = this.trainingPlanDao.getTrainingPlanByUserId(userId);
            return Utils.generateSuccessResponseEntity(trainingPlans);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getTrainingPlanByUserId " + e.getMessage());
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }
}
