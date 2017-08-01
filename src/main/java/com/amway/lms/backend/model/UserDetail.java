package com.amway.lms.backend.model;

import java.util.ArrayList;
import java.util.List;

public class UserDetail {
	private Employee employee;
	private List<Course> course = new ArrayList<>();

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Course> getCourse() {
		return course;
	}

	public void setCourse(List<Course> course) {
		this.course = course;
	}

}
