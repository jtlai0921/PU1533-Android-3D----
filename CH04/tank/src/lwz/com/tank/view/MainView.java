package lwz.com.tank.view;

import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.util.PicLoadUtil;
import static lwz.com.tank.activity.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback{

	MainActivity activity;
	SurfaceHolder holder;
	Bitmap background;
	Bitmap playup;
	Bitmap playdown;
	Bitmap settingup;
	Bitmap settingdown;
	Bitmap helpup;
	Bitmap helpdown;
	Bitmap exitup;
	Bitmap exitdown;
	public static boolean cdraw = true;
	public static float ratio_width=1;
	public static float ratio_height=1;
	static  float scalX;
	static  float scalY;
	static boolean play=true;
	static boolean setting=true;
	static boolean help=true;
	static boolean exit = true;
	public MainView(MainActivity activity) {
		super(activity);
		holder = this.getHolder();
		holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);
		scalX=MainActivity.screenWidth;
		scalY=MainActivity.screenHeight;
		initBitmap();
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		int currentNum = e.getAction();
		float x = e.getX();
		float y = e.getY();
		switch(currentNum)
		{
			case MotionEvent.ACTION_DOWN:
				if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(60+ssr.lucY)*ssr.ratio&&y<(60+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
					play=false;
 				}if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(180+ssr.lucY)*ssr.ratio&&y<(180+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
 					setting=false;	
 				}if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(300+ssr.lucY)*ssr.ratio&&y<(300+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
 					help=false;
 				}if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(420+ssr.lucY)*ssr.ratio&&y<(420+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
 					exit=false;
 				}
				break;
			case MotionEvent.ACTION_UP:
				play=true;
				setting = true;
				help=true;
				exit=true;
				if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(60+ssr.lucY)*ssr.ratio&&y<(60+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
					cdraw=false;
					activity.hd.sendEmptyMessage(3);
 				}if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(180+ssr.lucY)*ssr.ratio&&y<(180+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
 					cdraw=false;
 					activity.hd.sendEmptyMessage(1);
 				}if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(300+ssr.lucY)*ssr.ratio&&y<(300+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
 					cdraw=false;
 					activity.hd.sendEmptyMessage(6);
 				}if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+playup.getWidth()&&y>(420+ssr.lucY)*ssr.ratio&&y<(420+ssr.lucY)*ssr.ratio+playup.getHeight())
 				{
 					System.exit(0);
 					activity.editor.putBoolean("playback", false);
 				}
				break;
		}
		return true;
	}

	private void initBitmap() {
		background=PicLoadUtil.loadBM(getResources(), "background.png");
		playup=PicLoadUtil.loadBM(getResources(), "playup.png");
		playdown=PicLoadUtil.loadBM(getResources(), "playdown.png");
		settingup=PicLoadUtil.loadBM(getResources(), "settingup.png");
		settingdown=PicLoadUtil.loadBM(getResources(), "settingdown.png");
		helpup=PicLoadUtil.loadBM(getResources(), "helpup.png");
		helpdown=PicLoadUtil.loadBM(getResources(), "helpdown.png");
		exitup=PicLoadUtil.loadBM(getResources(), "exitup.png");
		exitdown=PicLoadUtil.loadBM(getResources(), "exitdown.png");
		
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
				if(play)
				{
					canvas.drawBitmap(playup,50,60, mpaint); 
				}else
				{
					canvas.drawBitmap(playdown,50,60, mpaint);
				}if(setting)
				{
					canvas.drawBitmap(settingup,50*1,180*1, mpaint); 
				}else
				{
					canvas.drawBitmap(settingdown,50*1,180*1, mpaint); 
				}if(help)
				{
					canvas.drawBitmap(helpup,50*1,300*1, mpaint); 
				}else
				{
					canvas.drawBitmap(helpdown,50*1,300*1, mpaint); 
				}if(exit)
				{
					canvas.drawBitmap(exitup,50*1,420*1, mpaint); 
				}else
				{
					canvas.drawBitmap(exitdown,50*1,420*1, mpaint); 
				}
				try
				{
					holder.unlockCanvasAndPost(canvas);
				}catch(Exception e)
				{
				}
			}
			background.recycle();
			playup.recycle();
			playdown.recycle();
			settingup.recycle();
			settingdown.recycle();
			helpup.recycle();
			helpdown.recycle();
			exitup.recycle();
			exitdown.recycle();
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
