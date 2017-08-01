/**
 * 
 */
package com.amway.lms.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.exception.EditObjectException;
import com.amway.lms.backend.model.ManagerOfDepartment;
import com.amway.lms.backend.exception.ObjectNotFoundException;

/**
 * @author acton
 *
 */
public interface DepartmentService {
    public ResponseEntity<?> setDepartmentForUsers(Department department) throws Exception;

    public ResponseEntity<?> getDepartmentList();

    public ResponseEntity<?> getDepartmentsbyHodOrAdmin(int id) throws ObjectNotFoundException;

    public ResponseEntity<?> updateDepartment(Department department) throws EditObjectException;

    public ResponseEntity<?> manageManagerOfDepartment(int userId, List<Department> departmentIds) throws Exception;

    public ResponseEntity<?> getDepartmentByAdmin(int adminId) throws Exception;

    public ResponseEntity<?> getAllDepartmentByHOD(int hodId) throws Exception;
}
