package com.amway.lms.backend.service.ldap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.common.Common;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.dao.ldap.LdapUserDao;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.model.ldap.LdapUser;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.UserRepository;

@Service("ldapUserService")
@Transactional
@EnableScheduling
public class LdapUserServiceImpl implements LdapUserService {

    private static final Logger logger = LoggerFactory.getLogger(LdapUserServiceImpl.class);

    @Autowired
    LdapUserDao<LdapUser> ldapUserDao;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    DepartmentRepository departmentRepository;
    
    @Value("${default.job.title}")
    private String DEFAULT_JOB_TITLE;

    @Override
    public List<LdapUser> getAllUsers() {
        return ldapUserDao.getAllUsers();
    }

    @Override
    public void syncUserData() {
        // TODO
        // Code to sync data goes here

        List<User> users = userRepository.getUserList();
        List<LdapUser> ldapUsers = getAllUsers();
        Set<Integer> listUserIdRemoveAM = new HashSet<>();

        //check update or delete 
        for (User user : users) {
            boolean isDeleted = true;
            for (LdapUser ldapUser : ldapUsers) {
                if (user.getUserID().equals(ldapUser.getUserID())) {
                    // is updateours
                    user.setFirstName(ldapUser.getFirstName());
                    user.setLastName(ldapUser.getLastName());
                    user.setUpdatedAt(Utils.getCurrentTime());
                    user.setEmail(ldapUser.getMail().trim());
                    user.setJobTitle(ldapUser.getTitle() == null ? DEFAULT_JOB_TITLE : ldapUser.getTitle());
                    user.setPhoneNumber(ldapUser.getTelephonNumber());
                    user.setOfficeLocation(ldapUser.getOfficeLocation());
                    
                    String managerId = ldapUser.getManagerUser();
                    User manager = getUserFromListByUserID(users, managerId);
                    user.setManagerId(manager == null ? 0 : manager.getId());
                    
                    //This code will be lock till deploy the real LDAP have 2 properties are "department" and "userAccountControl"
                    /*user.setActive(ldapUser.getUserAccountControl() == 514 ? (byte) 0 : (byte) 1);*/
                    Department department = departmentRepository.getDepartmentByName(ldapUser.getDepartment());
                    checkChangeDepartment(user, department, listUserIdRemoveAM);
                    
                    userRepository.editUser(user);
                    
                    isDeleted = false;
                    break;
                }
            }
            if (isDeleted) {
                user.setIsDeleted((byte) 1);
                user.setActive((byte) 0);
                userRepository.editUser(user);
            }
        }

        //check new user
        for (LdapUser ldapUser : ldapUsers) {
            boolean isNew = true;
            for(User user : users){
                if(ldapUser.getUserID().equals(user.getUserID())){
                    isNew = false;
                    break;
                }
            }
            if(isNew){
                logger.debug("Is new User userID = "+ldapUser.getUserID());
                User user = new User();
                user.setUserID(ldapUser.getUserID());
                user.setStaffCode(ldapUser.getUserID());
                user.setFirstName(ldapUser.getFirstName());
                user.setLastName(ldapUser.getLastName());
                user.setCreatedAt(Utils.getCurrentTime());
                user.setUpdatedAt(Utils.getCurrentTime());
                user.setEmail(ldapUser.getMail().trim());
                user.setJobTitle(ldapUser.getTitle() == null ? DEFAULT_JOB_TITLE : ldapUser.getTitle());
                user.setPhoneNumber(ldapUser.getTelephonNumber());
                user.setActive((byte) 1);
                user.setIsDeleted((byte) 0);
                user.setOfficeLocation(ldapUser.getOfficeLocation());
                user.setRoleId(4);
                user.setApprovalManagerId(0);;
                
                String managerId = ldapUser.getManagerUser();
                User manager = getUserFromListByUserID(users, managerId);
                user.setManagerId(manager == null ? 0 : manager.getId());

                //This code will be lock till deploy the real LDAP have 2 properties are "department" and "userAccountControl"
                Department department = departmentRepository.getDepartmentByName(ldapUser.getDepartment());
                user.setDepartmentId(department == null ? 0 : department.getId());
                
                /*user.setActive(ldapUser.getUserAccountControl() == 514 ? (byte) 0 : (byte) 1);*/
                
                userRepository.addUser(user);
                logger.debug("Is new User id = "+user.getId());
            }
        }
        
        //Remove AprovalManager out User
        for(Integer userId : listUserIdRemoveAM){
            if(userId == null) continue;
            User userRemoveAMId = userRepository.getUserById(userId);
            userRemoveAMId.setApprovalManagerId(0);
            userRepository.editUser(userRemoveAMId);
        }
    }

    private void checkChangeDepartment(User user, Department department, Set<Integer> listUserIdRemoveAM) {
        if (department == null) 
            department = Common.getEmptyDepartment();
        
        Department departmentBefore = departmentRepository.getDepartmentById(user.getDepartmentId());
        if (departmentBefore == null) {
            logger.error("This user don't have Department");
            return;
        }

        if(department.getId() == departmentBefore.getId()){
            logger.info("User not change Department "+user.getId());
        } else {
            logger.info("---checkChangeDepartment(): User" + user.getUserID() 
                +" change Department (From "+departmentBefore.getName()+ " To "+department.getName()+")");
            int roleBefore = user.getRoleId();

            user.setDepartmentId(department.getId());
            user.setRoleId(Roles.TRAINEE.getIntValue());
            
            if(roleBefore == Roles.HOD.getIntValue()){
                List<Department> departmentsBeforeUserManage = departmentRepository.getDepartmentListByHodId(user.getId());
                for(Department departmentBeforeUserManage : departmentsBeforeUserManage){
                    departmentBeforeUserManage.setHodId(0);
                    departmentRepository.updateDepartment(departmentBeforeUserManage);

                    List<User> approvalManagers = userRepository.getListApprovalManagerByDepartmentId(departmentBeforeUserManage.getId());
                    for (User approvalManager : approvalManagers) {
                        listUserIdRemoveAM.add(approvalManager.getId());
                    }
                }
            } else if (roleBefore == Roles.APPROVAL_MANAGER.getIntValue()){
                List<User> usersApproveByAM = userRepository.getListUserByApprovalMangerId(user.getId());
                for (User employee : usersApproveByAM) {
                    listUserIdRemoveAM.add(employee.getId());
                }
            } else if (roleBefore == Roles.ADMIN.getIntValue()) {
                List<Department> departmentsBeforeUserManage = departmentRepository.getDepartmentListByAdminId(user.getId());
                for(Department departmentBeforeUserManage : departmentsBeforeUserManage){
                    departmentBeforeUserManage.setAdminId(0);
                    departmentRepository.updateDepartment(departmentBeforeUserManage);
                }
            }
        }
    }

    private User getUserFromListByUserID(List<User> users, String userID){
        for(User user : users){
            if(user.getUserID().equals(userID))
                return user;
        }
        return null;
    }
    
    @EventListener
    @Override
    public void runCronjobOnStartUp(ContextRefreshedEvent event){
        logger.info("Run cronJob LDAP when start up....!");
        syncUserData();
    }
}
