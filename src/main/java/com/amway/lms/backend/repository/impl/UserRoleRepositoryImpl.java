/**
 * 
 */
package com.amway.lms.backend.repository.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amway.lms.backend.entity.UserRole;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.UserRoleRepository;

/**
 * @author acton
 *
 */
@Repository
@EnableTransactionManagement
@Transactional
public class UserRoleRepositoryImpl extends AbstractRepository<Integer, UserRole> implements UserRoleRepository{

    @Override
    public void addUserRole(UserRole userRole) {
        persist(userRole);
    }

    @Override
    public void deleteUserRole(UserRole userRole) {
        delete(userRole);
    }

    @Override
    public void editUserRole(UserRole userRole) {
        update(userRole);
        
    }

    @Override
    public List<UserRole> getListByUserId(int userId) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("userId", userId));
        return  criteria.list();
    }

    @Override
    public void deleteUserRolesByUserId(int userId) {
        String hql = "delete from UserRole where userId = :_userId";
        Query query = getSession().createQuery(hql);
        query.setInteger("_userId", userId);
        
        System.out.println(query.executeUpdate());
    }

    @Override
    public boolean exists(int id) {
        try {
            return getByKey(id) != null;
        } catch (Exception e) {
            return false;
        }
    }

}
