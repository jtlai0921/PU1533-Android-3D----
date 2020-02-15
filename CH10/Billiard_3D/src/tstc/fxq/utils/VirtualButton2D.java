package tstc.fxq.utils;

import tstc.fxq.constants.Constant;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * 
 * 虛擬按鈕類別
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
	//圖片的a值
	int aDown = 100;//按下去時圖片的a值
	int aUp = 255;//抬起時圖片的a值
	int currA = aUp;//目前a值
	//延伸的觸控範圍
    float addedTouchScaleX = 10;//延伸的x方向觸控範圍
    float addedTouchScaleY = 5;//延伸的Y方向觸控範圍
    //動畫時，圖片縮放比例
    public float ratio = 1;
	public VirtualButton2D(Bitmap upBmp,float x,float y)
	{
		this.upBmp=upBmp;
		this.x=x;//將相對位置轉換成實際位置
		this.y=y;
		this.width=upBmp.getWidth();
		this.height=upBmp.getHeight();
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		//設定畫筆的透明度
		paint.setAlpha(currA);
		//透過矩陣實現平移和縮放
		Matrix m1=new Matrix();//搬移矩陣
		m1.setTranslate(x*Constant.wRatio,  y*Constant.hRatio);//搬移到的位置		
		Matrix m2=new Matrix();//縮放矩陣		
		m2.setScale(ratio,ratio);//縮放的比例
		Matrix m3=new Matrix();
		m3.setConcat(m1, m2);//矩陣相乘
		//繪制圖片
		canvas.drawBitmap(upBmp, m3,paint);	
		//還原畫筆的透明度
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
	//判斷按鈕是否有觸控事件的方法
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
