package com.mas.album;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class Util {
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
   /*
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
       //旋转图片 动作   
       Matrix matrix = new Matrix();;  
       matrix.postRotate(angle);  
       System.out.println("angle2=" + angle);  
       // 创建新的图片   
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
   
	public static Bitmap CompressBytes(byte[] content)
	{
		int length = content.length;
		
//		 int degree = readPictureDegree(f.getAbsolutePath());
		 
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = false;
		
		if(length < 300000){
			option.inSampleSize = 4;
		}else{
			option.inSampleSize = 8;
		}
		return BitmapFactory
				.decodeByteArray(content, 0, content.length, option);
	}
	
	
	public static Bitmap CompressBytes(byte[] content,String fileName)
	{
		int length = content.length;
		
		 int degree = readPictureDegree(fileName);
		 
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = false;
		
		if(length < 300000){
			option.inSampleSize = 4;
		}else{
			option.inSampleSize = 8;
		}
		Bitmap cbitmap = BitmapFactory
			.decodeByteArray(content, 0, content.length, option);
		
		Bitmap newbitmap = rotaingImageView(degree, cbitmap);
		return newbitmap;
	}


	public static byte[] CompressBytes(byte[] content, int max_size,String fileName) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		Bitmap bm;
		byte[] compressBytes = null;

		int rate = 100;

		int scale = (int) Math.floor(content.length / max_size);

		BitmapFactory.Options option = new BitmapFactory.Options();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		option.inJustDecodeBounds = false;
		option.inSampleSize = 1;

		if (scale < 2) {
			option.inSampleSize = 1;
		} else if (scale >= 2 && scale < 8) {
			option.inSampleSize = 2;
		} else if (scale >= 8 && scale < 32) {
			option.inSampleSize = 4;
		} else if (scale >= 32 && scale < 128) {
			option.inSampleSize = 8;
		} else {
			option.inSampleSize = 16;
		}

		try {
			bm = BitmapFactory.decodeByteArray(content, 0, content.length,
					option);
		} catch (OutOfMemoryError e) {

			option.inSampleSize *= 2;
			bm = BitmapFactory.decodeByteArray(content, 0, content.length,
					option);
		}

		do {
			if (compressBytes == null) {

				bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);

				compressBytes = baos.toByteArray();
			} else {
				rate = rate - 3;
				baos.reset();
				compressBytes = null;
				System.gc();
				bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);
				compressBytes = baos.toByteArray();

			}

		} while (compressBytes.length > max_size);

		bm = null;

		System.gc();

		return compressBytes;
	}

	public static byte[] BitmapTobytes(Bitmap bm, int rate) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);
		return baos.toByteArray();
	}



	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

}
