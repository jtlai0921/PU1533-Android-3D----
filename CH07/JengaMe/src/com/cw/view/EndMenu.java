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
import android.widget.Toast;

public class EndMenu extends SurfaceView implements SurfaceHolder.Callback
{
	JengaMeActivity activity;
	Bitmap beijingtupian;
	Bitmap quitgame;
	Bitmap quitgamedown;
	Bitmap playgame;
	Bitmap playdown;
	Bitmap back;
	Bitmap backdown;
	
	static boolean  palyup=true;;
	static boolean quitup=true;
	static boolean	backup=true;
	static boolean optioup=true;
	SurfaceHolder holder;
	 static  float scalX;
	 static  float scalY;
	 
	 public static boolean cdraw=true;
     public EndMenu(JengaMeActivity activity)
     {
         super(activity);
         holder = this.getHolder();//���oholder  
         holder.addCallback(this);
		this.activity=activity;
		this.getHolder().addCallback(this);//�]�w�ͩR�P���^�ձ��f����{��	
		scalX=JengaMeActivity.screenWidth;
		scalY=JengaMeActivity.screenHeight;
		initBitmap();//�_�l���I�}��
     }

 	//�]�w��ť
 	public boolean onTouchEvent(MotionEvent e)
 	{
 		int currentNUm=e.getAction();
 		float x=e.getX();
 		float y=e.getY();		
 		switch(currentNUm)
 		{
 			case MotionEvent.ACTION_DOWN:
 				//�}�l����
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+playgame.getWidth()&&y>320*JengaMeActivity.scaleratioY&&y<320*JengaMeActivity.scaleratioY+playgame.getHeight())
 				{
 					palyup=false;
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+backdown.getWidth()&&y>480*JengaMeActivity.scaleratioY&&y<480*JengaMeActivity.scaleratioY+backdown.getHeight())
 				{
 					backup=false;
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+quitgame.getWidth()&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+quitgame.getHeight())
 				{
 					quitup=false;
 				}
 				break;
 			case MotionEvent.ACTION_UP:	
 				palyup=true;
		    	quitup=true;
		    	backup=true;
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+playgame.getWidth()&&y>320*JengaMeActivity.scaleratioY&&y<320*JengaMeActivity.scaleratioY+playgame.getHeight())
 				{
 					 cdraw=false;
 					 MenuView.cdraw=true;
 					 activity.hd.sendEmptyMessage(0);
 					Toast.makeText(activity, "���b���J�P�P�P�P�P�P", Toast.LENGTH_SHORT).show();
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+backdown.getWidth()&&y>480*JengaMeActivity.scaleratioY&&y<480*JengaMeActivity.scaleratioY+backdown.getHeight())
 				{
 					 //��V�D���ɭ� 
 					 cdraw=false;
 					 MenuView.cdraw=true;
 					activity.hd.sendEmptyMessage(1);
 				}
 				if(x>(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2&&x<(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2+quitgame.getWidth()&&y>640*JengaMeActivity.scaleratioY&&y<640*JengaMeActivity.scaleratioY+quitgame.getHeight())
 				{
 					System.exit(0);
 				}
 			break;
 		}
 		return true;
 	}
 	public void initBitmap()
 	{
 		beijingtupian=SYSUtil.scaleToFit(BitmapFactory.decodeByteArray(Constant.background8,0, Constant.background8.length), JengaMeActivity.scaleratiox,  JengaMeActivity.scaleratioY);
 		playgame=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.oncemore), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 		playdown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.oncemoredown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 		back=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.back0), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 		backdown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.backdown0), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 		quitgame=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.quit), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 		quitgamedown=SYSUtil.scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.quitdown), JengaMeActivity.scaleratiox, JengaMeActivity.scaleratioY);	
 	}
     class MyThread implements Runnable
     {
         public void run()
         {
        	 int width = beijingtupian.getWidth();//���o�귽�I�}�Ϫ��e
             int height = beijingtupian.getHeight();//���o�귽�I�}�Ϫ���
             float w = scalX/beijingtupian.getWidth();
             float h = scalY/beijingtupian.getHeight();
             Rect rect = new Rect (0,0,width,height); 
             RectF rectf = new RectF(0,0,width*w,height*h); 
             
             while(cdraw)
        	 {
        		 Canvas canvas = holder.lockCanvas(null);//���o�e��  
        		 canvas.drawBitmap(beijingtupian,rect,rectf,null); 
                 Paint mPaint = new Paint();
                     if(palyup)
                     {
                    	  canvas.drawBitmap(playgame,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,320*JengaMeActivity.scaleratioY, mPaint); 
                     }else
                     {
                    	  canvas.drawBitmap(playdown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,320*JengaMeActivity.scaleratioY, mPaint); 
                     }
                     if(backup)
                     {
                    	 canvas.drawBitmap(back,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,480*JengaMeActivity.scaleratioY, mPaint);
                     }else
                     {
                    	 canvas.drawBitmap(backdown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,480*JengaMeActivity.scaleratioY, mPaint);
                     }
                     
                     if(quitup)
                     {
                    	 canvas.drawBitmap(quitgame,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,640*JengaMeActivity.scaleratioY, mPaint); 
                     }else
                     {
                    	 canvas.drawBitmap(quitgamedown,(540*JengaMeActivity.scaleratiox-playgame.getWidth())/2,640*JengaMeActivity.scaleratioY, mPaint); 
                     }
                     try
                     {
                    	holder.unlockCanvasAndPost(canvas);//����e���A�ǰe�e�n���ϧ�  
                     }catch(Exception e)
                     {
                    	 
                     }
            }
             beijingtupian.recycle();
             quitgamedown.recycle();
             quitgame.recycle();
             backdown.recycle();
             back.recycle();
             playgame.recycle();
         }
     }
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	public void surfaceCreated(SurfaceHolder holder) {
		 new Thread(new MyThread()).start();
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
