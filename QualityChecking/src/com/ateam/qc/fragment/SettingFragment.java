package com.ateam.qc.fragment;

import com.ateam.qc.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 设置界面
 * @author Helen
 * 2015-6-16上午10:45:57
 */
public class SettingFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_setting, null);
	}
}
