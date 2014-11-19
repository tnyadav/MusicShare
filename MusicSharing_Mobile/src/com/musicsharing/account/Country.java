package com.musicsharing.account;

import com.google.gson.annotations.Expose;

public class Country {

   	
	
	@Expose
	private String name;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Expose
	private String dial_code;
	@Expose
	private String code;
	public Country(String name, String dial_code) {
		super();
		this.name = name;
		this.dial_code = dial_code;
	}

	public String getName() {
	return name;
	}

	public void setName(String name) {
	this.name = name;
	}

	public String getDial_code() {
	return dial_code;
	}

	public void setDial_code(String dial_code) {
	this.dial_code = dial_code;
	}

	@Override
	public String toString() {
		return "("+dial_code + ") " + name ;
	}

	
}
