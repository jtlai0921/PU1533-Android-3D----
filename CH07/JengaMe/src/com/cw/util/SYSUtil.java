package com.cw.util;

import javax.vecmath.Quat4f;

import android.graphics.Bitmap;
import android.graphics.Matrix;
public class SYSUtil 
{
	//�N�|�����ର���פ���b�V�q
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
    	int width = bm.getWidth(); 							//�Ϥ��e��
    	int height = bm.getHeight();							//�Ϥ�����
    	Matrix matrix = new Matrix(); 
    	matrix.postScale((float)width_Ratio, (float)height_Ratio);				//�Ϥ�����ҩԻ�����Ӫ�fblRatio��
    	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);//�ŧi�I�}��        	
    	return bmResult;									//�Ǧ^�Q�Y�񪺹Ϥ�
    }
}
