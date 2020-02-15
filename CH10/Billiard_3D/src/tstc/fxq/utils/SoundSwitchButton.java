package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 * 設定音效的虛擬按鈕
 *
 */
public class SoundSwitchButton {
	//關於按鈕的量
	Bitmap onBitmap;
	Bitmap offBitmap;
	float btnX;
	float btnY;	
	float btnWidth;
	float btnHeight;
	//關於文字的量
	Bitmap onTextBitmap;
	Bitmap offTextBitmap;
	float textX;
	float textY;
	float textWidth;
	float textHeight;
	//其它量
	private boolean isOn=false;//按鈕開關標志
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
			canvas.drawBitmap(onTextBitmap, textX*Constant.wRatio, textY*Constant.hRatio, paint);//繪制文字
			canvas.drawBitmap(onBitmap, btnX*Constant.wRatio, btnY*Constant.hRatio, paint);//繪制虛擬按鈕
		}
		else
		{
			canvas.drawBitmap(offTextBitmap, textX*Constant.wRatio, textY*Constant.hRatio, paint);//繪制文字
			canvas.drawBitmap(offBitmap, btnX*Constant.wRatio, btnY*Constant.hRatio, paint);//繪制虛擬按鈕
		}
	}
	//判斷按鈕是否有觸控事件的方法
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
