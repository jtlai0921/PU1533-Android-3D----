package com.bn.zxl;

import static com.bn.util.Constant.ssr;

import com.bn.util.Constant;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class DrawCurrView extends SurfaceView implements Callback {

	MyActivity activity;
	RootView currView;
	boolean flag;
	public DrawCurrView(MyActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
	}
	
	public void repaint()
	{
		SurfaceHolder holder=this.getHolder();
		Canvas canvas = holder.lockCanvas();//取得畫布
		try{
			synchronized(holder){
				onDraw(canvas);//繪制
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	public void initThread()
	{
		flag=true;
		new Thread()
		{
			{
				this.setName("DrawCurrView Thread");
			}
			public void run()
			{
				while(flag)
				{
					repaint();
					try 
					{
						Thread.sleep(40);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	@Override
	protected void onDraw(Canvas canvas) 
	{
		if (currView==null) 
		{
			return;
		}
		canvas.save();
		canvas.clipRect(0, 0, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
		canvas.translate(ssr.lucX, ssr.lucY);
		canvas.scale(ssr.ratio, ssr.ratio);
		
		
		currView.onDraw(canvas);
		
		canvas.restore();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (currView==null) 
		{
			return false;
		}
		
		return currView.onTouchEvent(event);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		initThread();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
