package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Project;
import com.team.hbase.dao.HBaseDao;

public class ProjectDao extends HBaseDao {

	public ProjectDao(Context context) {
		super(context);
	}
	
	public void save(Project pro){
		mDb.save(pro);
	}
	
	public List<Project> query(){
		return mDb.findAll(Project.class, "no ASC");
	}
	public void delete(int id){
		mDb.deleteById(Project.class, id);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	
	public boolean isExist(String no){
		List<Project> pros=mDb.findAllByWhere(Project.class, "no = '"+no+"'");
		if(pros!=null&&pros.size()>0){
			return true;
		}
		return false;
	}
}
