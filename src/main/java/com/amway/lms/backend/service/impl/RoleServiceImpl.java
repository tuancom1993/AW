/**
 * 
 */
package com.amway.lms.backend.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.entity.Role;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.entity.UserRole;
import com.amway.lms.backend.repository.RoleRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.repository.UserRoleRepository;
import com.amway.lms.backend.service.RoleService;

/**
 * @author acton
 * @email acton@enclave.vn
 */
@Service
@Transactional
@EnableTransactionManagement
public class RoleServiceImpl implements RoleService {
    
    private static final Logger logger = LoggerFactory
            .getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRoleRepository userRoleDao;
    
    @Autowired RoleRepository roleDao;
    
    @Autowired UserRepository userDao;
    /* (non-Javadoc)
     * @see com.amway.lms.backend.service.EntityService#getList()
     */
    @Override
    public ResponseEntity<?> getRoleList() {
        try {
            return new ResponseEntity<List<Role>>(roleDao.getRoleList(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setRoles " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    /* (non-Javadoc)
     * @see com.amway.lms.backend.service.EntityService#getEntityById(int)
     */
    @Override
    public ResponseEntity<?> getRoleById(int id) {
        try {
            return new ResponseEntity<Role>(roleDao.getRoleById(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - getRoleById " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /* (non-Javadoc)
     * @see com.amway.lms.backend.service.RoleService#setRoleForUsers(java.util.List)
     */
    @Override
    public ResponseEntity<?> setRoleForUsers(Role role) {
        try {
            if(!roleDao.exists(role.getId()))
                throw new Exception("Role "+role.getId()+" doesn't exixts");
            for(User user : role.getUsers()){
                if(!userDao.exists(user.getId()))
                    throw new Exception("User "+user.getId()+" doesn't exixts");
            }
            for(User user : role.getUsers()){
                userRoleDao.deleteUserRolesByUserId(user.getId());
                UserRole userRole = new UserRole();
                userRole.setRoleId(role.getId());
                userRole.setUserId(user.getId());
                userRoleDao.addUserRole(userRole);
            }
            return new ResponseEntity<>(role, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EXCEPTION - setRoleForUser " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
 