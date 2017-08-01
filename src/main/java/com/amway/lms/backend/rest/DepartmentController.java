/**
 * 
 */
package com.amway.lms.backend.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.service.DepartmentService;

/**
 * @author acton
 *
 */
@RestController
@RequestMapping("/api/v1")
public class DepartmentController {
    @Autowired DepartmentService departmentService;
    
    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDepartment() throws SQLException, Exception{
        return departmentService.getDepartmentList();
    }
    
    @GetMapping(value="/departments/{hodOrAdminId}/hodOrAdmin")
    public ResponseEntity<?> getDepartmentsbyHodOrAdmin(@PathVariable("hodOrAdminId") int hodOrAdminId) throws SQLException, Exception{
        return departmentService.getDepartmentsbyHodOrAdmin(hodOrAdminId);
    }
    
    @PutMapping(value="/departments/setManagerForDept/{hodOrAdminId}")
    public ResponseEntity<?> setManagerForDept(@PathVariable("hodOrAdminId") int userId, 
            @RequestBody ArrayList<Department> departmentIds) throws Exception{
        return departmentService.manageManagerOfDepartment(userId, departmentIds);
    }
    
    @RequestMapping(value = "/departments/admin/{adminId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDepartmentByAdmin(@PathVariable("adminId") int adminId) throws SQLException, Exception{
        return departmentService.getDepartmentByAdmin(adminId);
    }
    
    @RequestMapping(value = "/departments/hod/{hodId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDepartmentByHOD(@PathVariable("hodId") int hodId) throws SQLException, Exception{
        return departmentService.getAllDepartmentByHOD(hodId);
    }
}
