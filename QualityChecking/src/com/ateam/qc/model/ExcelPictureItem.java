package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name=ExcelPictureItem.TABLE_EXCEL_ITEM)
public class ExcelPictureItem {
	@Transient
	protected static final String TABLE_EXCEL_ITEM="tb_excel_picture_item";
	
	private int id;
	private Badness badness; //不良
	private String processMode;//处理方式
	private String picturePath;
	private String time;//日期
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Badness getBadness() {
		return badness;
	}
	public void setBadness(Badness badness) {
		this.badness = badness;
	}
	public String getProcessMode() {
		return processMode;
	}
	public void setProcessMode(String processMode) {
		this.processMode = processMode;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
