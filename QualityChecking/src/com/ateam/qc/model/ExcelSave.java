package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name=ExcelSave.TABLE_EXCEL_ITEM)
public class ExcelSave {
	protected static final String TABLE_EXCEL_ITEM="tb_excel";
	
	private int id; //ExcelId
	private int flowId;//流水号，唯一标示
	private String myGroup;//组别
	private String time;//日期
	private String fanHao;//番号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFlowId() {
		return flowId;
	}
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	
	public String getMyGroup() {
		return myGroup;
	}
	public void setMyGroup(String myGroup) {
		this.myGroup = myGroup;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFanHao() {
		return fanHao;
	}
	public void setFanHao(String fanHao) {
		this.fanHao = fanHao;
	}
	
	
}
