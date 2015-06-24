package com.ateam.qc.activity;

import com.ateam.qc.R;
import com.ateam.qc.R.drawable;
import com.ateam.qc.R.id;
import com.ateam.qc.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 设置界面
 * @author Helen
 * 2015-6-17下午2:43:17
 */
public class SetActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initView();
	}

	private void initView() {
		TextView tv_titlename = (TextView) findViewById(R.id.tv_titlename);
		tv_titlename.setText("设置中心");
		findViewById(R.id.rl_left).setOnClickListener(this);
		ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setImageResource(R.drawable.back);
		RelativeLayout rl_right = (RelativeLayout) findViewById(R.id.rl_right);
		rl_right.setVisibility(View.INVISIBLE);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;

		default:
			break;
		}
	}
	/**
	 * 设置组别
	 */
	public void goSettingGroup(View view){
		jump(SettingGroupActivity.class);
	}
	/**
	 * 设置型号
	 */
	public void goSettingSize(View view){
		jump(SettingSizeActivity.class);
	}
	/**
	 * 设置项目
	 */
	public void goSettingProject(View view){
		jump(SettingProjectActivity.class);
	}
	
	private void jump(Class<?> cls){
		startActivity(new Intent(this, cls));
	}
	
}
