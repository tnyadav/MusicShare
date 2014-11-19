package com.musicsharing.connections;

import com.google.gson.annotations.Expose;

public class Connections  {
	@Expose
	private String name;
	@Expose
	private String userStatus;
	@Expose
	private String connectionStatus;
	@Expose
	private String userId;
	@Expose
	private Boolean pendingForMyApproval;
	@Expose
	private String userMobileNumber;

	/**
	*
	* @return
	* The name
	*/
	public String getName() {
	return name;
	}

	/**
	*
	* @param name
	* The name
	*/
	public void setName(String name) {
	this.name = name;
	}

	/**
	*
	* @return
	* The userStatus
	*/
	public String getUserStatus() {
	return userStatus;
	}

	/**
	*
	* @param userStatus
	* The userStatus
	*/
	public void setUserStatus(String userStatus) {
	this.userStatus = userStatus;
	}

	/**
	*
	* @return
	* The connectionStatus
	*/
	public String getConnectionStatus() {
	return connectionStatus;
	}

	/**
	*
	* @param connectionStatus
	* The connectionStatus
	*/
	public void setConnectionStatus(String connectionStatus) {
	this.connectionStatus = connectionStatus;
	}

	/**
	*
	* @return
	* The userId
	*/
	public String getUserId() {
	return userId;
	}

	/**
	*
	* @param userId
	* The userId
	*/
	public void setUserId(String userId) {
	this.userId = userId;
	}

	/**
	*
	* @return
	* The pendingForMyApproval
	*/
	public Boolean getPendingForMyApproval() {
	return pendingForMyApproval;
	}

	/**
	*
	* @param pendingForMyApproval
	* The pendingForMyApproval
	*/
	public void setPendingForMyApproval(Boolean pendingForMyApproval) {
	this.pendingForMyApproval = pendingForMyApproval;
	}

	/**
	*
	* @return
	* The userMobileNumber
	*/
	public String getUserMobileNumber() {
	return userMobileNumber;
	}

	/**
	*
	* @param userMobileNumber
	* The userMobileNumber
	*/
	public void setUserMobileNumber(String userMobileNumber) {
	this.userMobileNumber = userMobileNumber;
	}
	
}
