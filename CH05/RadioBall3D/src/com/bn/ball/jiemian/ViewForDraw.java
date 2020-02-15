package com.bn.ball.jiemian;
import com.bn.ball.RadioBallActivity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//�D���View
public class ViewForDraw extends SurfaceView 
implements SurfaceHolder.Callback  //��{�ͩR�P���^�ձ��f
{
	RadioBallActivity activity;
	Paint paint;//�e��	
	public boolean flag;
	public MySFView curr;	
	
	public ViewForDraw(RadioBallActivity activity) 
	{
		super(activity);
		this.setKeepScreenOn(true);
		this.activity = activity;		
		//�]�w�ͩR�P���^�ձ��f����{��
		this.getHolder().addCallback(this);
		//�_�l�Ƶe��
		paint = new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���
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

	public void surfaceCreated(SurfaceHolder holder) {//�إ߮ɳQ�I�s
		repaint();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//�P���ɳQ�I�s

	}
	
	public void repaint()
	{
		SurfaceHolder holder=this.getHolder();
		Canvas canvas = holder.lockCanvas();//���o�e��
		try{
			synchronized(holder){
				onDraw(canvas);//ø��
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

	//�ù�Ĳ���ƥ�	
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