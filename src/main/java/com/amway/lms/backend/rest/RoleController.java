/**
 * 
 */
package com.amway.lms.backend.rest;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amway.lms.backend.entity.Role;
import com.amway.lms.backend.service.RoleService;

/**
 * @author acton
 *
 */
@RestController
@RequestMapping("/api/v1")
public class RoleController {
    
    @Autowired 
    RoleService roleService;
    
    @RequestMapping("/roles")
    public ResponseEntity<?> getAllRoles() throws SQLException, Exception{
        return roleService.getRoleList();
    }
    
    @RequestMapping("/roles/{roleId}/users")
    public ResponseEntity<?> setRoleForUsers(@PathVariable("roleId") int roleId
            , @RequestBody Role role) throws SQLException, Exception{
        role.setId(roleId);
        return roleService.setRoleForUsers(role);
    }
}
