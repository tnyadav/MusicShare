package com.musicsharing.account;

import com.google.gson.annotations.Expose;

public class User {
	@Expose
	private String userId;
	@Expose
	private String name;

	

	
	public String getUserId() {
	return userId;
	}

	public void setUserId(String authToken) {
	this.userId = authToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	}

