package com.amway.lms.backend.service.impl;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amway.lms.backend.common.AmwayEnum.Roles;
import com.amway.lms.backend.common.Utils;
import com.amway.lms.backend.configuration.CustomUserDetail;
import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.entity.Department;
import com.amway.lms.backend.entity.Location;
import com.amway.lms.backend.entity.PreparatoryParticipant;
import com.amway.lms.backend.entity.Room;
import com.amway.lms.backend.entity.Session;
import com.amway.lms.backend.entity.User;
import com.amway.lms.backend.exception.ObjectNotFoundException;
import com.amway.lms.backend.model.Employee;
import com.amway.lms.backend.model.SessionModel;
import com.amway.lms.backend.repository.CourseRepository;
import com.amway.lms.backend.repository.DepartmentRepository;
import com.amway.lms.backend.repository.LocationRepository;
import com.amway.lms.backend.repository.PostTrainingSurveyParticipantRepository;
import com.amway.lms.backend.repository.PreparatoryParticipantRepository;
import com.amway.lms.backend.repository.RoomRepository;
import com.amway.lms.backend.repository.SessionParticipantRepository;
import com.amway.lms.backend.repository.SessionRepository;
import com.amway.lms.backend.repository.UserRepository;
import com.amway.lms.backend.service.ParticipantService;

