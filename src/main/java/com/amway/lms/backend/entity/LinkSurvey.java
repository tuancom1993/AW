package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the link_surveys database table.
 * 
 */
@Entity
@Table(name="link_surveys")
@NamedQuery(name="LinkSurvey.findAll", query="SELECT l FROM LinkSurvey l")
public class LinkSurvey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="http_link")
	private String httpLink;

	@Column(name="id_user")
	private int idUser;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	public LinkSurvey() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getHttpLink() {
		return this.httpLink;
	}

	public void setHttpLink(String httpLink) {
		this.httpLink = httpLink;
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}