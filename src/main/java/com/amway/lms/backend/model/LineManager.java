package com.amway.lms.backend.model;

import java.util.ArrayList;
import java.util.List;

public class LineManager {
	private Integer id;
	private String emailAdrress;
	private List<Employee> trannees = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmailAdrress() {
		return emailAdrress;
	}

	public void setEmailAdrress(String emailAdrress) {
		this.emailAdrress = emailAdrress;
	}

	public List<Employee> getTrannees() {
		return trannees;
	}

	public void setTrannees(List<Employee> trannees) {
		this.trannees = trannees;
	}
}
