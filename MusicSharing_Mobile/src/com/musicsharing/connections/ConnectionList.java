package com.musicsharing.connections;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class ConnectionList {
	@Expose
	private List<Connections> userDTO = new ArrayList<Connections>();

	/**
	*
	* @return
	* The userDTO
	*/
	public List<Connections> getUserDTO() {
	return userDTO;
	}

	/**
	*
	* @param userDTO
	* The userDTO
	*/
	public void setUserDTO(List<Connections> userDTO) {
	this.userDTO = userDTO;
	}

}
