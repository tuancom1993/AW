package com.amway.lms.backend.common;

import java.util.Comparator;

import com.amway.lms.backend.entity.Course;
import com.amway.lms.backend.model.EventCourseTableView;

public class ComparatorUtils {
    
    public static class CourseComparator implements Comparator<Course>{
        @Override
        public int compare(Course o1, Course o2) {
            return o2.getId() - o1.getId();
        }
    }
    
    public static Comparator<Course> COURSE_COMPARATOR = new Comparator<Course>() {
        @Override
        public int compare(Course o1, Course o2) {
             return o2.getId() - o1.getId();
        }
    };
    
    public static Comparator<com.amway.lms.backend.model.Course> COURSE_MODEL_COMPARATOR = new Comparator<com.amway.lms.backend.model.Course>() {
        @Override
        public int compare(com.amway.lms.backend.model.Course o1, com.amway.lms.backend.model.Course o2) {
            return o2.getCourseId() - o1.getCourseId();
        }
    };
    
    public static Comparator<EventCourseTableView> EVENT_COURSE_TABLE_VIEW_COMPARATOR = new Comparator<EventCourseTableView>() {
        @Override
        public int compare(EventCourseTableView o1, EventCourseTableView o2) {
            return o2.getSessionId() - o1.getSessionId();
        }
    };
}
