package com.musicsharing.account;

import com.google.gson.annotations.Expose;

public class Login {
	
    @Expose
	private String userId;
    @Expose
	private String statusCode;
	@Expose
	private String message;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



}
