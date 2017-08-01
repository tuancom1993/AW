/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.ArrayList;
import java.util.List;

import com.amway.lms.backend.entity.Department;

/**
 * @author acton
 *
 */
public interface DepartmentRepository{

    /**
     * @param id
     * @return
     */
    public boolean exists(int id);

    public Department getDepartmentById(int id);

    public ArrayList<Department> getDepartmentList();
    
    public List<Department> getDepartmentListWithoutHod();
    
    public Department getDepartmentByName(String name);
    
    public void updateDepartment(Department department);

    public void removeHodInAllDepartmentByHodId(int hodId);
    
    public void removeAdminInAllDepartmentByAdmiId(int adminId);
    
    public List<Department> getDepartmentListByHodId(int hodId);
    
    public List<Department> getDepartmentListByHodIdOrWithoutHOD(int hodId);
    
    public List<Department> getDepartmentListByAdminIdOrWithoutAdmin(int adminId);
    
    public List<Department> getDepartmentListByAdminId(int adminId);


}
