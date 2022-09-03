package com.surge.vms.model;

import java.time.LocalDateTime;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tb_users")
public class UserDomain {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "Please provide a UserId")
	private String userId;

	@NotEmpty(message = "Please provide a UserFN")
	private String userFn;

	@NotEmpty(message = "Please provide a UserLN")
	private String userLn;

	@NotEmpty(message = "Please provide an Email")
	private String userEmail;
	
	
	

	@NotEmpty(message = "Please provide a Password")
	private String userPwd;
	
	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;


	public UserDomain() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFn() {
		return userFn;
	}

	public void setUserFn(String userFn) {
		this.userFn = userFn;
	}

	public String getUserLn() {
		return userLn;
	}

	public void setUserLn(String userLn) {
		this.userLn = userLn;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public UserDomain( @NotEmpty(message = "Please provide a UserId") String userId,

			@NotEmpty(message = "Please provide a UserFN") String userFn,

			@NotEmpty(message = "Please provide a UserLN") String userLn,

			@NotEmpty(message = "Please provide an Email") String userEmail, 


			@NotEmpty(message = "Please provide a Password") String userPwd) {
		super();

		this.userId = userId;
		this.userFn = userFn;
		this.userLn = userLn;
		this.userEmail = userEmail;

		this.userPwd = userPwd;
	}

	public UserDomain orElseThrow(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}