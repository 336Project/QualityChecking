package com.ateam.qc.model;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
@Table(name=Badness.TABLE_BADNESS)
public class Badness implements Serializable{
	private static final long serialVersionUID = 2059098392910565090L;

	public static final String TABLE_BADNESS="tb_badness";
	
	private int id;
	private String name;
	private String groupName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
	public boolean equals(Object o) {
		Badness otherSize=(Badness)o;
		if(this.id==otherSize.getId()){
			return true;
		}
		return false;
	}
	
}
