package com.ateam.qc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.ateam.qc.constant.Constant;
import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;
import com.team.hbase.utils.FileUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
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

public class ExportExcel {
	private static Context mContext;
	private ArrayList<ExcelItem> oldExcelItemsList=new ArrayList<ExcelItem>();//已存在的表里面的信息
	private static boolean hasExcel=false;
	private static Workbook book;//已经存在的excel对象
	private static int Rows=0;

	// 导出数据
	public static void export(Context context, Excel excel, String excelName) {
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
			// FileUtil.getInstance().createFileInSDCard(Constant.SAVED_EXCEL_DIR_PATH,
			// excelName+".xls")
			// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
			File file=new File(Environment
					.getExternalStorageDirectory() + "/" +  Constant.SAVED_EXCEL_DIR_PATH+"/"+excelName + ".xls");
			//判断是否已经生成了excel表格
	    	if(file.exists()){
	    		hasExcel=true;
	    		try {
					book = Workbook.getWorkbook(file);
				} catch (BiffException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		}
			wwb = Workbook.createWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wwb != null) {
			// 创建一个可写入的工作表
			// Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
			WritableSheet ws = wwb.createSheet("数量统计", 0);
			WritableSheet wsPicture = wwb.createSheet("照片", 1);

			ArrayList<ExcelItem> excelItemsList = excel.getExcelItemsList();
			int weight = 1;
			for (int i = 0; i < excelItemsList.size(); i++) {
				ExcelItem excelItem = excelItemsList.get(i);
				boolean fileExist = FileUtil.getInstance().isFileExist(
						Constant.SAVED_IMAGE_DIR_PATH,
						excelItem.getPicturePath());
				if (fileExist) {
					String filePath = FileUtil.getInstance().getFilePath(
							Constant.SAVED_IMAGE_DIR_PATH,
							excelItem.getPicturePath());
					File imageData = new File(filePath);
					WritableImage image = new WritableImage(1, 4 * weight + 16
							* (weight - 1), 10, 16, imageData);
					try {
						WritableCellFormat wc = new WritableCellFormat();
						wc.setAlignment(Alignment.CENTRE);
						Label labelHead = new Label(
								1, 
								5 * weight + 16* (weight), 
								excelItem.getSize().getName() + "　"
								+ excelItem.getProject().getContent(), 
								wc);
						wsPicture.addCell(labelHead);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}

					wsPicture.addImage(image);
					weight += 1;
				}
			}
			// 添加表头
			try {
				ws.mergeCells(0, 0, 7, 0);
				WritableCellFormat wc = new WritableCellFormat();
				wc.setAlignment(Alignment.CENTRE);
				Label labelHead = new Label(0, 0, excel.getGroup()
						+ excel.getFanHao() + "查核表" + "　　　" + excel.getTime(),
						wc);
				ws.addCell(labelHead);
			} catch (RowsExceededException e1) {
				e1.printStackTrace();
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
			// 下面开始添加单元格
			String[] topic = { "型号", "查核次数", "NG数", "检查数量", "不合格数量", "不良率",
					"简称", "不良状况", };
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
			
			/**
			 * 如果已经存在excel表数据  获取并添加的新表中  begin
			 */
			if(hasExcel){
				ArrayList<String> history;
				// 获得第一个工作表对象            
				Sheet sheet = book.getSheet(0); 
				Rows = sheet.getRows(); 
				for (int i = 2; i < Rows; ++i) { 
//						for (int j = 0; j < Cols; ++j) { 
//							
//						} 
					history = new ArrayList<String>();
					history.add((sheet.getCell(0, i)).getContents());
					history.add((sheet.getCell(1, i)).getContents());
					history.add((sheet.getCell(2, i)).getContents());
					history.add((sheet.getCell(3, i)).getContents());
					history.add((sheet.getCell(4, i)).getContents());
					history.add((sheet.getCell(5, i)).getContents());
					history.add((sheet.getCell(6, i)).getContents());
					history.add((sheet.getCell(7, i)).getContents());
					System.out.println(history.size());
					int k = 0;
					for (String l : history) {
						Label labelC = new Label(k, i, l);
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
					history = null;
				} 
				book.close(); 
			}
			/**
			 * 如果已经存在excel表数据  获取并添加的新表中  end
			 */
			ArrayList<String> li;

			//外面传入的值存入excel中
			for (int j = 0; j < excelItemsList.size(); j++) {

				ExcelItem excelItem = excelItemsList.get(j);
				li = new ArrayList<String>();

				li.add(excelItem.getSize().getName());
				li.add(excelItem.getExamineNum() + "");
				li.add(excelItem.getNgNum() + "");
				li.add(excelItem.getCheckNum() + "");
				li.add(excelItem.getUnqualifiedNum() + "");
				if (excelItem.getCheckNum() == 0
						|| excelItem.getUnqualifiedNum() == 0) {
					li.add("0%");
				} else {
					li.add(SysUtil.format(((float)excelItem.getUnqualifiedNum()
							/excelItem.getCheckNum())*100) + "%");
				}
				li.add(excelItem.getProject().getContent());
				li.add(excelItem.getProcessMode());

				System.out.println(li.size());
				int k = 0;
				int fistRow=0;
				//要是有历史数据  行从 j+rows 即当前数据列加上已有行数， 没有历史数据  就从j+fistrow 去掉头部的第三行开始
				if(!hasExcel){
					fistRow=2;
				}
				for (String l : li) {
					Label labelC = new Label(k, j+fistRow+Rows, l);
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
				Toast.makeText(mContext, "生成excel成功！", Toast.LENGTH_SHORT)
						.show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "生成excel失败！", Toast.LENGTH_SHORT)
						.show();
			} catch (WriteException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "生成excel失败！", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(mContext, "生成excel失败！", Toast.LENGTH_SHORT).show();
		}
	}
	
}
