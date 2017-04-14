package com.msxf.sso.model;

public class User {
	private String usercode;
	private String username;
	private String usernamePlain;
	
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsernamePlain() {
		return usernamePlain;
	}
	public void setUsernamePlain(String usernamePlain) {
		this.usernamePlain = usernamePlain;
	}
}