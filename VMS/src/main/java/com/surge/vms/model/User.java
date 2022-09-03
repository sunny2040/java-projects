package com.surge.vms.model;

public class User {
	
	private Profile profile;
	

	private Credentials credentials;
	
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredential(Credentials credentials) {
		this.credentials = credentials;
	}


}
