package com.ateam.qc.model;

import java.util.ArrayList;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

import android.app.ListActivity;

@Table(name=Excel.TABLE_EXCEL)
public class Excel {
	@Transient
	public static final String TABLE_EXCEL="tb_excel";
	
	@Id
	private int id; //ExcelId
	private int flowId;//流水号，唯一标示
	private String group;//组别
	private String time;//日期
	private String fanHao;//番号
	
	@Transient
	private ArrayList<ExcelItem> excelItemsList = new ArrayList<ExcelItem>(); //记录列表
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
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
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
	public ArrayList<ExcelItem> getExcelItemsList() {
		return excelItemsList;
	}
	public void setExcelItemsList(ArrayList<ExcelItem> excelItemsList) {
		this.excelItemsList = excelItemsList;
	}
	
}
