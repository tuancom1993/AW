package com.amway.lms.backend.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.repository.AbstractRepository;
import com.amway.lms.backend.repository.CourseRepository;

@Repository
@SuppressWarnings("unchecked")
public class CourseRepositoryImpl extends AbstractRepository<Integer, Course>
        implements CourseRepository {

    private static final Logger logger = LoggerFactory
            .getLogger(CourseRepositoryImpl.class);

    @Override
    public List<Course> getCoursesList(int isActive, Integer trainingPlanId) {
        Query query;
        if (trainingPlanId == null || trainingPlanId == -1){
            query = createNamedQuery("getCoursesList", -1, -1, isActive);
        } else {
            query = createNamedQuery("getCoursesByTrainingPlan", -1, -1, isActive, trainingPlanId);
        }
        return (List<Course>) query.list();
    }
    
    
    @Override
    public List<Course> getCoursesListByStatus(int firstItem, int maxItem, int isActive) {
        Query query = createNamedQuery("getCoursesListByStatus", firstItem, maxItem, isActive);
        return (List<Course>) query.list();
    }

    @Override
    public void addCourse(Course course) {
        persist(course);
        logger.info("Course saved successfully, Course Details=" + course);
    }

    @Override
    public void editCourse(Course course) {
        update(course);
        logger.info("Course saved successfully, Course Details=" + course);
    }

    @Override
    public Course getCourseById(int id) {
        logger.info("get Course By Id: id = " + id);
        return getByKey(id);
    }

    @Override
    public void updateCoursesStatus(Course course) {
        Query query = createNamedQuery("updateCoursesStatus", -1, -1,
                course.getIsActive(), course.getId());
        query.executeUpdate();
    }

    @Override
    public List<Course> getCoursesByNameOrCode(String searchString, int isActive) {
        searchString = "%" + searchString + "%";
        Query query = createNamedQuery("getCoursesByNameOrCode", -1, -1, isActive, searchString);
        return (List<Course>) query.list();
    }

    @Override
    public List<Course> getCoursesByUser(String searchString) {
        String queryString = "select * from courses where id in "
                + "(select cp.course_id from course_participants cp "
                + "left join users u ON u.id = cp.user_id where  u.first_name like :searchString "
                + "or u.last_name like :searchString or u.staff_code like :searchString)";
        SQLQuery query = getSession().createSQLQuery(queryString);

        query.setParameter("searchString", "%" + searchString + "%");
        query.addEntity(Course.class);
        return (List<Course>) query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> getCoursesApprovedList(int firstItem, int maxItem) {
        Query query = createNamedQuery("getCoursesApprovedList", firstItem,
                maxItem);
        return (List<Course>) query.list();
    }

    

	@Override
	public List<Course> getCoursesImprogressList(int firstItem, int maxItem) {
		String sql = "SELECT c.* FROM courses c "
				+ "LEFT JOIN sessions s "
				+ "ON c.id=s.course_id "
				+ "WHERE NOW() BETWEEN s.start_time AND s.end_time "
				+ "LIMIT " + firstItem + ", " + maxItem;
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(Course.class);
		return (List<Course>) query.list();
	}


	@Override
	public Object[] getCourseInformation(int courseId) {
		Query query = createNamedQuery("getCourseInformation", -1, -1, courseId);
		return (Object[]) query.uniqueResult();
	}

	@Override
	public List<Course> searchCourseByStaffCode(int userId) {
		Query query = createNamedQuery("searchCourseByStaffCode", -1, -1, userId);
		return query.list();
	}

	@Override
	public List<Course> getSuggestCourse(int userId) {
		Query query = createNamedQuery("getSuggestCourse", -1, -1, userId);
		return query.list();
	}

	@Override
	public void updateCourse(Course course) {
        update(course);
	}

    @Override
    public List<Course> getCoursesToAddTrainingPlan(int trainingPlanId) {
        Query query = createNamedQuery("getCoursesToAddTrainingPlan", -1, -1, trainingPlanId);
        return query.list();
    }
    
    @Override
    public List<Course> getCourseListNotHaveQuiz() {
        Query query = createNamedQuery("getCourseListNotHaveQuiz", -1, -1);
        return query.list();
    }

    @Override
    public List<Course> getCoursesOfTrainingPlan(int trainingPlanId) {
        Query query = createNamedQuery("getCoursesOfTrainingPlan", -1, -1, trainingPlanId);
        return query.list();
    }
    
    @Override
    public List<Course> getCoursesStartByToday(String courseName) {
        Query query;
        if (courseName == null){
            query = createNamedQuery("getCoursesStartByToday", -1, -1);
        } else {
            courseName = "%" + courseName + "%";
            logger.debug("CourseRepositoryImpl************getCoursesStartByToday: courseName=" + courseName);
            query = createNamedQuery("searchCoursesByNameStartByToday", -1, -1, courseName);
        }
        return query.list();
    }


    @Override
    public List<Course> getCourseListForPostTrainingSurvey() {
        Query query = createNamedQuery("getCourseListForPostTrainingSurvey", -1, -1);
        return query.list();
    }
}
