package com.ateam.qc.fragment;

import com.ateam.qc.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * 左侧菜单
 * @author Helen
 * 2015-6-16上午9:28:23
 */
public class MenuLeftFragment extends Fragment implements OnClickListener{
	private View mView;
	private MenuDelegate mDelegate;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.fragment_menu_left, container);
		mView.findViewById(R.id.btn_history).setOnClickListener(this);
		mView.findViewById(R.id.btn_setting).setOnClickListener(this);
		return mView;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_history:
			if(mDelegate!=null){
				mDelegate.opentHistory();
			}
			break;
		case R.id.btn_setting:
			if(mDelegate!=null){
				mDelegate.openSetting();
			}
			break;
		default:
			break;
		}
	}
	
	public interface MenuDelegate{
		void openSetting();
		void opentHistory();
	}
	
	public void setDelegate(MenuDelegate delegate) {
		this.mDelegate = delegate;
	}
}
