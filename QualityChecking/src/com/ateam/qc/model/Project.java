package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name=Project.TABLE_PROJECT)
public class Project {
	public static final String TABLE_PROJECT="tb_project";
	private int id;
	private String no;
	private String content;
	private String shortName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Override
	public boolean equals(Object o) {
		Project otherSize=(Project)o;
		if(this.id==otherSize.getId()){
			return true;
		}
		return false;
	}
}
