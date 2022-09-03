package com.surge.vms.model;

import lombok.Data;

/**
 * @author Joey
 * @desc Mail request class
 */

/*
 * Use lombok for getter and setter replacement
 */

@Data
public class MailRequest {
	private String name;
	private String to;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	private String from;
	private String subject;
}
