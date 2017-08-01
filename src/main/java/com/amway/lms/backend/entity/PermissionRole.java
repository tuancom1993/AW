package com.amway.lms.backend.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the permission_role database table.
 * 
 */
@Entity
@Table(name="permission_role")
@NamedQuery(name="PermissionRole.findAll", query="SELECT p FROM PermissionRole p")
public class PermissionRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="permission_id")
	private int permissionId;

	@Column(name="role_id")
	private int roleId;

	public PermissionRole() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPermissionId() {
		return this.permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}