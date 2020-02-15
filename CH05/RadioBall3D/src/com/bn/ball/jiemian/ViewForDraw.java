package com.bn.ball.jiemian;
import com.bn.ball.RadioBallActivity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//主選單View
public class ViewForDraw extends SurfaceView 
implements SurfaceHolder.Callback  //實現生命周期回調接口
{
	RadioBallActivity activity;
	Paint paint;//畫筆	
	public boolean flag;
	public MySFView curr;	
	
	public ViewForDraw(RadioBallActivity activity) 
	{
		super(activity);
		this.setKeepScreenOn(true);
		this.activity = activity;		
		//設定生命周期回調接口的實現者
		this.getHolder().addCallback(this);
		//起始化畫筆
		paint = new Paint();//建立畫筆
		paint.setAntiAlias(true);//開啟抗鋸齒
	} 
	
	public void initThread()
	{
		flag=true;
		new Thread()
		{
			{
				this.setName("VFD Thread");
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

	public void onDraw(Canvas canvas)
	{		
		if(canvas==null)
		{
			return;
		}
		canvas.clipRect
		(
			new Rect
			(
				0,
				0,
				(int)Constant.SCREEN_WIDTH,
				(int)Constant.SCREEN_HEIGHT
			)
		);

		curr.onDraw(canvas);
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) 
	{
		
	}

	public void surfaceCreated(SurfaceHolder holder) {//建立時被呼叫
		repaint();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//銷毀時被呼叫

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

	//螢幕觸控事件	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if(curr==null)
		{
			return false;
		}
		
		return curr.onTouchEvent(e);
	}
}