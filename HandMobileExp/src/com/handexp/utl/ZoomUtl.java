package com.handexp.utl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ZoomUtl{

	/**
	 * 放大或者缩小Bitmap
	 * @param bmp
	 * @param flag
	 * @return resizeBmp
	 */
	public static Bitmap resize(Bitmap bmp, boolean flag){
		//设置放大缩小比例
		float scale;
		if(flag){
			scale = (float) 1.25;
		}else{
			scale = (float) 0.8;
		}
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
		return resizeBmp;
		
	}
	
}
