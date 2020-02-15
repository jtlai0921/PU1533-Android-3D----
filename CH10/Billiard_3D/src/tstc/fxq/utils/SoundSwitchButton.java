package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 * �]�w���Ī��������s
 *
 */
public class SoundSwitchButton {
	//������s���q
	Bitmap onBitmap;
	Bitmap offBitmap;
	float btnX;
	float btnY;	
	float btnWidth;
	float btnHeight;
	//�����r���q
	Bitmap onTextBitmap;
	Bitmap offTextBitmap;
	float textX;
	float textY;
	float textWidth;
	float textHeight;
	//�䥦�q
	private boolean isOn=false;//���s�}���Ч�
	public SoundSwitchButton(
			Bitmap onTextBitmap,Bitmap offTextBitmap,
			Bitmap onBitmap,Bitmap offBitmap,
			float textX,float textY,
			float btnX,float btnY,
			boolean isOn
			)
	{		
		this.onTextBitmap=onTextBitmap;
		this.offTextBitmap=offTextBitmap;
		this.onBitmap=onBitmap;
		this.offBitmap=offBitmap;
		this.textX=textX;
		this.textY=textY;
		this.textWidth=onTextBitmap.getWidth();
		this.textHeight=onTextBitmap.getWidth();
		
		this.btnX=btnX;
		this.btnY=btnY;
		this.btnWidth=onBitmap.getWidth();
		this.btnHeight=onBitmap.getWidth();
		this.isOn=isOn;
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{		
		if(isOn)
		{
			canvas.drawBitmap(onTextBitmap, textX*Constant.wRatio, textY*Constant.hRatio, paint);//ø���r
			canvas.drawBitmap(onBitmap, btnX*Constant.wRatio, btnY*Constant.hRatio, paint);//ø��������s
		}
		else
		{
			canvas.drawBitmap(offTextBitmap, textX*Constant.wRatio, textY*Constant.hRatio, paint);//ø���r
			canvas.drawBitmap(offBitmap, btnX*Constant.wRatio, btnY*Constant.hRatio, paint);//ø��������s
		}
	}
	//�P�_���s�O�_��Ĳ���ƥ󪺤�k
	public boolean isActionOnButton(int pressX,int pressY)
	{
		if(
				pressX > btnX*Constant.wRatio &&
				pressX < btnX*Constant.wRatio + btnWidth && 
				pressY > btnY*Constant.hRatio &&
				pressY < btnY*Constant.hRatio + btnHeight 
		)
		{
			return true;			
		}
		return false;	
	}
	public boolean isOn() {
		return isOn;
	}
	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
}
