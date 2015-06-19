package com.ateam.qc.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.ateam.qc.R;
import com.ateam.qc.model.Excel;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;

/**
 * 历史数据列表
 * @author 魏天武
 * @version 
 * @create_date 2015-6-20上午1:16:15
 */
public class HistoryListAdapter extends HBaseAdapter<Excel>{

	public HistoryListAdapter(Context c, List<Excel> datas) {
		super(c, datas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, Excel bean, int position) {
		// TODO Auto-generated method stub
		((TextView)holder.getView(R.id.tv_history_name)).setText(bean.getFlowId());
	}

	@Override
	public int getResId() {
		// TODO Auto-generated method stub
		return R.layout.adapter_history_list;
	}

}
