package lwz.com.tank.view;

import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.util.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WaitView extends SurfaceView implements SurfaceHolder.Callback{

	MainActivity activity;
	SurfaceHolder holder;
	Bitmap background;
	Bitmap wait;
	public static float ratio_width=1;
	public static float ratio_height=1;
	public static boolean cdraw = true;
	static  float scalX;
	static  float scalY;
	public WaitView(MainActivity activity)
	{
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
	private void initBitmap() 
	{
		background=PicLoadUtil.loadBM(getResources(), "background.png");
		wait=PicLoadUtil.loadBM(getResources(), "wait.png");
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
				canvas.drawBitmap(wait,30,450, mpaint); 
				try
				{
					holder.unlockCanvasAndPost(canvas);
				}catch(Exception e)
				{
				}
			}
            background.recycle();
			wait.recycle();
		}
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(new MyThread()).start();
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
