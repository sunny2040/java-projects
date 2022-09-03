package com.sports.student.sportsstudent.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "name")
public class User {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String user_id;

	@Column
	public String password;

	@Column
	public Boolean is_admin;

	@Column

	public Timestamp created_dt;

	@Column
	public String created_by;

	@Column
	public Timestamp last_updated_dt;

	@Column
	public String last_updated_by;

	public User() {
		super();
	}

	public User(String user_id, String password, Boolean is_admin, Timestamp created_dt, String created_by,
			Timestamp last_updated_dt, String last_updated_by) {
		super();
		this.user_id = user_id;
		this.password = password;
		this.is_admin = is_admin;
		this.created_dt = created_dt;
		this.created_by = created_by;
		this.last_updated_dt = last_updated_dt;
		this.last_updated_by = last_updated_by;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(Boolean is_admin) {
		this.is_admin = is_admin;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(Timestamp created_dt) {
		this.created_dt = created_dt;
	}

	public Timestamp getLast_updated_dt() {
		return last_updated_dt;
	}

	public void setLast_updated_dt(Timestamp last_updated_dt) {
		this.last_updated_dt = last_updated_dt;
	}

	public String getLast_updated_by() {
		return last_updated_by;
	}

	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", password=" + password + ", is_admin=" + is_admin + ", created_dt="
				+ created_dt + ", created_by=" + created_by + ", last_updated_dt=" + last_updated_dt
				+ ", last_updated_by=" + last_updated_by + "]";
	}
}
