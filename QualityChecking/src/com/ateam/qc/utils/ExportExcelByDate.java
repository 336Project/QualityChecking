package com.ateam.qc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ateam.qc.constant.Constant;
import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;
import com.team.hbase.utils.FileUtil;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
/**
 * 根据日期范围导出汇总的Excel
 * @author Administrator
 */
public class ExportExcelByDate {
	private static Context mContext;

	// 导出数据
	public static void export(Context context, ArrayList<ExcelItem> AllExcelItem,String excelName) {
		mContext = context;
		WritableWorkbook wwb = null;
		try {
			File destDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ Constant.SAVED_EXCEL_DIR_PATH);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
			wwb = Workbook.createWorkbook(new File(Environment
					.getExternalStorageDirectory() + "/" +  Constant.SAVED_EXCEL_DIR_PATH+"/"+excelName + ".xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wwb != null) {
			// 创建一个可写入的工作表
			WritableSheet ws = wwb.createSheet("数量统计", 0);
//			WritableSheet wsPicture = wwb.createSheet("照片", 1);
			
			int weight=1;
			for (int i = 0; i < AllExcelItem.size(); i++) {
				ExcelItem excelItem = AllExcelItem.get(i);
				boolean fileExist = FileUtil.getInstance().isFileExist(Constant.SAVED_IMAGE_DIR_PATH, excelItem.getPicturePath());
				if(fileExist){
					String filePath = FileUtil.getInstance().getFilePath(Constant.SAVED_IMAGE_DIR_PATH, excelItem.getPicturePath());
					File imageData = new File(filePath);
					WritableImage image =new WritableImage(1, 4*weight+16*(weight-1), 10, 16, imageData );
					try {
						WritableCellFormat wc = new WritableCellFormat(); 
						wc.setAlignment(Alignment.CENTRE);
//						Label labelHead = new Label(1, 5*weight+16*(weight),excelItem.getSizeName()+"　"+excelItem.getPorjectName() ,wc);
//						wsPicture.addCell(labelHead);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
					
//					wsPicture.addImage(image );
					weight+=1;
				}
			}
			//添加表头
			//日期范围表 不需要添加表头
//			try {
//				ws.mergeCells(0, 0, 7, 0);
//				WritableCellFormat wc = new WritableCellFormat(); 
//				wc.setAlignment(Alignment.CENTRE);
//				Label labelHead = new Label(0, 0, excel.getGroup()+excel.getFanHao()+"查核表"+"　　　"+excel.getTime(),wc);
//				ws.addCell(labelHead);
//			} catch (RowsExceededException e1) {
//				e1.printStackTrace();
//			} catch (WriteException e1) {
//				e1.printStackTrace();
//			}
			// 下面开始添加单元格
			String[] topic = { "型号", "组别", "日期", "番号","不良率"};
			for (int i = 0; i < topic.length; i++) {
				Label labelC = new Label(i, 0, topic[i]);
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
			
			for (int j = 0; j < AllExcelItem.size(); j++) {
				
				ExcelItem excelItem = AllExcelItem.get(j);
				li = new ArrayList<String>();
				
				li.add(excelItem.getSizeName());
				li.add(excelItem.getMyGroup());
				li.add(excelItem.getTime());
				li.add(excelItem.getFanHao());
				if(excelItem.getCheckNum()==0||excelItem.getUnqualifiedNum()==0){
					li.add("0%");
				}
				else{
					li.add(SysUtil.format(((float)excelItem.getUnqualifiedNum()/excelItem.getCheckNum())*100)+"%");
				}
				
				System.out.println(li.size());
				int k = 0;
				for (String l : li) {
					Label labelC = new Label(k, j + 1, l);
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
