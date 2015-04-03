package com.musicsharing.web;

public class WebServiceConstants {

	
	// Server IP
	//  private static final String BASE_URL="http://jainbooks.share2people.com:8080/JainBooks/rest/jainbook-service/";
	// Local IP
	/*  private static final String BASE_URL="http://ec2-54-187-228-105.us-west-2.compute.amazonaws.com:8080/MusicShare/rest/musicshare-service/";
	  public static final String MQTT_BROKER_URL="tcp://ec2-54-187-228-105.us-west-2.compute.amazonaws.com:1883";*/
	
	  private static final String BASE_URL="http://ec2-52-74-50-6.ap-southeast-1.compute.amazonaws.com:8080/MusicShare/rest/musicshare-service/";
	  public static final String MQTT_BROKER_URL="tcp://ec2-52-74-50-6.ap-southeast-1.compute.amazonaws.com:1883";
	 
	  public static final String REGISTRATION=BASE_URL+"registerUser";
	  public static final String AUTHENTICATE=BASE_URL+"authenticate";
	  public static final String SEND_REGISTRATION_SMS= BASE_URL+"send-registration-sms?mobileNumber=";
	 
	  public static final String UPDATE_MY_STATUS= BASE_URL+"update-my-status?userId="; 
	  public static final String GET_ALL_USERS= BASE_URL+"get-users?userId=";
	  public static final String GET_USER_CONNECTIONS= BASE_URL+"get-user-connections?userId=";
	  public static final String GET_USER_LIBRARY= BASE_URL+"get-user-library?userId=";
	  public static final String ADD_TO_MY_LIBRARY= BASE_URL+"add-to-mylibrary";
	  
	  //post calls
	  public static final String ADD_CONNECTION= BASE_URL+"add-connection";
	  public static final String UPDATE_CONNECTION= BASE_URL+"update-connection-status";
	  public static final String FORGET_PASSWORD=BASE_URL+"forgot-password?mobileNumber=";
	  
	  
	 
		
}
