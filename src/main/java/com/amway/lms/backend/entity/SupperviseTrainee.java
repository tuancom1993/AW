package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the user_roles database table.
 * 
 */
@Entity
@Table(name = "suppervise_trainee")
@NamedQuery(name = "findSupperviseTraineeBysupperviseId", query = "SELECT s.userId FROM SupperviseTrainee s where s.supperviseId=?")
public class SupperviseTrainee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name = "suppervise_id")
	private Integer supperviseId;

	@Column(name = "user_id")
	private Integer userId;

	public SupperviseTrainee() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getSupperviseId() {
		return supperviseId;
	}

	public void setSupperviseId(Integer supperviseId) {
		this.supperviseId = supperviseId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}