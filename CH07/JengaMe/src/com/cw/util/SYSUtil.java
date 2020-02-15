package com.cw.util;

import javax.vecmath.Quat4f;

import android.graphics.Bitmap;
import android.graphics.Matrix;
public class SYSUtil 
{
	//將四元數轉為角度及轉軸向量
	public static float[] fromSYStoAXYZ(Quat4f q4)
	{	
		double sitaHalf=Math.acos(q4.w);
		float nx=(float) (q4.x/Math.sin(sitaHalf));
		float ny=(float) (q4.y/Math.sin(sitaHalf));
		float nz=(float) (q4.z/Math.sin(sitaHalf));
		return new float[]{(float) Math.toDegrees(sitaHalf*2),nx,ny,nz};
	}
	
	
	public static Bitmap scaleToFit(Bitmap bm,float width_Ratio,float height_Ratio)
	{		
    	int width = bm.getWidth(); 							//圖片寬度
    	int height = bm.getHeight();							//圖片高度
    	Matrix matrix = new Matrix(); 
    	matrix.postScale((float)width_Ratio, (float)height_Ratio);				//圖片等比例拉遠為原來的fblRatio倍
    	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);//宣告點陣圖        	
    	return bmResult;									//傳回被縮放的圖片
    }
}
