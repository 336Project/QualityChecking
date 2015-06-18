package com.ateam.qc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ateam.qc.adapter.ContentItemAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{

    private DrawerLayout drawerLayout;
    private static final String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
	private ArrayAdapter<String> adapter;
	private ListView lvContentBody;
	private View viewHead;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.ll_set).setOnClickListener(this);
		findViewById(R.id.ll_history).setOnClickListener(this);
		//1.单头
//		TextView tvTime = (TextView) findViewById(R.id.tv_time);
//		tvTime.setText(formatTime());
//		spChooseGroup = (MaterialSpinner) findViewById(R.id.sp_choose_group);
//		initSpinnerNoHintNoFloatingLabel();
		initListViewHead();
		//2.单体
		initListView();
	}
	private void initListViewHead() {
		viewHead = LayoutInflater.from(this).inflate(R.layout.content_head, null);
		TextView tvTime = (TextView) viewHead.findViewById(R.id.tv_time);
		tvTime.setText(formatTime());
		initSpinnerNoHintNoFloatingLabel();
	}
	private void initSpinnerNoHintNoFloatingLabel() {
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner1 = (Spinner) viewHead.findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
	}
	private void initListView() {
		lvContentBody = (ListView) findViewById(R.id.lv_content_body);
		lvContentBody.addHeaderView(viewHead);
		lvContentBody.setAdapter(new ContentItemAdapter(this));
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_left:
			drawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.ll_set:
			intent.setClass(MainActivity.this, SetActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_history:
			intent.setClass(MainActivity.this, HistoryActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	public String formatTime(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return simpleDateFormat.format(date);
	}

}
