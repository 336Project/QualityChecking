package com.ateam.qc.activity;

import java.util.ArrayList;
import java.util.List;

import com.ateam.qc.R;
import com.ateam.qc.R.drawable;
import com.ateam.qc.R.id;
import com.ateam.qc.R.layout;
import com.ateam.qc.R.style;
import com.ateam.qc.adapter.HistoryListAdapter;
import com.ateam.qc.dao.ExcelDao;
import com.ateam.qc.dao.ExcelItemDao;
import com.ateam.qc.model.ExcelSave;
import com.ateam.qc.utils.AddTimeDialog.OnAddTimeChangeListener;
import com.ateam.qc.utils.AddTimeDialog;
import com.ateam.qc.utils.MyToast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 历史列表
 * @author 魏天武
 * @version 
 * @create_date 2015-6-20上午1:18:14
 */
public class HistoryActivity extends Activity implements OnClickListener {

	private ListView mLvHistory;
	private List<ExcelSave> datas=new ArrayList<ExcelSave>();
	private List<ExcelSave> findDatas=new ArrayList<ExcelSave>();
	private HistoryListAdapter adapter;
	private ExcelDao mExcelDao;
	private ExcelItemDao mExcelItemDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initView();
	}

	private void initView() {
		mExcelItemDao=new ExcelItemDao(this);
		mLvHistory=(ListView)findViewById(R.id.lv_history);
		adapter=new HistoryListAdapter(this, datas);
		mLvHistory.setAdapter(adapter);
		mLvHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int flowId=findDatas.get(position).getFlowId();
				Intent intent=new Intent(HistoryActivity.this,HistoryDetailActivity.class);
				intent.putExtra("flowId", flowId);
				intent.putExtra("group", findDatas.get(position).getMyGroup());
				intent.putExtra("fanhao", findDatas.get(position).getFanHao());
				intent.putExtra("time", findDatas.get(position).getTime());
				startActivity(intent);
			}
		});
		
		TextView tv_titlename = (TextView) findViewById(R.id.tv_titlename);
		tv_titlename.setText("历史记录");
		findViewById(R.id.rl_left).setOnClickListener(this);
		ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setImageResource(R.drawable.back);
		RelativeLayout rl_right = (RelativeLayout) findViewById(R.id.rl_right);
		rl_right.setVisibility(View.INVISIBLE);
		findViewById(R.id.tv_delete).setOnClickListener(this);
		findViewById(R.id.tv_input).setOnClickListener(this);
		
		lordData();
	}
	
	/**
	 * 加载数据
	 */
	private void lordData(){
		datas.clear();
		findDatas.clear();
		SharedPreferences flowId=getSharedPreferences("flowId", Activity.MODE_PRIVATE);
		int mFlowId=flowId.getInt("flowId", 0);
		if(mFlowId!=0){
			mExcelDao=new ExcelDao(this);
			findDatas=mExcelDao.query();
			for (int i = 0; i < findDatas.size(); i++) {
				datas.add(findDatas.get(i));
			}
			adapter.notifyDataSetChanged();
		}else{
			MyToast.showShort(this, "暂时历史数据");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_delete:
			ArrayList<Integer> deleteItem=adapter.getDeleteItem();
			if(deleteItem.size()>0){
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setTitle("确定删除？");
				for (int i = 0; i < deleteItem.size(); i++) {
					mExcelDao.deleteByFlowId(datas.get(deleteItem.get(i)).getFlowId());
					mExcelItemDao.deleteByFlowId(datas.get(deleteItem.get(i)).getFlowId());
				}
				lordData();
			}else{
				MyToast.showShort(this, "请选择要删除信息");
			}
			break;
		case R.id.tv_input:
			Intent intent =new Intent(this,ExportByTimeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
