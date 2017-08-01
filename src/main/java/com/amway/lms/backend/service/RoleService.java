/**
 * 
 */
package com.amway.lms.backend.service;

import org.springframework.http.ResponseEntity;

import com.amway.lms.backend.entity.Role;

/**
 * @author acton
 *
 */
public interface RoleService{
    public ResponseEntity<?> setRoleForUsers(Role role);

    ResponseEntity<?> getRoleList();

    ResponseEntity<?> getRoleById(int id);
}
