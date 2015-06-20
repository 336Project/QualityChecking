package com.ateam.qc;

import java.util.ArrayList;
import java.util.List;

import com.ateam.qc.adapter.HistoryListAdapter;
import com.ateam.qc.model.Excel;
import com.ateam.qc.widget.ExcelItemLinearLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private List<Excel> datas=new ArrayList<Excel>();
	private HistoryListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initView();
	}

	private void initView() {
		mLvHistory=(ListView)findViewById(R.id.lv_history);
		adapter=new HistoryListAdapter(this, datas);
		mLvHistory.setAdapter(adapter);
		
		TextView tv_titlename = (TextView) findViewById(R.id.tv_titlename);
		tv_titlename.setText("历史记录");
		findViewById(R.id.rl_left).setOnClickListener(this);
		ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setImageResource(R.drawable.back);
		RelativeLayout rl_right = (RelativeLayout) findViewById(R.id.rl_right);
		rl_right.setVisibility(View.INVISIBLE);
		findViewById(R.id.tv_delete).setOnClickListener(this);
		findViewById(R.id.tv_input).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;
		case R.id.tv_delete:

			break;
		case R.id.tv_input:

			break;
		default:
			break;
		}
	}

}
