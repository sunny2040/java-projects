package com.surge.vms.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity

public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long contactId;

	@Column
	private String primaryContName;

	@Column
	private String secContName;
	
	@Column
	private String primaryContEmail;

	@Column
	private String secContEmail;
	
	@Column
	private String primaryContPhone;

	@Column
	private String secContPhone;
	
	@Column
	private Long contactOwnerId;
	
	
		
    @CreationTimestamp
    private LocalDateTime createDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

	

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(LocalDateTime updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getPrimaryContName() {
		return primaryContName;
	}

	public void setPrimaryContName(String primaryContName) {
		this.primaryContName = primaryContName;
	}

	public String getSecContName() {
		return secContName;
	}

	public void setSecContName(String secContName) {
		this.secContName = secContName;
	}

	public String getPrimaryContEmail() {
		return primaryContEmail;
	}

	public void setPrimaryContEmail(String primaryContEmail) {
		this.primaryContEmail = primaryContEmail;
	}

	public String getSecContEmail() {
		return secContEmail;
	}

	public void setSecContEmail(String secContEmail) {
		this.secContEmail = secContEmail;
	}

	public String getPrimaryContPhone() {
		return primaryContPhone;
	}

	public void setPrimaryContPhone(String primaryContPhone) {
		this.primaryContPhone = primaryContPhone;
	}

	public String getSecContPhone() {
		return secContPhone;
	}

	public void setSecContPhone(String secContPhone) {
		this.secContPhone = secContPhone;
	}

	public Long getContactOwnerId() {
		return contactOwnerId;
	}

	public void setContactOwnerId(Long contactOwnerId) {
		this.contactOwnerId = contactOwnerId;
	}



}