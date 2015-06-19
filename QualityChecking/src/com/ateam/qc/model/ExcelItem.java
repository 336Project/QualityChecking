package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name=ExcelItem.TABLE_EXCEL_ITEM)
public class ExcelItem {
	protected static final String TABLE_EXCEL_ITEM="tb_excel_item";
	private int id;
	private int flowId;//流水号用来和excel表中的数据进行绑定
	private Project project; //项目内容 
	private Size size; //型号
	private int checkNum; //检查数量
	private int unqualifiedNum; //不合格数量
	private int examineNum;//查核数量
	private int ngNum;//NG数量
	private String processMode;//处理方式
	private String[] pictureArray; //照片
	
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
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public int getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}
	public int getUnqualifiedNum() {
		return unqualifiedNum;
	}
	public void setUnqualifiedNum(int unqualifiedNum) {
		this.unqualifiedNum = unqualifiedNum;
	}
	public int getExamineNum() {
		return examineNum;
	}
	public void setExamineNum(int examineNum) {
		this.examineNum = examineNum;
	}
	public int getNgNum() {
		return ngNum;
	}
	public void setNgNum(int ngNum) {
		this.ngNum = ngNum;
	}
	public String getProcessMode() {
		return processMode;
	}
	public void setProcessMode(String processMode) {
		this.processMode = processMode;
	}
	public String[] getPictureArray() {
		return pictureArray;
	}
	public void setPictureArray(String[] pictureArray) {
		this.pictureArray = pictureArray;
	}
}