@Service
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SessionParticipantRepository sessionParticipantRepository;

    @Autowired
    private PreparatoryParticipantRepository preparatoryParticipantRepository;

    @Autowired 
    private PostTrainingSurveyParticipantRepository postTrainingSurveyParticipantRepository;
    
    @Override
    public ResponseEntity<?> getInforCourseAndSession(Integer sessionId) {
        Timestamp today = new Timestamp(System.currentTimeMillis());
        Session session = sessionRepository.getSessionById(sessionId);
        Course courseEntity = courseRepository.getCourseById(session.getCourseId());
        Room room = roomRepository.getRoomById(session.getRoomId());
        Location location = locationRepository.getLocationById(session.getLocationId());
        SessionModel sessionModel = new SessionModel();
        sessionModel.setCourseName(courseEntity.getName());
        sessionModel.setSessionName(session.getName());
        sessionModel.setLocationName(location != null ? location.getName() : "Waiting...");
        sessionModel.setDate(Utils.convertDateToStringTrainingAction(session.getStartTime()));
        sessionModel.setStart(Utils.getTimeByDate(session.getStartTime()));
        sessionModel.setEnd(Utils.getTimeByDate(session.getEndTime()));
        sessionModel.setStartTimeStr(DateFormatUtils.format(session.getStartTime(), "dd-MMM-yyyy hh:mm aa"));
        sessionModel.setEndTimeStr(DateFormatUtils.format(session.getEndTime(), "dd-MMM-yyyy hh:mm aa"));
        sessionModel.setTrainerFullName(session.getTrainerFullName());
        if(session.getStartTime().after(today)) {
            sessionModel.setStatus("Not Started");
        }else if(session.getEndTime().after(today) && session.getStartTime().before(today)) {
            sessionModel.setStatus("In Progress");
        }else {
            sessionModel.setStatus("Completed");
        }
        sessionModel.setRoomName(room != null ? room.getName() : "Waiting...");
        return Utils.generateSuccessResponseEntity(sessionModel);
    }

    @Override
    public ResponseEntity<?> getEmployee(Integer sessionId, Integer departmentId) {
        try {
            List<Employee> employees = new LinkedList<>();
            Session session = sessionRepository.getSessionById(sessionId);
            
            User userLogin = getUserLogin();
            if(userLogin == null)
                throw new ObjectNotFoundException("UserLogin is null");
            
            if (departmentId == 0) {
                List<User> users = null;

                if(userLogin.getRoleId() == Roles.ADMIN.getIntValue())
                    users = userRepository.getListUserAddParticipantBySessionIdAndAdminId(sessionId, userLogin.getId());
                else
                    users = userRepository.getListUserAddParticipant(sessionId);
                
                for (User user : users) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(user.getId());
                    employee.setFirstName(user.getFirstName());
                    employee.setIsTrainneePlan(1);
                    employee.setFromTraineePlan("No");
                    employee.setSessionId(sessionId);
                    employee.setLastName(user.getLastName());
                    employee.setStaffCode(user.getStaffCode());
                    Department department = departmentRepository.getDepartmentById(user.getDepartmentId());
                    employee.setDepartment(department != null ? department.getName() : "");
                    employee.setJobTitle(user.getJobTitle());
                    employee.setEmailAddress(user.getEmail());
                    employee.setApprovalManagerId(user.getApprovalManagerId());
                    employee.setFullName(
                            new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).toString());
                    PreparatoryParticipant preparatoryParticipant = preparatoryParticipantRepository
                            .getPreparatoryParticipantByCourseIdAndUserId(session.getCourseId(), employee.getEmployeeId(),
                                    session.getId());
                    if (preparatoryParticipant != null) {
                        employee.setFullNameManager(preparatoryParticipant.getRequestedManagerName());
                    }
                    employees.add(employee);
                }
                
            } else {
                List<User> users = userRepository.getListUserAddParticipantByDepartmentId(sessionId, departmentId);

                for (User user : users) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(user.getId());
                    employee.setFirstName(user.getFirstName());
                    employee.setLastName(user.getLastName());
                    employee.setIsTrainneePlan(1);
                    employee.setStaffCode(user.getStaffCode());
                    Department department = departmentRepository.getDepartmentById(user.getDepartmentId());
                    employee.setDepartment(department != null ? department.getName() : "");
                    employee.setJobTitle(employee.getJobTitle());
                    employee.setApprovalManagerId(user.getApprovalManagerId());
                    employee.setFullName(
                            new StringBuilder(user.getLastName()).append(" ").append(user.getLastName()).toString());
                    PreparatoryParticipant preparatoryParticipant = preparatoryParticipantRepository
                            .getPreparatoryParticipantByCourseIdAndUserId(session.getCourseId(), employee.getEmployeeId(),
                                    session.getId());
                    if (preparatoryParticipant != null) {
                        employee.setFullNameManager(preparatoryParticipant.getRequestedManagerName());
                    }
                    employees.add(employee);
                }
            }
            return Utils.generateSuccessResponseEntity(employees);
        } catch (Exception e) {
            e.printStackTrace();
            return Utils.generateSuccessResponseEntity(new LinkedList<Employee>());
        }
    }

    @Override
    public ResponseEntity<?> getSubmitedList(Integer sessionId) {
        List<User> users = userRepository.getSubmmittedListUser(sessionId);
        List<Employee> employees = new LinkedList<>();
        for (User user : users) {
            Employee employee = new Employee();
            employee.setEmployeeId(user.getId());
            employee.setFirstName(user.getFirstName());
            employee.setLastName(user.getLastName());
            employee.setIsTrainneePlan(1);
            employee.setStaffCode(user.getStaffCode());
            Department department = departmentRepository.getDepartmentById(user.getDepartmentId());
            employee.setDepartment(department != null ? department.getName() : "");
            employee.setJobTitle(user.getJobTitle());
            employee.setFullName(
                    new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).toString());
            if (user.getApprovalManagerId() != 0) {
                User mananger = userRepository.getUserById(user.getApprovalManagerId());
                employee.setFullNameManager(new StringBuilder(mananger.getLastName()).append(" ")
                        .append(mananger.getLastName()).toString());
            }
            employees.add(employee);
        }
        return Utils.generateSuccessResponseEntity(employees);
    }

    @Override
    public ResponseEntity<?> newParticipant(List<Employee> employees) {
        for (Employee employee : employees) {
            sessionParticipantRepository.addSessionParticipantFromSession(employee.getSessionId(), employee.getEmployeeId());
        }
        return Utils.generateSuccessResponseEntity(employees);
    }
    
    private User getUserLogin(){
        User userLogin = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            CustomUserDetail customUserDetails = (CustomUserDetail) principal;
            userLogin = customUserDetails.getUser();
        }
        return userLogin;
    }

}