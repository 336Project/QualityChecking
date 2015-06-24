package com.ateam.qc.activity;

import java.util.ArrayList;

import com.ateam.qc.R;
import com.ateam.qc.R.drawable;
import com.ateam.qc.R.id;
import com.ateam.qc.R.layout;
import com.ateam.qc.adapter.HistoryDetailAdapter;
import com.ateam.qc.dao.ExcelItemDao;
import com.ateam.qc.model.ExcelItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryDetailActivity extends Activity {

	private LinearLayout mLayout;
	private TextView mTvGroup;
	private TextView mTvFanHao;
	private TextView mTvTime;
	private ListView mLvShowDetail;
	
	private ArrayList<ExcelItem> mDatas=new ArrayList<ExcelItem>();
	private ArrayList<ExcelItem> mFindDatas=new ArrayList<ExcelItem>();
	private HistoryDetailAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_detail);
		initView();
	}
	
	private void initView(){
		TextView tv_titlename = (TextView) findViewById(R.id.tv_titlename);
		tv_titlename.setText("历史记录");
		findViewById(R.id.rl_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setImageResource(R.drawable.back);
		RelativeLayout rl_right = (RelativeLayout) findViewById(R.id.rl_right);
		rl_right.setVisibility(View.INVISIBLE);
		
		mTvGroup=(TextView)findViewById(R.id.tv_group);
		mTvGroup.setText("组别:"+getIntent().getStringExtra("group"));
		mTvFanHao=(TextView)findViewById(R.id.tv_fanhao);
		mTvFanHao.setText("番号:"+getIntent().getStringExtra("fanhao"));
		mTvTime=(TextView)findViewById(R.id.tv_time);
		mTvTime.setText("时间:"+getIntent().getStringExtra("time"));
		mLvShowDetail=(ListView)findViewById(R.id.lv_showDetail);
		
		mAdapter=new HistoryDetailAdapter(this, mDatas);
		mLvShowDetail.setAdapter(mAdapter);
		initData();
	}

	private void initData(){
		int flowId=getIntent().getIntExtra("flowId", -1);
		if(flowId!=-1){
			ExcelItemDao mDao=new ExcelItemDao(this);
			mFindDatas=(ArrayList<ExcelItem>) mDao.findByFlowId(flowId);
			for (int i = 0; i < mFindDatas.size(); i++) {
				mDatas.add(mFindDatas.get(i));
			}
			mAdapter.notifyDataSetChanged();
		}
	}
}
