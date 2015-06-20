package com.ateam.qc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ExportExcel {
	private static Context mContext;

	// 导出数据
	public static void export(Context context, Excel excel) {
		mContext = context;
		Calendar c = Calendar.getInstance();
//		String filename = Integer.toString(c.get(Calendar.YEAR)) + "-"
//				+ Integer.toString(c.get(Calendar.MONTH) + 1) + "-"
//				+ Integer.toString(c.get(Calendar.DATE)) + "_"
//				+ Integer.toString(c.get(Calendar.HOUR)) + "-"
//				+ Integer.toString(c.get(Calendar.MINUTE)) + "-"
//				+ Integer.toString(c.get(Calendar.SECOND));
		String filename=excel.getTime();
		WritableWorkbook wwb = null;
		try {
			// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
			wwb = Workbook.createWorkbook(new File(Environment
					.getExternalStorageDirectory() + "/" + filename + ".xls"));
			Log.e("", "wwb" + wwb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wwb != null) {
			// 创建一个可写入的工作表
			// Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
			WritableSheet ws = wwb.createSheet("数量统计", 0);
			WritableSheet wsPicture = wwb.createSheet("照片", 1);
			
			//添加表头
			try {
				ws.mergeCells(0, 0, 7, 0);
				WritableCellFormat wc = new WritableCellFormat(); 
				wc.setAlignment(Alignment.CENTRE);
				Label labelHead = new Label(0, 0, excel.getGroup()+excel.getFanHao()+"查核表"+"　　　"+excel.getTime(),wc);
				ws.addCell(labelHead);
			} catch (RowsExceededException e1) {
				e1.printStackTrace();
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
			// 下面开始添加单元格
			String[] topic = { "型号", "查核次数", "NG数", "检查数量", "不合格数量",
					"不良率", "项目", "处理方式",};
			for (int i = 0; i < topic.length; i++) {
				Label labelC = new Label(i, 1, topic[i]);
				try {
					// 将生成的单元格添加到工作表中
					ws.addCell(labelC);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
			ArrayList<String> li;
			ArrayList<ExcelItem> excelItemsList = excel.getExcelItemsList();
			
			for (int j = 0; j < excelItemsList.size(); j++) {
				
				ExcelItem excelItem = excelItemsList.get(j);
				li = new ArrayList<String>();
				
				li.add(excelItem.getSize().getName());
				li.add(excelItem.getExamineNum()+"");
				li.add(excelItem.getNgNum()+"");
				li.add(excelItem.getCheckNum()+"");
				li.add(excelItem.getUnqualifiedNum()+"");
				if(excelItem.getCheckNum()==0||excelItem.getUnqualifiedNum()==0){
					li.add("0");
				}
				else{
					li.add(excelItem.getUnqualifiedNum()/excelItem.getCheckNum()+"");
				}
				li.add(excelItem.getProject().getContent());
				li.add(excelItem.getProcessMode());
				
				System.out.println(li.size());
				int k = 0;
				for (String l : li) {
					Label labelC = new Label(k, j + 2, l);
					k++;
					try {
						// 将生成的单元格添加到工作表中
						ws.addCell(labelC);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
				li = null;
			}

			try {
				// 从内存中写入文件中
				wwb.write();
				// 关闭资源，释放内存
				wwb.close();
				Toast.makeText(mContext, "生成excel成功！", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "生成excel失败！", Toast.LENGTH_SHORT).show();
			} catch (WriteException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "生成excel失败！", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(mContext, "生成excel失败！", Toast.LENGTH_SHORT).show();
		}
	}
}