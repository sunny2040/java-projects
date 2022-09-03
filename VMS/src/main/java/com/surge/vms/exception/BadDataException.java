package com.surge.vms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*the purpose of this class is to ,we use this exception in 
 * controller layer whenever we developed http ai 
 * then the employee not existing in the database , then REST API 
 * Wil thought Customer Exception*/

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BadDataException extends Exception {

	private static final long serialVersionUID = 1L;

	/*
	 * We will created a constructor here.We pass message in the constructor that
	 * help to show when we have exception
	 */
	public BadDataException(String message) {
		super(message);
	}
}