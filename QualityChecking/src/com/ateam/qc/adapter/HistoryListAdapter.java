package com.ateam.qc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ateam.qc.R;
import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelSave;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;

/**
 * 历史数据列表
 * @author 魏天武
 * @version 
 * @create_date 2015-6-20上午1:16:15
 */
public class HistoryListAdapter extends HBaseAdapter<ExcelSave> {

	private ArrayList<Integer> mDeleteItem=new ArrayList<Integer>();
	
	public HistoryListAdapter(Context c, List<ExcelSave> datas) {
		super(c, datas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, ExcelSave bean, final int position) {
		// TODO Auto-generated method stub
		CheckBox mCB = (CheckBox)holder.getView(R.id.cb_history);
		mCB.setChecked(false);
		mCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mDeleteItem.add(position);
				}else{
					mDeleteItem.remove(""+position);
				}
			}
		});
		
		TextView mtv = (TextView)holder.getView(R.id.tv_historyName);
		mtv.setText("时间:"+bean.getTime()+"    组别:"+bean.getMyGroup()+"  流水号:"+bean.getFlowId()+"");
	}

	@Override
	public int getResId() {
		// TODO Auto-generated method stub
		return R.layout.adapter_history_list;
	}
	
	public ArrayList<Integer> getDeleteItem(){
		return mDeleteItem;
	}

}
