package com.ateam.qc.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.ateam.qc.R;
import com.ateam.qc.application.MyApplication;
import com.ateam.qc.dao.GroupDao;
import com.ateam.qc.dao.ProjectDao;
import com.ateam.qc.model.Group;
import com.ateam.qc.model.Project;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;
import com.team.hbase.widget.HAutoCompleteTextView;

/**
 * 项目设置
 * @author Helen
 * 2015-6-18上午9:52:03
 */
public class SettingProjectActivity extends HBaseActivity {
	private ListView mListView;
	private List<Project> mDatas;
	private ProjectAdapter mAdapter;
	private ProjectDao mProjectDao;
	
	private ArrayList<String> mProNoList=new ArrayList<String>();
	private MyApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getRightIcon().setVisibility(View.GONE);
		getLeftIcon().setImageDrawable(getResources().getDrawable(R.drawable.back));
		setBaseContentView(R.layout.activity_setting_project);
		setActionBarTitle("项目设置");
		init();
		initApplication();
	}

	private void initApplication() {
		mApplication = (MyApplication)getApplication();
	}

	private void init() {
		mListView=(ListView) findViewById(R.id.listView);
		mProjectDao=new ProjectDao(this);
		mDatas=mProjectDao.query();
		if(mDatas==null||mDatas.size()==0){
			mDatas=new ArrayList<Project>();
			showMsg(this, R.string.empty_data);
		}
		mAdapter=new ProjectAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		refreshNo();
	}
	/**
	 * 刷新序号
	 */
	private void refreshNo(){
		mProNoList.clear();
		for (int i = 1; i <= 100; i++) {
			mProNoList.add(i-1,i+"");
		}
		for (Project pro : mDatas) {
			mProNoList.remove(pro.getNo());
		}
	}
	/**
	 * 添加项目
	 */
	public void addProject(View view){
		AlertDialog.Builder builder=new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("添加项目");
		View v=LayoutInflater.from(this).inflate(R.layout.item_dialog_add_project, null);
		//序号
		final Spinner noSpinner=(Spinner) v.findViewById(R.id.sp_no);
		ArrayAdapter<String> noAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mProNoList);
		noAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		noSpinner.setAdapter(noAdapter);
		
		//组别
		final Spinner groupSpinner=(Spinner) v.findViewById(R.id.sp_group);
		ArrayList<String> groupList = new ArrayList<String>();
		GroupDao groupDao = new GroupDao(this);
		List<Group> groups = groupDao.query();
		if(groups!=null){
			for (Group group : groups) {
				groupList.add(group.getName());
			}
		}
		ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupList);
		groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpinner.setAdapter(groupAdapter);
		
		final HAutoCompleteTextView editShorName = (HAutoCompleteTextView) v.findViewById(R.id.et_short_name);
		final HAutoCompleteTextView editContent = (HAutoCompleteTextView) v.findViewById(R.id.et_content);
		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss(dialog, true);
			}
		});
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String no=noSpinner.getSelectedItem().toString();
				String shortName=editShorName.getText().toString();
				String content=editContent.getText().toString();
				String groupName = groupSpinner.getSelectedItem().toString();
				if(mProjectDao.isExist(no)){
					showMsg(SettingProjectActivity.this, "该序号已存在，请重新选择");
					dismiss(dialog, false);
				}else if(TextUtils.isEmpty(shortName)){
					showMsg(SettingProjectActivity.this, "简称不能为空");
					dismiss(dialog, false);
				}else if(TextUtils.isEmpty(content)){
					showMsg(SettingProjectActivity.this, "内容不能为空");
					dismiss(dialog, false);
				}else{
					save(no, shortName, content,groupName);
					dismiss(dialog, true);
					showMsg(SettingProjectActivity.this, "添加成功");
				}
				
			}
		});
		builder.create().show();
		
	}
	/**
	 * 删除项目
	 */
	public void deleteProject(View view){
		boolean[] isChecked=mAdapter.getHasChecked();
		for (int i = 0; i < isChecked.length; i++) {
			if(isChecked[i]){
				mProjectDao.delete(mDatas.get(i).getId());
			}
		}
		notifyDataSetChanged();
		mApplication.setRefreshProject(true);
		showMsg(SettingProjectActivity.this, "删除成功");
	}
	/**
	 * 修改 
	 **/
	public void updateProject(View view){
		boolean[] isChecked=mAdapter.getHasChecked();
		int count = 0;
		Project pro = null;
		for (int i = 0; i < isChecked.length; i++) {
			if(isChecked[i]){
				count++;
				if(count > 1){
					showMsg(this, "一次只能修改一项");
					return;
				}
				pro = mDatas.get(i);
			}
		}
		if(isChecked.length == 0 || pro == null){
			showMsg(this, "请选中项再操作");
			return;
		}
		final Project currPro = pro;
		
		AlertDialog.Builder builder=new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("修改项目");
		View v=LayoutInflater.from(this).inflate(R.layout.item_dialog_add_project, null);
		
		final Spinner noSpinner=(Spinner) v.findViewById(R.id.sp_no);
		ArrayList<String> proNos = mProNoList;
		proNos.add(0, currPro.getNo());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proNos);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		noSpinner.setAdapter(adapter);
		noSpinner.setSelection(0);
		
		final HAutoCompleteTextView editShorName = (HAutoCompleteTextView) v.findViewById(R.id.et_short_name);
		editShorName.setText(currPro.getShortName());
		final HAutoCompleteTextView editContent = (HAutoCompleteTextView) v.findViewById(R.id.et_content);
		editContent.setText(currPro.getContent());
		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss(dialog, true);
			}
		});
		builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String no=noSpinner.getSelectedItem().toString();
				String shortName=editShorName.getText().toString();
				String content=editContent.getText().toString();
				if(TextUtils.isEmpty(shortName)){
					showMsg(SettingProjectActivity.this, "简称不能为空");
					dismiss(dialog, false);
				}else if(TextUtils.isEmpty(content)){
					showMsg(SettingProjectActivity.this, "内容不能为空");
					dismiss(dialog, false);
				}else{
					currPro.setNo(no);
					currPro.setShortName(shortName);
					currPro.setContent(content);
					mProjectDao.update(currPro);
					dismiss(dialog, true);
					showMsg(SettingProjectActivity.this, "修改成功");
					notifyDataSetChanged();
					mApplication.setRefreshProject(true);
				}
				
			}
		});
		builder.create().show();
	}
	
	private void dismiss(DialogInterface dialog,boolean isDismiss){
		try{
			Field field=dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isDismiss);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存
	 */
	private void save(String no,String shortName,String content,String groupName){
		Project pro=new Project();
		pro.setContent(content);
		pro.setNo(no);
		pro.setShortName(shortName);
		pro.setGroupName(groupName);
		mProjectDao.save(pro);
		notifyDataSetChanged();
		mApplication.setRefreshProject(true);
	}
	/**
	 * 刷新列表
	 */
	private void notifyDataSetChanged(){
		mDatas.clear();
		mDatas.addAll(mProjectDao.query());
		mAdapter.setHasChecked(new boolean[mDatas.size()]);
		mAdapter.notifyDataSetChanged();
		refreshNo();
	}
	/**
	 * 适配器
	 * @author Helen
	 * 2015-6-18上午10:13:21
	 */
	private class ProjectAdapter extends HBaseAdapter<Project>{
		private boolean[] mHasChecked;
		public ProjectAdapter(Context c, List<Project> datas) {
			super(c, datas);
			mHasChecked=new boolean[getCount()];
		}

		@Override
		public void convert(ViewHolder holder, Project bean,final int position) {
			if(mHasChecked.length!=getCount()){  
	            boolean[] hasCheckedCache=mHasChecked.clone();  
	            mHasChecked=new boolean[getCount()];  
	            for(int i=0;i<hasCheckedCache.length;i++){  
	                mHasChecked[i]=hasCheckedCache[i];  
	            }  
	        }
			holder.getTextView(R.id.tv_project_content).setText(bean.getContent());
			holder.getTextView(R.id.tv_project_short_name).setText(bean.getShortName());
			holder.getTextView(R.id.tv_project_group).setText(bean.getGroupName());
			final CheckBox cb=holder.getCheckBox(R.id.cb_project);
			cb.setText(bean.getNo());
			holder.getConvertView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked=!cb.isChecked();
					cb.setChecked(isChecked);
					mHasChecked[position]=isChecked;
				}
			});
			cb.setChecked(mHasChecked[position]);
		}

		@Override
		public int getResId() {
			return R.layout.adapter_setting_project;
		}
		
		public boolean[] getHasChecked() {
			return mHasChecked;
		}

		public void setHasChecked(boolean[] mHasChecked) {
			this.mHasChecked = mHasChecked;
		}
		
	}
}
