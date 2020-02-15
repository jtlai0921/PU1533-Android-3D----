package lwz.com.tank.view;

import static lwz.com.tank.activity.Constant.*;
import lwz.com.tank.activity.Constant;
import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.util.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SettingView extends SurfaceView implements SurfaceHolder.Callback{

	MainActivity activity;
	SurfaceHolder holder;
	Bitmap background;
	Bitmap musicon;
	Bitmap musicoff;
	Bitmap soundon;
	Bitmap soundoff;
	Bitmap back;
	public static boolean cdraw = true;
	public static float ratio_width=1;
	public static float ratio_height=1;
	static  float scalX;
	static  float scalY;
	static boolean musicflag = false;
	static boolean soundflag = false;
	public SettingView(MainActivity activity) {
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
		float x = e.getX();
		float y = e.getY();
		switch(currentNum)
		{
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:	
				if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+musicon.getWidth()&&y>(90+ssr.lucY)*ssr.ratio&&y<(90+ssr.lucY)*ssr.ratio+musicon.getHeight())
 				{
					musicflag=!musicflag;
					activity.editor.putBoolean("playback", musicflag);
					activity.editor.commit();
					activity.playbackmusic=activity.sp.getBoolean("playback", false);
					if(activity.playbackmusic)
			        {
						activity.soundutil.play_bg_sound();  
			        }else
			        {
			        	try
			        	{
			        		activity.soundutil.stop_bg_sound();
			        	}
			        	catch(Exception exception)
			        	{
			        		System.out.println(""+exception);
			        	}
			        }
 				}else if(x>(50+ssr.lucX)*ssr.ratio&&x<(50+ssr.lucX)*ssr.ratio+musicon.getWidth()&&y>(250+ssr.lucY)*ssr.ratio&&y<(240+ssr.lucY)*ssr.ratio+musicon.getHeight())
 				{
 					soundflag=!soundflag;
 					activity.editor.putBoolean("playeffect", soundflag);
 					activity.editor.commit();
 				}
				if(x>(106+ssr.lucX)*ssr.ratio&&x<(106+ssr.lucX)*ssr.ratio+back.getWidth()&&y>(390+ssr.lucY)*ssr.ratio&&y<(390+ssr.lucY)*ssr.ratio+back.getHeight())
 				{
					cdraw=false;
					MainView.cdraw=true;
					activity.hd.sendEmptyMessage(0);
 				}
				break;
		}
		return true;
	}
	
	private void initBitmap() {
		background=PicLoadUtil.loadBM(getResources(), "background.png");
		musicon=PicLoadUtil.loadBM(getResources(), "musicon.png");
		musicoff=PicLoadUtil.loadBM(getResources(), "musicoff.png");
		soundon=PicLoadUtil.loadBM(getResources(), "soundon.png");
		soundoff=PicLoadUtil.loadBM(getResources(), "soundoff.png");
		back=PicLoadUtil.loadBM(getResources(), "back.png");
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
				canvas.drawBitmap(background, 0,0,null);
				canvas.save();
				canvas.restore();
				if(musicflag)
				{
					canvas.drawBitmap(musicon,50*MainActivity.scaleratiox,90*MainActivity.scaleratioY, mpaint); 
				}else
				{
					canvas.drawBitmap(musicoff,50*MainActivity.scaleratiox,90*MainActivity.scaleratioY, mpaint); 
				}if(soundflag)
				{
					canvas.drawBitmap(soundon,50*MainActivity.scaleratiox,240*MainActivity.scaleratioY, mpaint); 
				}else
				{
					canvas.drawBitmap(soundoff,50*MainActivity.scaleratiox,240*MainActivity.scaleratioY, mpaint); 
				}
				canvas.drawBitmap(back,106*MainActivity.scaleratiox,390*MainActivity.scaleratioY, mpaint); 
				try
				{
					holder.unlockCanvasAndPost(canvas);
				}catch(Exception e)
				{
					
				}
			}
			background.recycle();
			musicon.recycle();
			musicoff.recycle();
			soundon.recycle();
			soundoff.recycle();
			back.recycle();
		}
		
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		musicflag = activity.sp.getBoolean("playback", false);
		soundflag = activity.sp.getBoolean("playeffect", false);
		new Thread(new MyThread()).start();
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		
	}

}
