package com.threemc.data;

public class Position {

	private int id;
	private String positionName;
	private String positionDesc;
	public Position(String positionName, String positionDesc) {
		this.positionName = positionName;
		this.positionDesc = positionDesc;
	}

	public Position(int id, String positionName, String positionDesc) {
		this(positionName, positionDesc);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getPositionDesc() {
		return positionDesc;
	}

	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
}
