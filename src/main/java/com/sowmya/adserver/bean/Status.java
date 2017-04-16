package com.sowmya.adserver.bean;

public class Status {
	
	private String responseStatus;  
	 private String message;  
	  
	 public Status() {  
	 }  
	  
	 public Status(String code, String message) {  
	  this.responseStatus = code;  
	  this.message = message;  
	 }

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}  
	 

}
