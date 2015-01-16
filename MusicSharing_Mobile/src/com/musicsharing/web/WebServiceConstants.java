package com.musicsharing.web;

public class WebServiceConstants {
	// old http://ec2-54-191-72-198.us-west-2.compute.amazonaws.com:8080/JainBooks/rest/jainbook-service/
	//http://ec2-54-68-4-201.us-west-2.compute.amazonaws.com:8080/JainBooks/rest/jainbook-service/	
	//http://ec2-54-68-180-12.us-west-2.compute.amazonaws.com:8080
	
	// Server IP
	//  private static final String BASE_URL="http://jainbooks.share2people.com:8080/JainBooks/rest/jainbook-service/";
	// Local IP
	  private static final String BASE_URL="http://ec2-54-69-167-78.us-west-2.compute.amazonaws.com:8080/MusicShare/rest/musicshare-service/";
	  public static final String MQTT_BROKER_URL="tcp://ec2-54-69-167-78.us-west-2.compute.amazonaws.com:1884";
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
	  
	  
	 
		
}
