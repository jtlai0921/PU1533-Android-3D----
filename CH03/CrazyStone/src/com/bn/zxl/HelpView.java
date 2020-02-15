package com.bn.zxl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import static com.bn.util.Constant.*;

public class HelpView extends RootView 
{
	MyActivity activity;
	
	Bitmap help;
	Bitmap back_button;
	float back_x=49;//傳回按鈕繪制座標
	float back_y=463;
	public HelpView(MyActivity activity) 
	{
		this.activity=activity;
		initBitmap();
	}
	
	protected void initBitmap()
	{
		help=pic2DHashMap.get("help.png");
		back_button=pic2DHashMap.get("back.png");
	}
	@Override
	public void onDraw(Canvas canvas) 
	{
		canvas.drawColor(Color.argb(255, 0, 0, 0));//清除螢幕為黑色
		canvas.drawBitmap(help,0,0, null);//畫背景
		canvas.drawBitmap(back_button, back_x, back_y, null);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (event.getAction()==MotionEvent.ACTION_UP&&bitmapHitTest(event.getX(), event.getY(), back_x, back_y, back_button)) 
		{
			activity.hd.sendEmptyMessage(1);
		}
		return true;
	}
	
}
