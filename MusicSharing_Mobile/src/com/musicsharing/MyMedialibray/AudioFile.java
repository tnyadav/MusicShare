package com.musicsharing.MyMedialibray;

public class AudioFile {
	private String id;
	private String diaplayName;
	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDiaplayName() {
		return diaplayName;
	}

	public void setDiaplayName(String diaplayName) {
		this.diaplayName = diaplayName;
	}

	public String getPath() {
		return path;
	}

	public AudioFile(String id, String diaplayName, String path) {
		super();
		this.id = id;
		this.diaplayName = diaplayName;
		this.path = path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return diaplayName;
	}

}
