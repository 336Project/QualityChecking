package com.ateam.qc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initView();
	}

	private void initView() {
//		TextView tv_titlename = (TextView) findViewById(R.id.tv_titlename);
//		tv_titlename.setText("设置中心");
//		ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
//		iv_left.setImageResource(R.drawable.back);
//		RelativeLayout rl_right = (RelativeLayout) findViewById(R.id.rl_right);
//		rl_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.set, menu);
		return true;
	}
}
