/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.DepartmentRepository;

/**
 * @author acton
 *
 */
@Repository
public class DepartmentRepositoryImpl extends AbstractRepository<Integer, Department> implements DepartmentRepository{

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#getList()
     */
    @Override
    public ArrayList<Department> getDepartmentList() {
        String queryString = "from Department";
        Query query = getSession().createQuery(queryString);
        return (ArrayList<Department>) query.list();
    }
    
    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#getEntityById(int)
     */
    @Override
    public Department getDepartmentById(int id) {
        return getByKey(id);
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
    public Department getDepartmentByName(String name) {
        Query query = createNamedQuery("getDepartmentByName", 0, 1, name);
        return (Department) query.uniqueResult();
    }


    @Override
    public void updateDepartment(Department department) {
        update(department);
    }


    @Override
    public void removeHodInAllDepartmentByHodId(int hodId) {
        Query query = createNamedQuery("removeHodInAllDepartmentByHodId", 0, 1, hodId);
        query.executeUpdate();
    }


    @Override
    public void removeAdminInAllDepartmentByAdmiId(int adminId) {
        Query query = createNamedQuery("removeAdminInAllDepartmentByAdmiId", 0, 1, adminId);
        query.executeUpdate();
    }


    @Override
    public List<Department> getDepartmentListByHodId(int hodId) {
        Query query = createNamedQuery("getDepartmentListByHodId", -1, -1, hodId);
        return query.list();
    }
    
    @Override
    public List<Department> getDepartmentListByHodIdOrWithoutHOD(int hodId) {
        Query query = createNamedQuery("getDepartmentListByHodIdOrWithoutHOD", -1, -1, hodId);
        return query.list();
    }
    
    @Override
    public List<Department> getDepartmentListByAdminIdOrWithoutAdmin(int adminId) {
        Query query = createNamedQuery("getDepartmentListByAdminIdOrWithoutAdmin", -1, -1, adminId);
        return query.list();
    }
    
    @Override
    public List<Department> getDepartmentListWithoutHod(){
        Query query = createNamedQuery("getDepartmentListWithoutHod",-1,-1);
        return query.list();
    }

    @Override
    public List<Department> getDepartmentListByAdminId(int adminId) {
        Query query = createNamedQuery("getDepartmentListByAdminId", -1, -1, adminId);
        return query.list();
    }
}
