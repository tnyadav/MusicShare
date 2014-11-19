package com.musicsharing.connections;

import com.google.gson.annotations.Expose;

public class FriendLibrarySong {
	@Expose
	private Integer numberOfShares;
	@Expose
	private String name;
	@Expose
	private String fileName;

	/**
	*
	* @return
	* The numberOfShares
	*/
	public Integer getNumberOfShares() {
	return numberOfShares;
	}

	/**
	*
	* @param numberOfShares
	* The numberOfShares
	*/
	public void setNumberOfShares(Integer numberOfShares) {
	this.numberOfShares = numberOfShares;
	}

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
	* The fileName
	*/
	public String getFileName() {
	return fileName;
	}

	/**
	*
	* @param fileName
	* The fileName
	*/
	public void setFileName(String fileName) {
	this.fileName = fileName;
	}

}
