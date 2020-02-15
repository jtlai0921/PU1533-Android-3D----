package com.cw.view;

import com.cw.game.Constant;
import com.cw.game.JengaMeActivity;
import com.cw.game.R;
import com.cw.game.R.drawable;
import com.cw.util.SYSUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class OptionMenu extends SurfaceView implements SurfaceHolder.Callback
{
	JengaMeActivity activity;
	Bitmap beijingtupian;
	Bitmap okbutton;
	Bitmap okbuttondown;
	Bitmap backbutton;
	Bitmap backbuttondown;
	Bitmap	backoff;
	Bitmap	backon;
	Bitmap sceneoff;
	Bitmap sceneon;
	static boolean  backup=true;;
	static boolean okup=true;
	static boolean yesflaga=false;
	static boolean yesflagb=false;
	SurfaceHolder holder;
	static  float scalX;
	static  float scalY;
	public static boolean candraw=true;
	
     public OptionMenu(JengaMeActivity activity)
     {
         super(activity);
         holder = this.getHolder();//取得holder  
         holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);//設定生命周期回調接口的實現者	
		scalX=JengaMeActivity.screenWidth;
		scalY=JengaMeActivity.screenHeight;
		initBitmap();//起始化點陣圖
		candraw=true;
     }

     public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
     {
     }
     public void surfaceCreated(SurfaceHolder holder)
     {
    	 yesflagb=activity.sp.getBoolean("playeffect", false);
    	 yesflaga=activity.sp.getBoolean("playback", false);
         new Thread(new MyThread()).start();
     }
     public void surfaceDestroyed(SurfaceHolder holder)
     {
     }
 	 public boolean onTouchEvent(MotionEvent e)
 	{
 		int currentNUm=e.getAction();
 		float x=e.getX();
 		float y=e.getY();
 		switch(currentNUm)
 		{
 		case MotionEvent.ACTION_DOWN:
				//開始游戲
				if(x>30*JengaMeActivity.scaleratiox&&x<30*JengaMeActivity.scaleratiox+okbutton.getWidth()&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+okbutton.getHeight())
				{
					okup=false;
				}
				if(x>(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth()&&x<(540-30)*JengaMeActivity.scaleratiox&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+okbutton.getHeight())
				{
					backup=false;
				}
				if(x>(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox&&x<(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox+sceneon.getWidth()&&y>240*JengaMeActivity.scaleratioY&&y<240*JengaMeActivity.scaleratioY+sceneon.getHeight())
				{
					yesflaga=!yesflaga;
				}
				if(x>(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox&&x<(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox+sceneon.getWidth()&&y>400*JengaMeActivity.scaleratioY&&y<400*JengaMeActivity.scaleratioY+sceneon.getHeight())
				{
					yesflagb=!yesflagb;
				}
				break;
			case MotionEvent.ACTION_UP:	
				okup=true;
	    	    backup=true;
				if(x>30*JengaMeActivity.scaleratiox&&x<30*JengaMeActivity.scaleratiox+okbutton.getWidth()&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+okbutton.getHeight())
				{
					candraw=false;
					MenuView.cdraw=true;
					activity.hd.sendEmptyMessage(1);
					activity.editor.putBoolean("playback",yesflaga);
					activity.editor.putBoolean("playeffect",yesflagb);
					activity.editor.commit();
					activity.playBackMusic=activity.sp.getBoolean("playback", false);
					if(activity.playBackMusic)
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
				}
			if(x>(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth()&&x<(540-30)*JengaMeActivity.scaleratiox&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+backbutton.getHeight())
				{
					candraw=false;
					MenuView.cdraw=true;
					activity.hd.sendEmptyMessage(1);
				}
			break;
 		}
 		return true;
 	}
 	 Bitmap dstbmp;
 	public void initBitmap()
 	{
 		//beijingtupian=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.background1), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		beijingtupian=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background1,0, Constant.background1.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);	
 	 	
 		okbutton=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.ok), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	;	
 		backbutton=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.back), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		okbuttondown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.okdown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	;	
 		backbuttondown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backdown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 		backoff=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backmusicoff), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		backon=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backmusicon), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		sceneoff=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.scenemusicoff), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 		sceneon=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.scenemusicon), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);
 	}
    class MyThread implements Runnable
     {
         public void run()
         {
                int width = beijingtupian.getWidth();//取得資源點陣圖的寬
                int height = beijingtupian.getHeight();//取得資源點陣圖的高
                   float w = scalX/beijingtupian.getWidth();
                   float h = scalY/beijingtupian.getHeight();
                Rect rect = new Rect (0,0,width,height); 
                RectF rectf = new RectF(0,0,width*w,height*h); 
           while(candraw)
           {
        		 Canvas canvas = holder.lockCanvas(null);//取得畫布  
                 Paint mPaint = new Paint();
                 canvas.drawBitmap(beijingtupian,rect,rectf,null);
                 if(okup)
                 {
                	 canvas.drawBitmap(okbutton,30*JengaMeActivity.scaleratiox,640*JengaMeActivity.scaleratioY, mPaint); 
                 }else
                 {
                	 canvas.drawBitmap(okbuttondown,30*JengaMeActivity.scaleratiox,640*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 if(backup)
                 {
                	 canvas.drawBitmap(backbutton,(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth(),640*JengaMeActivity.scaleratioY, mPaint); 
                 }else
                 {
                	 canvas.drawBitmap(backbuttondown,(540-30)*JengaMeActivity.scaleratiox-backbutton.getWidth(),640*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 if(yesflaga)
                 {
                	 canvas.drawBitmap(backon,(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox,240*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 else
                 {
                	 canvas.drawBitmap(backoff,(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox,240*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 if(yesflagb)
                 {
                	 canvas.drawBitmap(sceneon,(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox,400*JengaMeActivity.scaleratioY, mPaint); 
                 }
                 else
                 {
                	 canvas.drawBitmap(sceneoff,(540-backbutton.getWidth())/2*JengaMeActivity.scaleratiox,400*JengaMeActivity.scaleratioY, mPaint);
                 }
                 try
                 {
                	holder.unlockCanvasAndPost(canvas);//解鎖畫布，傳送畫好的圖形  
                 }catch(Exception e)
                 {
                	 
                 }
          }
         beijingtupian.recycle();
         beijingtupian.recycle();
  		okbutton.recycle();
  		backbutton.recycle();
  		okbuttondown.recycle();
  		backbuttondown.recycle();
  		backoff.recycle();
  		backon.recycle();
  		sceneoff.recycle();
  		sceneon.recycle();
       }
     }
}
