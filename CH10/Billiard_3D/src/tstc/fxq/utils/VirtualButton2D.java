package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * 
 * �������s���O
 *
 */
public class VirtualButton2D {
	public float x;
	float y;
	int width;
	int height;
	Bitmap upBmp;
	boolean isDown=false;
	public boolean isDown() {
		return isDown;
	}
	//�Ϥ���a��
	int aDown = 100;//���U�h�ɹϤ���a��
	int aUp = 255;//��_�ɹϤ���a��
	int currA = aUp;//�ثea��
	//������Ĳ���d��
    float addedTouchScaleX = 10;//������x��VĲ���d��
    float addedTouchScaleY = 5;//������Y��VĲ���d��
    //�ʵe�ɡA�Ϥ��Y����
    public float ratio = 1;
	public VirtualButton2D(Bitmap upBmp,float x,float y)
	{
		this.upBmp=upBmp;
		this.x=x;//�N�۹��m�ഫ����ڦ�m
		this.y=y;
		this.width=upBmp.getWidth();
		this.height=upBmp.getHeight();
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		//�]�w�e�����z����
		paint.setAlpha(currA);
		//�z�L�x�}��{�����M�Y��
		Matrix m1=new Matrix();//�h���x�}
		m1.setTranslate(x*Constant.wRatio,  y*Constant.hRatio);//�h���쪺��m		
		Matrix m2=new Matrix();//�Y��x�}		
		m2.setScale(ratio,ratio);//�Y�񪺤��
		Matrix m3=new Matrix();
		m3.setConcat(m1, m2);//�x�}�ۭ�
		//ø��Ϥ�
		canvas.drawBitmap(upBmp, m3,paint);	
		//�٭�e�����z����
		paint.setAlpha(255);
	}
	public void pressDown()
	{
		currA = aDown;
		isDown=true;
	}
	public void releaseUp()
	{
		currA = aUp;
		isDown=false;
	}
	//�P�_���s�O�_��Ĳ���ƥ󪺤�k
	public boolean isActionOnButton(float pressX,float pressY)
	{
		if(
				pressX > (x - addedTouchScaleX)*Constant.wRatio &&
				pressX < (x + addedTouchScaleX)*Constant.wRatio + width && 
				pressY > (y - addedTouchScaleY)*Constant.hRatio &&
				pressY < (y + addedTouchScaleY)*Constant.hRatio + height 
		)
		{
			return true;			
		}
		return false;
	}
}
