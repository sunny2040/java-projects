package com.surge.vms.exception;

class UserNotFoundException extends RuntimeException {

	UserNotFoundException(Long id) {
		super("Could not find user " + id);
	}

	UserNotFoundException(String UserFN, String UserLN, String userEmail, String userPwd) {
		super("Could not find user " + UserFN + UserLN + userEmail + userPwd);
	}

}
