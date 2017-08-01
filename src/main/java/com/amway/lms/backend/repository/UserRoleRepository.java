/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.List;

import com.amway.lms.backend.entity.UserRole;

/**
 * @author acton
 *
 */
public interface UserRoleRepository{
    public List<UserRole> getListByUserId(int userId);
    public void deleteUserRolesByUserId(int userId);
    public boolean exists(int id);
    void addUserRole(UserRole userRole);
    void deleteUserRole(UserRole userRole);
    void editUserRole(UserRole userRole);
}
