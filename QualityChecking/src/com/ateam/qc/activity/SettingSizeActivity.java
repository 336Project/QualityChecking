package com.ateam.qc.activity;

import java.util.ArrayList;
import java.util.List;

import com.ateam.qc.R;
import com.ateam.qc.R.drawable;
import com.ateam.qc.R.id;
import com.ateam.qc.R.layout;
import com.ateam.qc.R.string;
import com.ateam.qc.application.MyApplication;
import com.ateam.qc.dao.SizeDao;
import com.ateam.qc.model.Size;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;
import com.team.hbase.widget.HAutoCompleteTextView;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 型号设置
 * @author Helen
 * 2015-6-18上午9:22:01
 */
public class SettingSizeActivity extends HBaseActivity {
	private ListView mListView;
	private List<Size> mDatas;
	private HAutoCompleteTextView mEditSizeName;
	private SizeDao mSizeDao;
	private SizeAdapter mAdapter;
	private MyApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getRightIcon().setVisibility(View.GONE);
		getLeftIcon().setImageDrawable(getResources().getDrawable(R.drawable.back));
		setBaseContentView(R.layout.activity_setting_size);
		setActionBarTitle("型号设置");
		init();
		initApplication();
	}
	
	private void initApplication() {
		mApplication = (MyApplication) getApplication();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mListView=(ListView) findViewById(R.id.listView);
		mEditSizeName=(HAutoCompleteTextView) findViewById(R.id.et_size_name);
		mSizeDao=new SizeDao(this);
		mDatas=mSizeDao.query();
		if(mDatas==null||mDatas.size()==0){
			mDatas=new ArrayList<Size>();
			showMsg(this, R.string.empty_data);
		}
		mAdapter=new SizeAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
	}
	/**
	 * 添加组别
	 */
	public void addSize(View view){
		String name=mEditSizeName.getText().toString();
		if(TextUtils.isEmpty(name)){
			showMsg(this, "请输入型号名称");
			return;
		}
		if(mSizeDao.isExist(name)){
			showMsg(this, "该型号已存在，请重新输入");
			return;
		}
		Size size=new Size();
		size.setName(name);
		mSizeDao.save(size);
		mDatas.clear();
		mDatas.addAll(mSizeDao.query());
		mAdapter.notifyDataSetChanged();
		mApplication.setRefreshSize(true);
		showMsg(SettingSizeActivity.this, "添加成功");
	}
	/**
	 * 删除组别
	 */
	public void deleteSize(View view){
		boolean[] isChecked=mAdapter.getHasChecked();
		for (int i = 0; i < isChecked.length; i++) {
			if(isChecked[i]){
				mSizeDao.delete(mDatas.get(i).getId());
			}
		}
		mDatas.clear();
		mDatas.addAll(mSizeDao.query());
		mAdapter.setHasChecked(new boolean[mDatas.size()]);
		mAdapter.notifyDataSetChanged();
		mApplication.setRefreshSize(true);
		showMsg(SettingSizeActivity.this, "删除成功");
	}
	
	private class SizeAdapter extends HBaseAdapter<Size>{
		private boolean[] mHasChecked;
		public SizeAdapter(Context c, List<Size> datas) {
			super(c, datas);
			mHasChecked=new boolean[getCount()];
		}

		@Override
		public void convert(final ViewHolder holder, Size bean,final int position) {
			if(mHasChecked.length!=getCount()){  
	            boolean[] hasCheckedCache=mHasChecked.clone();  
	            mHasChecked=new boolean[getCount()];  
	            for(int i=0;i<hasCheckedCache.length;i++){  
	                mHasChecked[i]=hasCheckedCache[i];  
	            }  
	        }
			
			((TextView)holder.getView(R.id.tv_size_name)).setText(bean.getName());
			final CheckBox cb=holder.getView(R.id.cb_size);
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
			return R.layout.adapter_setting_size;
		}

		public boolean[] getHasChecked() {
			return mHasChecked;
		}

		public void setHasChecked(boolean[] mHasChecked) {
			this.mHasChecked = mHasChecked;
		}
		
	}
}
