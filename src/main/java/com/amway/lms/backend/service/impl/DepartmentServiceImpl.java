/**
 * 
 */
package com.amway.lms.backend.service.impl;

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
import com.amway.lms.backend.common.ErrorCode;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.DepartmentModel;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.DepartmentService;

/**
 * @author acton
 *
 */
@Service
@Transactional
@EnableTransactionManagement
public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    UserRepository userDao;

    @Autowired
    DepartmentRepository departmentDao;

    @Override
    public ResponseEntity<?> getDepartmentList() {
        try {
            return new ResponseEntity<List<Department>>(departmentDao.getDepartmentList(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - get List Department " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> setDepartmentForUsers(Department department) throws Exception {
        // try {
        List<User> usersReturn = new ArrayList<>();
        if (!departmentDao.exists(department.getId()))
            throw new Exception("Department " + department.getId() + " doesn't exixts");
        for (User user : department.getUsers()) {
            if (!userDao.exists(user.getId()))
                throw new Exception("User " + user.getId() + " doesn't exixts");
        }
        for (User user : department.getUsers()) {
            userDao.updateDepartmentIdForUsers(user.getId(), department.getId());
            User userRepo = userDao.getUserById(user.getId());
            userRepo.setDepartmentId(department.getId());
            usersReturn.add(userRepo);
        }
        return new ResponseEntity<>(usersReturn, HttpStatus.CREATED);
        // } catch (Exception e) {
        // e.printStackTrace();
        // logger.error("EXCEPTION - setDepartmentForUsers " + e.getMessage());
        // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // }
    }

    @Override
    public ResponseEntity<?> updateDepartment(Department department) throws EditObjectException {
        try {
            Department departmentDB = departmentDao.getDepartmentById(department.getId());
            if (departmentDB == null)
                throw new ObjectNotFoundException("Cannot find Department by id = " + department.getId());

            departmentDB.setAdminId(department.getAdminId());
            departmentDB.setHodId(department.getHodId());
            departmentDB.setUpdatedAt(Utils.getCurrentTime());
            departmentDao.updateDepartment(departmentDB);

            return Utils.generateSuccessResponseEntity(departmentDB);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    @Override
    public ResponseEntity<?> getDepartmentsbyHodOrAdmin(int id) throws ObjectNotFoundException {
        try {
            List<DepartmentModel> departmentModel = new LinkedList<>();
            List<Department> departments = new ArrayList<>();
            User user = userDao.getUserById(id);
            if (user == null)
                throw new ObjectNotFoundException("Cannot find User by id =" + id);

            if (user.getRoleId() == Roles.HOD.getIntValue()) {
                departments = departmentDao.getDepartmentListByHodIdOrWithoutHOD(id);
                if (departments == null)
                    throw new ObjectNotFoundException("Cannot find Department by hodId =" + id);
                departmentModel = getListDepartmentsByHod(departments, id);
                
            } else if (user.getRoleId() == Roles.ADMIN.getIntValue()) {
                departments = departmentDao.getDepartmentListByAdminIdOrWithoutAdmin(id);
                if (departments == null)
                    throw new ObjectNotFoundException("Cannot find Department by adminId =" + id);
                departmentModel = getListDepartmentsByAdmin(departments);
            }
            return Utils.generateSuccessResponseEntity(departmentModel);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_OBJECT_NOT_FOUND_EXCEPTION,
                    ErrorCode.MSG_OBJECT_NOT_FOUND_EXCEPTION);
        }
    }

    public List<DepartmentModel> getListDepartmentsByHod(List<Department> departments, int hodId) {
        List<DepartmentModel> departmentModel = new LinkedList<>();
        for (Department department : departments) {
            User user = userDao.getUserById(hodId);
            DepartmentModel deptModel = new DepartmentModel();
            deptModel.setId(department.getId());
            deptModel.setName(department.getName());
            deptModel.setAdminId(department.getAdminId());
            deptModel.setHodId(department.getHodId());
            if (deptModel.getId() == user.getDepartmentId()) {
                deptModel.setIsHodBelongDepartment(true);
            }
            if (department.getHodId() != 0) {
                deptModel.setIsManageDepartment(true);
            }
            deptModel.setCreatedAt(department.getCreatedAt());
            deptModel.setUpdatedAt(department.getUpdatedAt());
            departmentModel.add(deptModel);
        }
        return departmentModel;
    }

    public List<DepartmentModel> getListDepartmentsByAdmin(List<Department> departments) {
        List<DepartmentModel> departmentModel = new LinkedList<>();
        for (Department department : departments) {
            DepartmentModel deptModel = new DepartmentModel();
            deptModel.setId(department.getId());
            deptModel.setName(department.getName());
            deptModel.setAdminId(department.getAdminId());
            deptModel.setHodId(department.getHodId());
            if (department.getAdminId() != 0) {
                deptModel.setIsManageDepartment(true);
            }
            deptModel.setCreatedAt(department.getCreatedAt());
            deptModel.setUpdatedAt(department.getUpdatedAt());
            departmentModel.add(deptModel);
        }
        return departmentModel;
    }
    
    @Override
    public ResponseEntity<?> manageManagerOfDepartment(int userId, List<Department> departmentIds) throws Exception {
        try {
            logger.info("---manageManagerOfDepartment()");
            User user = userDao.getUserById(userId);

            if (user == null)
                throw new ObjectNotFoundException("Cannot find User by id " + userId);

            List<Department> departments = new ArrayList<>();
            for (Department dept : departmentIds) {
                Department department = departmentDao.getDepartmentById(dept.getId());
                if (department != null)
                    departments.add(department);
            }

            if (user.getRoleId() == Roles.HOD.getIntValue()) {
                setHODForDepartments(user, departments);
            } else if (user.getRoleId() == Roles.ADMIN.getIntValue()) {
                setAdminForDepartments(user, departments);
            } else
                throw new Exception("User is not HOD or ADMIN");

            return Utils.generateSuccessResponseEntity(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateFailureResponseEntity(ErrorCode.CODE_EDIT_OBJECT_EXCEPTION,
                    ErrorCode.MSG_EDIT_OBJECT_EXCEPTION);
        }
    }

    private void setHODForDepartments(User user, List<Department> newDepartments) throws Exception {
        List<Department> currentDepartments = departmentDao.getDepartmentListByHodId(user.getId());

        List<Department> departmentsAddNew = getListDepartmentAddNew(currentDepartments, newDepartments);
        List<Department> departmentsRemoved = getListDepartmentHasBeenRemoved(currentDepartments, newDepartments);

        for(Department department : departmentsAddNew){
            if(userDao.getHodByDepartmentId(department.getId()) != null){
                logger.info("This Dept have has HOD "+department.getName());
                continue;
            }
            
            department.setHodId(user.getId());
            departmentDao.updateDepartment(department);
            List<User> approvalManagers = userDao.getListApprovalManagerByDepartmentId(department.getId());
            for(User approvalManager : approvalManagers){
                approvalManager.setApprovalManagerId(user.getId());
                userDao.editUser(approvalManager);
            }
        }
        
        for(Department department : departmentsRemoved){
            if(user.getDepartmentId() == department.getId()){
                logger.info("User is HOD and belong this dept. Cannot removed -- Dept id = "+department.getId());
                continue;
            }
            
            department.setHodId(0);
            departmentDao.updateDepartment(department);
            
            List<User> approvalManagers = userDao.getListApprovalManagerByDepartmentId(department.getId());
            for(User approvalManager : approvalManagers){
                approvalManager.setApprovalManagerId(0);
                userDao.editUser(approvalManager);
            }
        }
    }

    private void setAdminForDepartments(User user, List<Department> newDepartments) {
        List<Department> currentDepartments = departmentDao.getDepartmentListByAdminId(user.getId());

        List<Department> departmentsAddNew = getListDepartmentAddNew(currentDepartments, newDepartments);
        List<Department> departmentsRemoved = getListDepartmentHasBeenRemoved(currentDepartments, newDepartments);
        
        for(Department department : departmentsAddNew){
            if(userDao.getAdminByDepartmentId(department.getId()) != null){
                logger.info("This Dept have has Admin "+department.getName());
                continue;
            }
            department.setAdminId(user.getId());
            departmentDao.updateDepartment(department); 
        }
        
        for(Department department : departmentsRemoved){
            department.setAdminId(0);
            departmentDao.updateDepartment(department);
        }
    }

    private List<Department> getListDepartmentAddNew(List<Department> beforeDepts, List<Department> afterDepts) {
        List<Department> departmentsRtn = new ArrayList<>();

        for(Department afterDept : afterDepts){
            boolean isNew = true;
            for(Department beforeDept : beforeDepts){
                if(afterDept.getId() == beforeDept.getId()){
                    isNew = false;
                    break;
                }
            }
            if(isNew){
                departmentsRtn.add(afterDept);
            }
        }
        return departmentsRtn;
    }

    private List<Department> getListDepartmentHasBeenRemoved(List<Department> beforeDepts, List<Department> afterDepts) {
        List<Department> departmentsRtn = new ArrayList<>();

        for(Department beforeDept : beforeDepts){
            boolean isRemoved = true;
            for(Department afterDept : afterDepts){
                if(afterDept.getId() == beforeDept.getId()){
                    isRemoved = false;
                    break;
                }
            }
            if(isRemoved){
                departmentsRtn.add(beforeDept);
            }
        }
        return departmentsRtn;
    }

    @Override
    public ResponseEntity<?> getDepartmentByAdmin(int adminId) throws Exception {
        try {
            return Utils.generateSuccessResponseEntity(departmentDao.getDepartmentListByAdminId(adminId));
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateSuccessResponseEntity(new ArrayList<>());
        }
    }
    
    @Override
    public ResponseEntity<?> getAllDepartmentByHOD(int hodId) throws Exception {
        try {
            return Utils.generateSuccessResponseEntity(departmentDao.getDepartmentListByHodId(hodId));
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateSuccessResponseEntity(new ArrayList<>());
        }
    }
}