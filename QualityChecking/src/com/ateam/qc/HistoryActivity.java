package com.ateam.qc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initView();
	}

	private void initView() {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.set, menu);
		return true;
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
