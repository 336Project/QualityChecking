package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.model.ExcelSave;
import com.ateam.qc.model.Project;
import com.team.hbase.dao.HBaseDao;

public class ExcelItemDao extends HBaseDao {

	public ExcelItemDao(Context context) {
		super(context);
	}
	
	public void save(ExcelItem pro){
		mDb.save(pro);
	}
	
	public List<ExcelItem> query(){
		return mDb.findAll(ExcelItem.class, "no ASC");
	}
	
	/**
	 * 根据流水好删除数据
	 * @param flowId
	 */
	public void deleteByFlowId(int flowId){
		mDb.deleteByWhere(ExcelItem.class, "flowId='"+flowId+"'");
	}
	
	/**
	 * 根据流水号查找数据
	 * @param flowId
	 * @return
	 */
	public List<ExcelItem> findByFlowId(int flowId){
		return mDb.findAllByWhere(ExcelItem.class, "flowId='"+flowId+"'");
	}
	
	/**
	 * 根据时间查询
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<ExcelItem> findByTime(String beginTime,String endTime){
		return mDb.findAllByWhere(ExcelItem.class, "time>='"+beginTime+"'"+" and time<='"+endTime+"'");
	}
	
	public void delete(int id){
		mDb.deleteById(ExcelItem.class, id);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	
	public boolean isExist(String no){
		List<ExcelItem> pros=mDb.findAllByWhere(ExcelItem.class, "no = '"+no+"'");
		if(pros!=null&&pros.size()>0){
			return true;
		}
		return false;
	}
}
