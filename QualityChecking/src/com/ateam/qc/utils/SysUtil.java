package com.ateam.qc.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class SysUtil {
	/**
	 * 生成缩略图
	 * @param bitmap
	 * @return
	 * @throws Exception
	 */
	public static byte[] extractThumbnail(Bitmap bitmap) throws Exception{
		float scale=calScale(bitmap);
		bitmap=ThumbnailUtils.extractThumbnail(bitmap, (int)(bitmap.getWidth()/scale), (int)(bitmap.getHeight()/scale), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 90;
        while (baos.toByteArray().length / 1024>80&&options>0) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10
        }
        bitmap.recycle();
        bitmap=null;
        baos.flush();
		byte[] resource =baos.toByteArray();
		baos.close();
		return resource;
	}
	
	/**
	 * 将byte  类型转换成bitmap类型
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] mByte){
		if (mByte.length != 0) {
		   return BitmapFactory.decodeByteArray(mByte, 0, mByte.length);
		 } else {
		       return null;
		 }
	}
	
	/**
	 * 根据宽高获取缩放比例
	 * @param bitmap
	 * @return
	 */
	private static float calScale(Bitmap bitmap){
		int width=480,height=800;//一般分辨率480*800
		float w = bitmap.getWidth(); 
		float h = bitmap.getHeight();
		if(w>=1920||h>=1920){//1920*1080
			width=1080;
			height=1920;
		}else if(w>=1280||h>=1280){//720*1280
			width=720;
			height=1280;
		}
		float be = 1.0f;//be=1表示不缩放
	    if (w > h && w > width) {//如果宽度大的话根据宽度固定大小缩放  
	        be = (float) (w / width);  
	    } else if (w < h && h > height) {//如果高度高的话根据宽度固定大小缩放  
	        be = (float) (h / height);  
	    }  
	    if (be <= 0)
	        be = 1; 
		return be;
	}
}
