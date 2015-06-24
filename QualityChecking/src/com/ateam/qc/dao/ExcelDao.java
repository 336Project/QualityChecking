package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelSave;
import com.ateam.qc.model.Project;
import com.team.hbase.dao.HBaseDao;

public class ExcelDao extends HBaseDao {

	public ExcelDao(Context context) {
		super(context);
	}
	
	public void save(ExcelSave pro){
		mDb.save(pro);
	}
	
	public List<ExcelSave> query(){
		return mDb.findAll(ExcelSave.class,"time DESC");
	}
	public void delete(int id){
		mDb.deleteById(ExcelSave.class, id);
	}
	
	/**
	 * 根据流水好删除数据
	 * @param flowId
	 */
	public void deleteByFlowId(int flowId){
		mDb.deleteByWhere(ExcelSave.class, "flowId="+flowId);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	
	public boolean isExist(String no){
		List<ExcelSave> pros=mDb.findAllByWhere(ExcelSave.class, "no = '"+no+"'");
		if(pros!=null&&pros.size()>0){
			return true;
		}
		return false;
	}
}
