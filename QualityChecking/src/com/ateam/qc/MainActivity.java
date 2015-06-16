package com.ateam.qc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{

    private DrawerLayout drawer_layout;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
	private void initView() {
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.ll_set).setOnClickListener(this);
		findViewById(R.id.ll_history).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_left:
			drawer_layout.openDrawer(Gravity.LEFT);
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

}
