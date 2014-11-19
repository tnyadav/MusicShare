package com.musicsharing.connections;

import com.google.gson.annotations.Expose;

public class RequestedSong {
	@Expose
	private String senderId;
	
	@Expose
	private String filePath;

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "RequestedSong [senderId=" + senderId + ", filePath=" + filePath
				+ "]";
	}

	
	
}
