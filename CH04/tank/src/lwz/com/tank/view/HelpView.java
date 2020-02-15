package lwz.com.tank.view;

import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.util.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView extends SurfaceView implements SurfaceHolder.Callback{

	MainActivity activity;
	SurfaceHolder holder;
	Bitmap background;
	public static float ratio_width=1;
	public static float ratio_height=1;
	public static boolean cdraw = true;
	static  float scalX;
	static  float scalY;
	public HelpView(MainActivity activity) {
		super(activity);
		holder = this.getHolder();
		holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);
		scalX=MainActivity.screenWidth;
		scalY=MainActivity.screenHeight;
		initBitmap();
		cdraw = true;
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		int currentNum = e.getAction();
		switch(currentNum)
		{
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		return true;
	}

	private void initBitmap() {
		background=PicLoadUtil.loadBM(getResources(), "help.png");
	}
	class MyThread implements Runnable
	{
		public void run() {
			while(cdraw)
			{
				Paint mpaint = new Paint();
				Canvas canvas = holder.lockCanvas(null);
				canvas.translate(Constant.ssr.lucX, Constant.ssr.lucY);
				canvas.scale(Constant.ssr.ratio, Constant.ssr.ratio); //呼叫scale方法將目前畫布縮放到特定的比例
				canvas.drawBitmap(background, 0,0,mpaint);
				canvas.save();
				canvas.restore();
				try
				{
					holder.unlockCanvasAndPost(canvas);
				}catch(Exception e)
				{
					
				}
			}
			background.recycle();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(new MyThread()).start();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
