package com.amway.lms.backend.model;

import java.util.ArrayList;
import java.util.List;

public class EmailModel {

	List<LineManager> managers = new ArrayList<>();

	public List<LineManager> getManagers() {
		return managers;
	}

	public void setManagers(List<LineManager> managers) {
		this.managers = managers;
	}

}
