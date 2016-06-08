package com.settings;

public class FTPSettings {
	private String host;
	private int port;
	private String user;
	private String pass;
	public FTPSettings(String host,int port,String user,String pass){
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public String getUser() {
		return user;
	}
	public String getPass() {
		return pass;
	}
}
