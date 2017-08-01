/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Role;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.RoleRepository;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Repository()
public class RoleRepositoryImpl extends AbstractRepository<Integer, Role> implements RoleRepository {

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#getList()
     */
    @Override
    public ArrayList<Role> getRoleList() {
        String queryString = "from Role";
        Query query = getSession().createQuery(queryString);
        return (ArrayList<Role>) query.list();
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#addEntity(java.lang.Object)
     */
    @Override
    public void addRole(Role role) {
        persist(role);
        
    }

   
    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#editEntity(java.lang.Object)
     */
    @Override
    public void editEntity(Role entity) {
        getSession().saveOrUpdate(entity);
        
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.EntityDao#getEntityById(int)
     */
    @Override
    public Role getRoleById(int id) {
        return getByKey(id);
    }
    
    @Override
    public boolean exists(int id) {
        try {
            Criteria criteria = createEntityCriteria();
            criteria.add(Restrictions.eq("id", id));
            return criteria.uniqueResult() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.dao.RoleDao#getListRoleByUserId(int)
     */
    @Override
    public List<Role> getListRoleByUserId(int userId) {
        String sql = "Select * from roles r inner join user_roles ur "
                + "on r.id = ur.role_id "
                + "where ur.user_id = :userId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.addEntity(Role.class);
        return query.list();
    }
}
