/**
 * 
 */
package com.amway.lms.backend.repository;

import java.util.ArrayList;
import java.util.List;

import com.amway.lms.backend.entity.Role;

/**
 * @author acton
 *
 */
public interface RoleRepository{

    /**
     * @param id
     * @return
     */
    public boolean exists(int id);
    public List<Role> getListRoleByUserId(int userId);
    ArrayList<Role> getRoleList();
    void addRole(Role role);
    void editEntity(Role entity);
    Role getRoleById(int id);

}
