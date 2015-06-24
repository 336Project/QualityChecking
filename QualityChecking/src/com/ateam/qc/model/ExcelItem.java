package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name=ExcelItem.TABLE_EXCEL_ITEM)
public class ExcelItem {
	@Transient
	protected static final String TABLE_EXCEL_ITEM="tb_excel_item";
	
	private int id;
	private int flowId;//流水号用来和excel表中的数据进行绑定
	private Project project; //项目内容 
	private Size size; //型号
	private String sizeName;//型号名称
	private String porjectName;//项目名称
	private int checkNum; //检查数量
	private int unqualifiedNum; //不合格数量
	private int examineNum;//查核数量
	private int ngNum;//NG数量
	private String processMode;//处理方式
	private String[] pictureArray; //照片
	private String picturePath;
	private String picture;
	private String myGroup;//组别
	private String time;//日期
	private String fanHao;//番号
	
	
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
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPorjectName() {
		return porjectName;
	}
	public void setPorjectName(String porjectName) {
		this.porjectName = porjectName;
	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
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
