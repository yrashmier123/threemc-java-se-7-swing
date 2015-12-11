package com.threemc.data;

public class Notice {

	private int id;
	private String date;
	private String desc;

	public Notice(String date, String desc) {
		this.date = date;
		this.desc = desc;
	}

	public Notice(int id, String date, String desc) {
		this(date,desc);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
